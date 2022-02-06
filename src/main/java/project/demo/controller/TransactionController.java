package project.demo.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.demo.entity.CoinStock;
import project.demo.entity.Student;
import project.demo.repository.CoinStockRepository;
import project.demo.repository.StudentRepository;
import project.demo.service.StockService;
import project.demo.util.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class TransactionController {

    @Autowired
    StockService stockService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CoinStockRepository stockRepository;

    @GetMapping("stock")
    public ResponseEntity<?> getStock() {
        CoinStock output = stockService.getStock((long) 1);
        if (output != null) {
            return ResponseEntity.ok(ProjectMapper.INSTANCE.getStock(output));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<?> buy(@RequestBody Student stu, @RequestParam(value = "amount", required = false) Integer amount) {
        Student student = studentRepository.findById(stu.getId()).orElse(null);
        CoinStock stock = stockService.getStock((long) 1);
        stock.setAmount(stock.getAmount() - amount);
        student.setMoney(student.getMoney() - 10.0 * amount);
        student.setCoinAmount(student.getCoinAmount() + amount);
        CoinStock output = stockRepository.save(stock);
        studentRepository.save(student);
        return ResponseEntity.ok(ProjectMapper.INSTANCE.getStock(output));
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody Student stu, @RequestParam(value = "amount", required = false) Integer amount) {
        Student student = studentRepository.findById(stu.getId()).orElse(null);
        CoinStock stock = stockService.getStock((long) 1);
        stock.setAmount(stock.getAmount() + amount);
        student.setMoney(student.getMoney() + 10.0 * amount);
        student.setCoinAmount(student.getCoinAmount() - amount);
        CoinStock output = stockRepository.save(stock);
        studentRepository.save(student);
        return ResponseEntity.ok(ProjectMapper.INSTANCE.getStock(output));
    }
}
