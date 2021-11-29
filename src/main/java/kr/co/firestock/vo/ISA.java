package kr.co.firestock.vo;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ISA {

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

    @Builder
    public ISA(String stockName, String stockCode, String averagePrice, String currentPrice, String quantity, String todaysFluctuationRate, String yield, String valuationLoss, String purchaseAmount, String balanceAssessment) {
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.averagePrice = averagePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.todaysFluctuationRate = todaysFluctuationRate;
        this.yield = yield;
        this.valuationLoss = valuationLoss;
        this.purchaseAmount = purchaseAmount;
        this.balanceAssessment = balanceAssessment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISA)) return false;
        ISA isa = (ISA) o;
        return Objects.equals(stockName, isa.stockName) && Objects.equals(stockCode, isa.stockCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, stockCode, averagePrice, currentPrice, quantity, todaysFluctuationRate, yield, valuationLoss, purchaseAmount, balanceAssessment);
    }
}
