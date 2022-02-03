package kr.co.firestock.vo;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "stocks")
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