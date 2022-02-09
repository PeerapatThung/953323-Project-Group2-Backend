package project.demo.config;

import project.demo.entity.CoinStock;
import project.demo.entity.Student;
import project.demo.repository.CoinStockRepository;
import project.demo.repository.StudentRepository;
import project.demo.security.entity.Authority;
import project.demo.security.entity.AuthorityName;
import project.demo.security.entity.User;
import project.demo.security.repository.AuthorityRepository;
import project.demo.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    CoinStockRepository coinStockRepository;
    @Autowired
    StudentRepository studentRepository;
    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        User user1;
        CoinStock stock;
        Student student;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        Authority authUser = Authority.builder().name(AuthorityName.ROLE_USER).build();

        stock = CoinStock.builder()
                .stockname("CAMT Coin")
                .amount((int)(Math.random()* 1200)).build();

        user1 = User.builder()
                .username("student1")
                .email("student1@admin.com")
                .password(encoder.encode("student1"))
                .firstname("student1")
                .lastname("student1")
                .enabled(true)
                .lastPasswordResetDate(Date.from(LocalDate.of(2021,01,01).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                .build();
        student = Student.builder()
                    .name(user1.getFirstname())
                    .money(200.00)
                    .buyLimit(5)
                    .coinAmount(5).build();
        authorityRepository.save(authUser);
        student.setAccount(user1);
        user1.setMember(student);
        user1.getAuthorities().add(authUser);
        userRepository.save(user1);
        studentRepository.save(student);
        coinStockRepository.save(stock);
    }


}
