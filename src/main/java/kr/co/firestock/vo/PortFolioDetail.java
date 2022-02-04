package kr.co.firestock.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PortFolioDetail {

    String portFolioType;

    double portFolioWonMoney;

    double portFolioDollarMoney;

    String regDt;

    String upDt;

    List<PortFolioData> portFolioDataList;

    public PortFolioDetail(String portFolioType, int portFolioWonMoney, int portFolioDollarMoney, String regDt, String upDt, List<PortFolioData> portFolioDataList) {
        this.portFolioType = portFolioType;
        this.portFolioWonMoney = portFolioWonMoney;
        this.portFolioDollarMoney = portFolioDollarMoney;
        this.regDt = regDt;
        this.upDt = upDt;
        this.portFolioDataList = portFolioDataList;
    }
}
