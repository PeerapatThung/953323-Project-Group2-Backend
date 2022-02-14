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

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> buy(@RequestBody Student body) {
        Map result = new HashMap();

        Student student = studentRepository.findById(body.getId()).orElse(null);
        CoinStock stock = stockService.getStock((long) 1);
        if (body.getCoinAmount() > stock.getAmount() || student.getMoney() < body.getCoinAmount()*10.0 || stock.getAmount() <=0 || student.getBuyLimit() == 0 || body.getCoinAmount() >5){
            return (ResponseEntity<?>) ResponseEntity.badRequest();
        }
        stock.setAmount(stock.getAmount() - body.getCoinAmount());
        student.setBuyLimit(student.getBuyLimit() - body.getCoinAmount());
        student.setMoney(student.getMoney() - (10.0 * body.getCoinAmount()));
        student.setCoinAmount(student.getCoinAmount() + body.getCoinAmount());
        CoinStock output = stockRepository.save(stock);
        studentRepository.save(student);
        result.put("student", ProjectMapper.INSTANCE.getStudentDTO(student));
        System.out.println(output);
        result.put("stock", ProjectMapper.INSTANCE.getStock(output));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sell")
    public ResponseEntity<?> sell(@RequestBody Student body) {
        Map result = new HashMap();
        Student student = studentRepository.findById(body.getId()).orElse(null);
        CoinStock stock = stockService.getStock((long) 1);
        stock.setOriginamount(stock.getAmount());
        stockRepository.save(stock);
        System.out.println(body);
        if(student.getCoinAmount() < body.getCoinAmount()) {return (ResponseEntity<?>) ResponseEntity.badRequest();}

        if (student.getCoinAmount() > 5) {
            student.setMoney(student.getMoney() + body.getCoinAmount() * body.getMoney());
        } else {
            student.setMoney(student.getMoney() + 10.0 * body.getCoinAmount());
        }
        stock.setAmount(stock.getAmount() + body.getCoinAmount());
        student.setCoinAmount(student.getCoinAmount() - body.getCoinAmount());
        CoinStock output = stockRepository.save(stock);
        studentRepository.save(student);
        result.put("student", ProjectMapper.INSTANCE.getStudentDTO(student));
        result.put("stock", ProjectMapper.INSTANCE.getStock(output));
        return ResponseEntity.ok(result);
    }
}
