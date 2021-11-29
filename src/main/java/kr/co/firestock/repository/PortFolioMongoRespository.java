package kr.co.firestock.repository;


import kr.co.firestock.vo.PortFolio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortFolioMongoRespository extends MongoRepository<PortFolio, String> {

    PortFolio findBy_id(String _id);

    Boolean existsBy_id(String _id);

}
