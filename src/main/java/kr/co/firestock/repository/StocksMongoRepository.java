package kr.co.firestock.repository;

import kr.co.firestock.vo.CryptoCurrencyVO;
import kr.co.firestock.vo.Stocks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StocksMongoRepository extends MongoRepository<Stocks,String> {

    Stocks findByStockInfo(String stockInfo);
//
//    @Query(value = "select * from stocks c where c.stockInfo in :markets", nativeQuery = true)
//    List<Stocks> findStockInfoIn(String[] markets);
}
