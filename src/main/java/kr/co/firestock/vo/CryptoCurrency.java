package kr.co.firestock.vo;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CryptoCurrency {

    String stockName;
    String exchange; //거래소
    String averagePrice;
    String currentPrice;
    String quantity;
    String yield;       //수익률
    String valuationLoss; //평가 손익
    String purchaseAmount; //매수금액
    String balanceAssessment;//잔고평가액
    String type; //어느 타입인지

    @Builder
    public CryptoCurrency(String stockName, String exchange, String averagePrice, String currentPrice, String quantity, String yield, String valuationLoss, String purchaseAmount, String balanceAssessment, String type) {
        this.stockName = stockName;
        this.exchange = exchange;
        this.averagePrice = averagePrice;
        this.currentPrice = currentPrice;
        this.quantity = quantity;
        this.yield = yield;
        this.valuationLoss = valuationLoss;
        this.purchaseAmount = purchaseAmount;
        this.balanceAssessment = balanceAssessment;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CryptoCurrency)) return false;
        CryptoCurrency that = (CryptoCurrency) o;
        return Objects.equals(stockName, that.stockName) && Objects.equals(exchange, that.exchange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, exchange, averagePrice, currentPrice, quantity, yield, valuationLoss, purchaseAmount, balanceAssessment);
    }
}
