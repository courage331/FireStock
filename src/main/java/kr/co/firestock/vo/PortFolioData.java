package kr.co.firestock.vo;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class PortFolioData {
    String stockInfo;
    String stockName;
    String stockType;
    String stockPrice; //구매 가격 -> 평균 단가
//    String currentPrice; //현재 가격
//    String totSum; //총 금액
//    String totProfit; //총 수익
    String stockAmount; //총 수량
}

