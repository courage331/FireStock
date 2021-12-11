package kr.co.firestock.vo;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PersonalPension {

    String stockName;
    String stockCode;
    String averagePrice;
    String currentPrice;
    String quantity;
    String yield;       //수익률
    String purchaseAmount; //매수금액
    String balanceAssessment;//잔고평가액
    String currentWeight; //현재 비중
    String targetWeight; //목표 비중
    String targetQuantity; //목표 수량
    String operationFee; //운용보수
    String payCalculation; //보수계산
    String type; //어느 타입인지

    @Builder
    public PersonalPension(String stockName, String stockCode, String averagePrice, String currentPrice, String quantity, String yield, String purchaseAmount, String balanceAssessment, String currentWeight, String targetWeight, String targetQuantity, String operationFee, String payCalculation, String type) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.averagePrice = averagePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.yield = yield;
        this.purchaseAmount = purchaseAmount;
        this.balanceAssessment = balanceAssessment;
        this.currentWeight = currentWeight;
        this.targetWeight = targetWeight;
        this.targetQuantity = targetQuantity;
        this.operationFee = operationFee;
        this.payCalculation = payCalculation;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalPension)) return false;
        PersonalPension that = (PersonalPension) o;
        return Objects.equals(stockName, that.stockName) && Objects.equals(stockCode, that.stockCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, stockCode, averagePrice, currentPrice, quantity, yield, purchaseAmount, balanceAssessment, currentWeight, targetWeight, targetQuantity, operationFee, payCalculation);
    }
}
