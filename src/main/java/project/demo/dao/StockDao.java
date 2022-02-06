package project.demo.dao;

import project.demo.entity.CoinStock;

import java.util.Optional;

public interface StockDao {
    CoinStock getStock(Long id);
    CoinStock save(CoinStock stock);
}
