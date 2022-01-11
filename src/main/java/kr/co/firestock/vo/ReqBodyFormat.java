package kr.co.firestock.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBodyFormat {

    String stockInfo;
    String stockName;
    String stockType;
    String stockPrice; //구매 가격
    String stockAmount; //총 수량

}
