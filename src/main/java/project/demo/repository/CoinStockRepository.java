package project.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.demo.entity.CoinStock;

public interface CoinStockRepository extends JpaRepository<CoinStock, Long> {

}
