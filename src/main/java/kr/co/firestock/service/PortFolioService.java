package kr.co.firestock.service;

import kr.co.firestock.repository.PortFolioMongoRespository;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class PortFolioService {

    @Autowired
    PortFolioMongoRespository portFolioMongoRespository;

    public ResponseInfo createFolio(String userId) {
        Boolean isExist = portFolioMongoRespository.existsBy_id(userId);
        if (!isExist) {
            portFolioMongoRespository.save(new PortFolio(userId, new HashMap<>()));
            return new ResponseInfo(0, "Create Success");
        } else {
            return new ResponseInfo(-1, "userId Already Exists!");
        }
    }

    public ResponseInfo createFolioName(String userId, String portFolioType, String portFolioName) {
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = portFolio.getPortFolioDetailMap();
        if (map.size() != 0) {
            for (String key : map.keySet()) {
                /**중복되는 포트폴리오 이름이 있는경우 */
                if (key.equals(portFolioName)) {
                    return new ResponseInfo(-1, "[중복되는 포트폴리오 명이 있음]");
                }
            }
        }
        String date = new StringUtil().makeTodayDate();
        map.put(portFolioName, new PortFolioDetail(portFolioType, "0", date, date, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        portFolio.setPortFolioDetailMap(map);
        portFolioMongoRespository.save(portFolio);
        log.info("[{} 포트폴리오 추가]", portFolioName);
        return new ResponseInfo(0, "[" + portFolioName + " 포트폴리오 추가]");
    }


    public ResponseInfo findPortFolio(String userId) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        if(null == polio){
            return new ResponseInfo(-1,"존재하지 않는 사용자 ID입니다.");
        }
        return new ResponseInfo(0, "Success", polio);
    }

    public ResponseInfo findPortFolioDetail(String type, String userId, String portFolioName) {
        try {
            PortFolio polio = portFolioMongoRespository.findBy_id(userId);
            if (null == polio) {
                return new ResponseInfo(-1, "[존재하지 않는 사용자 입니다..]");
            }
            PortFolioDetail portFolioDetail = polio.getPortFolioDetailMap().get(portFolioName);
            if (null == portFolioDetail) {
                return new ResponseInfo(-1, "[없는 포트폴리오명입니다.]");
            }

            if (type.equals("all")) {
                return new ResponseInfo(0, "Success", portFolioDetail);
            } else if (type.equals("domestic")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getDomesticStocks());
            } else if (type.equals("overseas")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getOverseasStocks());
            } else if (type.equals("isa")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getIsas());
            } else if (type.equals("personal")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getPersonalPensions());
            } else if (type.equals("retirement")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getRetirementPensions());
            } else if (type.equals("coin")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getCryptoCurrencys());
            } else if (type.equals("noncurrent")) {
                return new ResponseInfo(0, "Success", portFolioDetail.getNonCurrentAssets());
            } else if (type.equals("list")) {
                return new ResponseInfo(0, "Success", this.makePortFolioDetailList(portFolioDetail));
            } else {
                return new ResponseInfo(0, "Wrong type!!");
            }
        } catch (Exception e) {
            log.error("[findPortFolioDetail Error][{}]", e.toString());
            return new ResponseInfo(-1, "[없는 포트폴리오명입니다.]", e.toString());
        }
    }

    /**
     * 모든 주식들에 대해서 하나의 리스트로 반환
     */
    private Object makePortFolioDetailList(PortFolioDetail portFolioDetail) {
        List<Object> portFolioDetailList = new ArrayList<>();

        if (!portFolioDetail.getDomesticStocks().isEmpty()) {
            List<DomesticStock> domesticStockList = portFolioDetail.getDomesticStocks();
            for (int i = 0; i < domesticStockList.size(); i++) {
                portFolioDetailList.add(domesticStockList.get(i));
            }
        }

        if (!portFolioDetail.getOverseasStocks().isEmpty()) {
            List<OverseasStock> overseasStockList = portFolioDetail.getOverseasStocks();
            for (int i = 0; i < overseasStockList.size(); i++) {
                portFolioDetailList.add(overseasStockList.get(i));
            }
        }

        if (!portFolioDetail.getIsas().isEmpty()) {
            List<ISA> isaList = portFolioDetail.getIsas();
            for (int i = 0; i < isaList.size(); i++) {
                portFolioDetailList.add(isaList.get(i));
            }
        }

        if (!portFolioDetail.getPersonalPensions().isEmpty()) {
            List<PersonalPension> personalPensionList = portFolioDetail.getPersonalPensions();
            for (int i = 0; i < personalPensionList.size(); i++) {
                portFolioDetailList.add(personalPensionList.get(i));
            }
        }

        if (!portFolioDetail.getRetirementPensions().isEmpty()) {
            List<RetirementPension> retirementPensionList = portFolioDetail.getRetirementPensions();
            for (int i = 0; i < retirementPensionList.size(); i++) {
                portFolioDetailList.add(retirementPensionList.get(i));
            }
        }

        if (!portFolioDetail.getCryptoCurrencys().isEmpty()) {
            List<CryptoCurrency> cryptoCurrencyList = portFolioDetail.getCryptoCurrencys();
            for (int i = 0; i < cryptoCurrencyList.size(); i++) {
                portFolioDetailList.add(cryptoCurrencyList.get(i));
            }
        }

        if (!portFolioDetail.getNonCurrentAssets().isEmpty()) {
            List<NonCurrentAssets> nonCurrentAssetsList = portFolioDetail.getNonCurrentAssets();
            for (int i = 0; i < nonCurrentAssetsList.size(); i++) {
                portFolioDetailList.add(nonCurrentAssetsList.get(i));
            }
        }
        return portFolioDetailList;
    }


    /**
     * 포트폴리오 상세정보 삭제
     */
    public ResponseInfo deletePortFolioDetail(String userId, String portFolioName) {
        ResponseInfo responseInfo = new ResponseInfo();
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        if(null == portFolio){
            return new ResponseInfo(-1,"[존재하지 않는 사용자 입니다.]");
        }
        HashMap<String, PortFolioDetail> map = portFolio.getPortFolioDetailMap();
        if(map.size()==0){
            return new ResponseInfo(-1,"[삭제할 포트폴리오가 없습니다.]");
        }
        map.remove(portFolioName);
        portFolio.setPortFolioDetailMap(map);
        portFolioMongoRespository.save(portFolio);
        return new ResponseInfo(0,"["+portFolioName+" 포트폴리오 삭제에 성공했습니다.]");
    }

    /**
     * 국내 주식 추가
     */
    public ResponseInfo inputDomesticStock(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<DomesticStock> domesticStockList = new ArrayList<>();
        if (!portFolioDetail.getDomesticStocks().isEmpty()) {
            domesticStockList = portFolioDetail.getDomesticStocks();
        }

        DomesticStock domesticStock = new DomesticStock();
        domesticStock.setStockName(reqBodyFormat.getStockName());
        domesticStock.setStockCode(reqBodyFormat.getStockCode());
        domesticStock.setAveragePrice(reqBodyFormat.getAveragePrice());
        domesticStock.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        domesticStock.setQuantity(reqBodyFormat.getQuantity());
        domesticStock.setTodaysFluctuationRate(reqBodyFormat.getTodaysFluctuationRate());
        domesticStock.setYield(reqBodyFormat.getYield());
        domesticStock.setValuationLoss(reqBodyFormat.getValuationLoss());
        domesticStock.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        domesticStock.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        domesticStock.setType("domestic");
        if (method.equals("delete")) {
            int idx = domesticStockList.indexOf(domesticStock);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            domesticStockList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            /** 이미 존재한다면 기존에 있던것을 삭제하고 새로운 데이터를 집어넣음*/
            int idx = domesticStockList.indexOf(domesticStock);
            if (idx != 1) {
                domesticStockList.add(domesticStock);
            } else {
                domesticStockList.remove(idx);
                domesticStockList.add(domesticStock);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }
        portFolioDetail.setDomesticStocks(domesticStockList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", domesticStockList);
    }

    /**
     * 해외 주식 추가
     */
    public ResponseInfo inputOverseasStock(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<OverseasStock> overseasStockList = new ArrayList<>();
        if (!portFolioDetail.getOverseasStocks().isEmpty()) {
            overseasStockList = portFolioDetail.getOverseasStocks();
        }

        OverseasStock overseasStock = new OverseasStock();
        overseasStock.setStockName(reqBodyFormat.getStockName());
        overseasStock.setAveragePrice(reqBodyFormat.getAveragePrice());
        overseasStock.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        overseasStock.setQuantity(reqBodyFormat.getQuantity());
        overseasStock.setTodaysFluctuationRate(reqBodyFormat.getTodaysFluctuationRate());
        overseasStock.setYield(reqBodyFormat.getYield());
        overseasStock.setValuationLoss(reqBodyFormat.getValuationLoss());
        overseasStock.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        overseasStock.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        overseasStock.setType("overseas");

        if (method.equals("delete")) {
            int idx = overseasStockList.indexOf(overseasStock);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            overseasStockList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = overseasStockList.indexOf(overseasStock);
            if (idx != 1) {
                overseasStockList.add(overseasStock);
            } else {
                overseasStockList.remove(idx);
                overseasStockList.add(overseasStock);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }

        portFolioDetail.setOverseasStocks(overseasStockList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", overseasStockList);
    }

    /**
     * ISA 추가
     */
    public ResponseInfo inputISA(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<ISA> isaList = new ArrayList<>();
        if (!portFolioDetail.getIsas().isEmpty()) {
            isaList = portFolioDetail.getIsas();
        }

        ISA isa = new ISA();
        isa.setStockName(reqBodyFormat.getStockName());
        isa.setStockCode(reqBodyFormat.getStockCode());
        isa.setAveragePrice(reqBodyFormat.getAveragePrice());
        isa.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        isa.setQuantity(reqBodyFormat.getQuantity());
        isa.setTodaysFluctuationRate(reqBodyFormat.getTodaysFluctuationRate());
        isa.setYield(reqBodyFormat.getYield());
        isa.setValuationLoss(reqBodyFormat.getValuationLoss());
        isa.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        isa.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        isa.setType("isa");

        if (method.equals("delete")) {
            int idx = isaList.indexOf(isa);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            isaList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = isaList.indexOf(isa);
            if (idx != 1) {
                isaList.add(isa);
            } else {
                isaList.remove(idx);
                isaList.add(isa);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }

        portFolioDetail.setIsas(isaList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", isaList);
    }

    /**
     * 개인 연금 추가
     */
    public ResponseInfo inputPersonal(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<PersonalPension> personalPensionList = new ArrayList<>();
        if (!portFolioDetail.getPersonalPensions().isEmpty()) {
            personalPensionList = portFolioDetail.getPersonalPensions();
        }

        PersonalPension personalPension = new PersonalPension();
        personalPension.setStockName(reqBodyFormat.getStockName());
        personalPension.setStockCode(reqBodyFormat.getStockCode());
        personalPension.setAveragePrice(reqBodyFormat.getAveragePrice());
        personalPension.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        personalPension.setQuantity(reqBodyFormat.getQuantity());
        personalPension.setYield(reqBodyFormat.getYield());
        personalPension.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        personalPension.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        personalPension.setCurrentWeight(reqBodyFormat.getCurrentWeight());
        personalPension.setTargetWeight(reqBodyFormat.getTargetWeight());
        personalPension.setTargetQuantity(reqBodyFormat.getTargetQuantity());
        personalPension.setOperationFee(reqBodyFormat.getOperationFee());
        personalPension.setPayCalculation(reqBodyFormat.getPayCalculation());
        personalPension.setType("personalPension");

        if (method.equals("delete")) {
            int idx = personalPensionList.indexOf(personalPension);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            personalPensionList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = personalPensionList.indexOf(personalPension);
            if (idx != 1) {
                personalPensionList.add(personalPension);
            } else {
                personalPensionList.remove(idx);
                personalPensionList.add(personalPension);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }
        portFolioDetail.setPersonalPensions(personalPensionList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", personalPensionList);
    }

    /**
     * 퇴직 연금 추가
     */
    public ResponseInfo inputRetirement(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<RetirementPension> retirementPensionList = new ArrayList<>();
        if (!portFolioDetail.getRetirementPensions().isEmpty()) {
            retirementPensionList = portFolioDetail.getRetirementPensions();
        }

        RetirementPension retirementPension = new RetirementPension();
        retirementPension.setStockName(reqBodyFormat.getStockName());
        retirementPension.setStockCode(reqBodyFormat.getStockCode());
        retirementPension.setAveragePrice(reqBodyFormat.getAveragePrice());
        retirementPension.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        retirementPension.setQuantity(reqBodyFormat.getQuantity());
        retirementPension.setYield(reqBodyFormat.getYield());
        retirementPension.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        retirementPension.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        retirementPension.setCurrentWeight(reqBodyFormat.getCurrentWeight());
        retirementPension.setTargetWeight(reqBodyFormat.getTargetWeight());
        retirementPension.setTargetQuantity(reqBodyFormat.getTargetQuantity());
        retirementPension.setType("retirementPension");

        if (method.equals("delete")) {
            int idx = retirementPensionList.indexOf(retirementPension);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            retirementPensionList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = retirementPensionList.indexOf(retirementPension);
            if (idx != 1) {
                retirementPensionList.add(retirementPension);
            } else {
                retirementPensionList.remove(idx);
                retirementPensionList.add(retirementPension);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }
        portFolioDetail.setRetirementPensions(retirementPensionList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", retirementPensionList);
    }

    /**
     * 암호 화폐 추가
     * <p>
     * ex)
     * <p>
     * {
     * "stockName" : "NEO",
     * "exchange" : "upbit",
     * "averagePrice" : "75000",
     * "currentPrice" : "52870",
     * "quantity" : "20",
     * "yield" : "-29.51%",
     * "valuationLoss" : "-442600",
     * "purchaseAmount" : "1500000",
     * "balanceAssessment" : "1057400"
     * }
     */
    public ResponseInfo inputCoin(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<CryptoCurrency> cryptoCurrencyList = new ArrayList<>();
        if (!portFolioDetail.getCryptoCurrencys().isEmpty()) {
            cryptoCurrencyList = portFolioDetail.getCryptoCurrencys();
        }

        CryptoCurrency cryptoCurrency = new CryptoCurrency();
        cryptoCurrency.setStockName(reqBodyFormat.getStockName());
        cryptoCurrency.setExchange(reqBodyFormat.getExchange());
        cryptoCurrency.setAveragePrice(reqBodyFormat.getAveragePrice());
        cryptoCurrency.setCurrentPrice(reqBodyFormat.getCurrentPrice());
        cryptoCurrency.setQuantity(reqBodyFormat.getQuantity());
        cryptoCurrency.setYield(reqBodyFormat.getYield());
        cryptoCurrency.setValuationLoss(reqBodyFormat.getValuationLoss());
        cryptoCurrency.setPurchaseAmount(reqBodyFormat.getPurchaseAmount());
        cryptoCurrency.setBalanceAssessment(reqBodyFormat.getBalanceAssessment());
        cryptoCurrency.setType("cryptoCurrency");

        if (method.equals("delete")) {
            int idx = cryptoCurrencyList.indexOf(cryptoCurrency);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            cryptoCurrencyList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = cryptoCurrencyList.indexOf(cryptoCurrency);
            if (idx != 1) {
                cryptoCurrencyList.add(cryptoCurrency);
            } else {
                /**같은 경우에는 idx==1 */
                cryptoCurrencyList.remove(idx);
                cryptoCurrencyList.add(cryptoCurrency);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }
        portFolioDetail.setCryptoCurrencys(cryptoCurrencyList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", cryptoCurrencyList);
    }

    /**
     * 비유동자산 추가
     */
    public ResponseInfo inputNonCurrent(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<NonCurrentAssets> nonCurrentAssetsList = new ArrayList<>();
        if (!portFolioDetail.getNonCurrentAssets().isEmpty()) {
            nonCurrentAssetsList = portFolioDetail.getNonCurrentAssets();
        }

        NonCurrentAssets nonCurrentAssets = new NonCurrentAssets();
        nonCurrentAssets.setStockName(reqBodyFormat.getStockName());
        nonCurrentAssets.setMonthlyPayment(reqBodyFormat.getMonthlyPayment());
        nonCurrentAssets.setMonthlyPaymentMonth(reqBodyFormat.getMonthlyPaymentMonth());
        nonCurrentAssets.setTotalPayment(reqBodyFormat.getTotalPayment());
        nonCurrentAssets.setTotalAppraisalValue(reqBodyFormat.getTotalAppraisalValue());
        nonCurrentAssets.setType("nonCurrentAssets");

        if (method.equals("delete")) {
            int idx = nonCurrentAssetsList.indexOf(nonCurrentAssets);
            if (idx == -1) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            nonCurrentAssetsList.remove(idx);
        } else if (method.equals("update")) {
            /** update */
            int idx = nonCurrentAssetsList.indexOf(nonCurrentAssets);
            if (idx != 1) {
                nonCurrentAssetsList.add(nonCurrentAssets);
            } else {
                nonCurrentAssetsList.remove(idx);
                nonCurrentAssetsList.add(nonCurrentAssets);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }
        portFolioDetail.setNonCurrentAssets(nonCurrentAssetsList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", nonCurrentAssetsList);
    }

}
