package kr.co.firestock.repository;

import kr.co.firestock.vo.ScheduledStock;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduledStockRepository extends MongoRepository<ScheduledStock,String> {



}
