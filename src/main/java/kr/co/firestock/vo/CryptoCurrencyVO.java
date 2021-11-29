package kr.co.firestock.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cryptocurrency")
public class CryptoCurrencyVO {

    @Id
    String market;

    String korean_name;

    String english_name;

    String candle_date_time_utc;
    String candle_date_time_kst;
    String opening_price;
    String high_price;
    String low_price;
    String trade_price;
    String timestamp;
    String candle_acc_trade_price;
    String candle_acc_trade_volume;
    String unit;
    String updt;
}
