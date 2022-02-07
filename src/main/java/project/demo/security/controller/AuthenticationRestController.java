package project.demo.security.controller;


import project.demo.entity.Student;
import project.demo.repository.StudentRepository;
import project.demo.security.JwtTokenUtil;
import project.demo.security.entity.Authority;
import project.demo.security.entity.AuthorityName;
import project.demo.security.entity.JwtUser;
import project.demo.security.entity.User;
import project.demo.security.repository.AuthorityRepository;
import project.demo.security.repository.UserRepository;
import project.demo.util.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    AuthorityRepository authorityRepository;

    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        User user = userRepository.findById(((JwtUser) userDetails).getId()).orElse(null);
        Map result = new HashMap();
        result.put("token", token); 
        if (user != null) {
//            result.put("authorities", ProjectMapper.INSTANCE.getAuthorityDTO(user.getAuthorities()));
            result.put("user", ProjectMapper.INSTANCE.getUserDto(user));
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping(value = "${jwt.route.authentication.refresh}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("${jwt.route.register.path}")
    public ResponseEntity<?> addUser(@RequestBody JwtAuthenticationRequest authenticationRequest) {
       if(userRepository.findByUsername(authenticationRequest.getUsername()) == null){
            Authority authUser = Authority.builder().name(AuthorityName.ROLE_USER).build();
            authorityRepository.save(authUser);
            User user = new User();
            user.getAuthorities().add(authUser);
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(authenticationRequest.getPassword()));
            user.setEmail(authenticationRequest.getEmail());
            user.setUsername(authenticationRequest.getUsername());
            user.setFirstname(authenticationRequest.getFirstname());
            user.setLastname(authenticationRequest.getLastname());
            user.setEnabled(true);
            userRepository.save(user);

            Student student = Student.builder()
                    .money(200.00)
                    .name(user.getFirstname())
                    .coinAmount(0)
                    .build();
            student.setAccount(user);
            studentRepository.save(student);
            return ResponseEntity.ok("Register successfully");

        }
        return (ResponseEntity<?>) ResponseEntity.badRequest();
    }
}
