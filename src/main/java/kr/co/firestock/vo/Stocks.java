package kr.co.firestock.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stocks {
    String ticker;
    String name;
    String market;
    String locale;
    String primaryExchange;
    String active;
    String currencyName;
    String cik;
    String compostieFigi;
    String shareClassFigi;
    String lastUpdateUtc;
}
