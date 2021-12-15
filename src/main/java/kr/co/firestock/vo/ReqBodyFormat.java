package kr.co.firestock.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBodyFormat {

    String stockInfo;
    String stockName;
    String stockType;
    String purchasePrice; //구매 가격
    String currentPrice; //현재 가격
    String totSum; //총 금액
    String totProfit; //총 수익
    String totAmount; //총 수량

}
