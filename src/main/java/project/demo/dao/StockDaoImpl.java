package project.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.demo.entity.CoinStock;
import project.demo.repository.CoinStockRepository;

import java.util.Optional;

@Repository
@Profile("db")
public class StockDaoImpl implements StockDao {
    @Autowired
    CoinStockRepository stockRepository;

    @Override
    public CoinStock getStock(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public CoinStock save(CoinStock stock) {
        return stockRepository.save(stock);
    }
}
