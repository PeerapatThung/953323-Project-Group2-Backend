package project.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.demo.dao.StockDao;
import project.demo.entity.CoinStock;

import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    StockDao stockDao;

    @Override
    public CoinStock getStock(Long id) {
        return stockDao.getStock(id);
    }

    @Override
    public CoinStock save(CoinStock stock) {
        return stockDao.save(stock);
    }

    @Scheduled(fixedRate = 86400)
    public void cronJobSch() {
        Integer number = (int)(Math.random()* 1200);
        if((stockDao.getStock(1L) != null)){
            CoinStock c = stockDao.getStock(1L);
            c.setAmount(number);
            stockDao.save(c);
        }
    }

}
