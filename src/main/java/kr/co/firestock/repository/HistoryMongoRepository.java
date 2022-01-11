package kr.co.firestock.repository;

import kr.co.firestock.vo.History;
import kr.co.firestock.vo.PortFolio;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryMongoRepository extends MongoRepository<History, String> {

    List<History> findByUserId(String userId);

    List<History> findByUserIdAndPortFolioName(String userId, String portFolioName);

    List<History> findByUserIdAndType(String userId, String type);

    List<History> findByUserIdAndTypeAndPortFolioName(String userId, String type, String portFolioName);

}
