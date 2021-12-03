package kr.co.firestock.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PortFolioDetail {

    String portFolioType;

    String portFolioMoney;

    List<DomesticStock> domesticStocks;

    List<OverseasStock> overseasStocks;

    List<ISA> isas;

    List<PersonalPension> personalPensions;

    List<RetirementPension> retirementPensions;

    List<CryptoCurrency> cryptoCurrencys;

    List<NonCurrentAssets> nonCurrentAssets;

    @Builder
    public PortFolioDetail(String portFolioType, String portFolioMoney, List<DomesticStock> domesticStocks, List<OverseasStock> overseasStocks, List<ISA> isas, List<PersonalPension> personalPensions, List<RetirementPension> retirementPensions, List<CryptoCurrency> cryptoCurrencys, List<NonCurrentAssets> nonCurrentAssets) {
        this.portFolioType = portFolioType;
        this.portFolioMoney = portFolioMoney;
        this.domesticStocks = domesticStocks;
        this.overseasStocks = overseasStocks;
        this.isas = isas;
        this.personalPensions = personalPensions;
        this.retirementPensions = retirementPensions;
        this.cryptoCurrencys = cryptoCurrencys;
        this.nonCurrentAssets = nonCurrentAssets;
    }
}
