package kr.co.firestock.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqBodyFormat {

    String stockName;
    String stockCode;
    String averagePrice;
    String currentPrice;
    String quantity;
    String todaysFluctuationRate; //금일 등락률
    String yield;       //수익률
    String valuationLoss; //평가 손익
    String purchaseAmount; //매수금액
    String balanceAssessment;//잔고평가액
    String exchange; //거래소
    String monthlyPayment; //월 납입 금액
    String monthlyPaymentMonth;// 납입 월 수
    String totalPayment; //총 납입 금액
    String totalAppraisalValue; //총 평가 금액
    String currentWeight; //현재 비중
    String targetWeight; //목표 비중
    String targetQuantity; //목표 수량
    String operationFee; //운용보수
    String payCalculation; //보수계산

}
