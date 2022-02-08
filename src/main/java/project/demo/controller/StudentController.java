package project.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import project.demo.security.entity.User;
import project.demo.security.repository.UserRepository;
import project.demo.util.ProjectMapper;


@Controller
public class StudentController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping ("/getInfo/{uid}")
    public ResponseEntity<?> getInfo(@PathVariable(value = "uid", required = false) Long uid)  throws AuthenticationException {
        System.out.println(uid);
        User user = userRepository.findById(uid).orElse(null);
        return ResponseEntity.ok(ProjectMapper.INSTANCE.getUserDto(user));
    }
}
