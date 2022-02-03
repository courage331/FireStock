package kr.co.firestock.vo;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class PortFolioData {
    String stockInfo;
    String stockType;
    String stockName;
    String stockPrice; //구매 가격 -> 평균 단가
    String stockAmount; //총 수량
}

