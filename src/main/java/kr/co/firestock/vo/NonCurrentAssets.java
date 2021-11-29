package kr.co.firestock.vo;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NonCurrentAssets {

    String stockName;
    String monthlyPayment; //월 납입 금액
    String monthlyPaymentMonth;// 납입 월 수
    String totalPayment; //총 납입 금액
    String totalAppraisalValue; //총 평가 금액

    @Builder
    public NonCurrentAssets(String stockName, String monthlyPayment, String monthlyPaymentMonth, String totalPayment, String totalAppraisalValue) {
        this.stockName = stockName;
        this.monthlyPayment = monthlyPayment;
        this.monthlyPaymentMonth = monthlyPaymentMonth;
        this.totalPayment = totalPayment;
        this.totalAppraisalValue = totalAppraisalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NonCurrentAssets)) return false;
        NonCurrentAssets that = (NonCurrentAssets) o;
        return Objects.equals(stockName, that.stockName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockName, monthlyPayment, monthlyPaymentMonth, totalPayment, totalAppraisalValue);
    }
}
