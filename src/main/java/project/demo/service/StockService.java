package project.demo.service;

import org.springframework.stereotype.Service;
import project.demo.entity.CoinStock;

import java.util.Optional;

@Service
public interface StockService {
    CoinStock getStock(Long id);
    CoinStock save(CoinStock stock);
}
