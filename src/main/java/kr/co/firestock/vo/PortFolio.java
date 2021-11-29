package kr.co.firestock.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashMap;

@Document("portpolio")
@Getter
@Setter
@ToString
public class PortFolio {

    @Id
    String _id;

    HashMap<String, PortFolioDetail> portPolioDetailMap;

    @Builder
    public PortFolio(String _id, HashMap<String, PortFolioDetail> portPolioDetailMap) {
        this._id = _id;
        this.portPolioDetailMap = portPolioDetailMap;
    }
}
