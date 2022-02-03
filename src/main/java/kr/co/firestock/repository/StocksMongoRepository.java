package kr.co.firestock.repository;

import kr.co.firestock.vo.Stocks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StocksMongoRepository extends MongoRepository<Stocks,String> {
}
