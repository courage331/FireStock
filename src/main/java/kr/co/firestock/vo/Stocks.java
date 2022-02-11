package kr.co.firestock.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document("stocks")
public class Stocks {
    @Id
    String _id;

    String stockInfo;

    String stockType;

    String stockName;

    String currentWonPrice;

    String currentDollarPrice;

    String updt;
}