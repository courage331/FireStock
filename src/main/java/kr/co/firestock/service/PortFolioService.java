package kr.co.firestock.service;

import kr.co.firestock.repository.HistoryMongoRepository;
import kr.co.firestock.repository.PortFolioMongoRespository;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class PortFolioService {

    @Autowired
    PortFolioMongoRespository portFolioMongoRespository;

    @Autowired
    HistoryMongoRepository historyMongoRepository;

    public ResponseInfo createFolio(String userId) {
        Boolean isExist = portFolioMongoRespository.existsBy_id(userId);
        if (!isExist) {
            portFolioMongoRespository.save(new PortFolio(userId, new HashMap<>()));
            return new ResponseInfo(0, "[포트폴리오 생성 성공!]");
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
        map.put(portFolioName, new PortFolioDetail(portFolioType, 0, 0, date, date, new ArrayList<>()));
        portFolio.setPortFolioDetailMap(map);
        portFolioMongoRespository.save(portFolio);
        log.info("[{} 포트폴리오 추가]", portFolioName);
        return new ResponseInfo(0, "[" + portFolioName + " 포트폴리오 추가]");
    }


    public ResponseInfo findPortFolio(String userId) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        if (null == polio) {
            return new ResponseInfo(-1, "존재하지 않는 사용자 ID입니다.");
        }
        return new ResponseInfo(1, "Success", polio);
    }

    public ResponseInfo findPortFolioDetail(String type, String userId, String portFolioName) {
        List<PortFolioData> returnData = new ArrayList<>();
        try {
            PortFolio polio = portFolioMongoRespository.findBy_id(userId);
            if (null == polio) {
                return new ResponseInfo(-1, "[존재하지 않는 사용자 입니다]");
            }
            PortFolioDetail portFolioDetail = polio.getPortFolioDetailMap().get(portFolioName);
            List<PortFolioData> portFolioDetailList = portFolioDetail.getPortFolioDataList();

            if (type.equals("all")) {
                return new ResponseInfo(1, "[PortFolioDeatil 조회 성공]", portFolioDetail);
            } else if (type.equals("list")) {
                returnData = portFolioDetailList;
            } else {
                returnData = this.makePortFolioDetailList(portFolioDetailList, type);
            }

            return new ResponseInfo(1, "[PortFolioDeatil 조회 성공]", returnData);
        } catch (Exception e) {
            log.error("[findPortFolioDetail Error][{}]", e.toString());
            return new ResponseInfo(-1, "[없는 포트폴리오명입니다.]", e.toString());
        }
    }

    /**
     * 모든 주식들에 대해서 하나의 리스트로 반환
     */
    private List<PortFolioData> makePortFolioDetailList(List<PortFolioData> portFolioDetailList, String type) {
        List<PortFolioData> returnData = new ArrayList<>();
        for (int i = 0; i < portFolioDetailList.size(); i++) {
            if (portFolioDetailList.get(i).getStockType().equals(type)) {
                returnData.add(portFolioDetailList.get(i));
            }
        }
        return returnData;
    }


    /**
     * 포트폴리오 상세정보 삭제
     */
    public ResponseInfo deletePortFolioDetail(String userId, String portFolioName) {
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        if (null == portFolio) {
            return new ResponseInfo(-1, "[존재하지 않는 사용자 입니다.]");
        }
        HashMap<String, PortFolioDetail> map = portFolio.getPortFolioDetailMap();
        if (map.size() == 0) {
            return new ResponseInfo(-1, "[삭제할 포트폴리오가 없습니다.]");
        }
        map.remove(portFolioName);
        portFolio.setPortFolioDetailMap(map);
        portFolioMongoRespository.save(portFolio);
        return new ResponseInfo(0, "[" + portFolioName + " 포트폴리오 삭제에 성공했습니다.]");
    }

    /**
     * 주식 추가
     * 평단 계산
     */
    public ResponseInfo inputPortFolioData(ReqBodyFormat reqBodyFormat, String method, String userId, String portFolioName) {
        PortFolio polio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = polio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);

        List<PortFolioData> portFolioDataList = new ArrayList<>();
        if (!portFolioDetail.getPortFolioDataList().isEmpty()) {
            portFolioDataList = portFolioDetail.getPortFolioDataList();
        }

        PortFolioData portFolioData = PortFolioData.builder().
                stockInfo(reqBodyFormat.getStockInfo()).
                stockName(reqBodyFormat.getStockName()).
                stockType(reqBodyFormat.getStockType()).
                stockPrice(reqBodyFormat.getStockPrice()).
                stockAmount(reqBodyFormat.getStockAmount()).build();

        boolean removechk = true;
        /** 현재 수익 = (주식 가격 * 수량)  */
        String currentMoney = String.valueOf(Double.parseDouble(reqBodyFormat.getStockAmount()) * Double.parseDouble(reqBodyFormat.getStockPrice()));
        if (method.equals("delete") || method.equals("sell")) {
            PortFolioData oldData = null;
            double amt = 0;
            /** 이미 존재한다면 기존에 있던것을 삭제하고 새로운 데이터를 집어넣음*/
            for (int i = 0; i < portFolioDataList.size(); i++) {
                if (portFolioDataList.get(i).getStockName().equals(portFolioData.getStockName())) {
                    oldData = portFolioDataList.get(i);
                    if (Double.parseDouble(portFolioData.getStockAmount()) >= Double.parseDouble(reqBodyFormat.getStockAmount())) {
                        amt = (Double.parseDouble(portFolioDataList.get(i).getStockAmount()) - Double.parseDouble(reqBodyFormat.getStockAmount()));
                    }
                    portFolioDataList.remove(i);
                    removechk = false;
                    break;
                }
            }
            oldData.setStockAmount(String.valueOf(amt));
            if (removechk) {
                return new ResponseInfo(-1, "Delete Fail");
            }
            if(amt>0){
                portFolioDataList.add(oldData);
            }

            if (method.equals("sell")) {
                double newCurretMoney = 0;
                /**판매금액 만큼 더해 주기*/
                if(portFolioData.getStockType().equals("overseas")){
                     newCurretMoney = portFolioDetail.getPortFolioDollarMoney() + Double.parseDouble(currentMoney);
                }else{
                     newCurretMoney = portFolioDetail.getPortFolioWonMoney() + Double.parseDouble(currentMoney);
                }

                portFolioDetail.setPortFolioWonMoney(newCurretMoney);
                History history = History.builder().
                        userId(userId).portFolioName(portFolioName).type(method).
                        money(currentMoney).
                        regdt(new StringUtil().makeTodayDate()).portFolioData(portFolioData).
                        build();
                historyMongoRepository.save(history);
            }

        } else if (method.equals("update") || method.equals("buy")) {
            /** 이미 존재한다면 기존에 있던것을 삭제하고 새로운 데이터를 집어넣음*/
            double avg = 0;
            double amt = 0;
            boolean avgchk = false;
            for (int i = 0; i < portFolioDataList.size(); i++) {
                if (portFolioDataList.get(i).getStockName().equals(portFolioData.getStockName())) {
                    amt = (Double.parseDouble(reqBodyFormat.getStockAmount()) + Double.parseDouble(portFolioDataList.get(i).getStockAmount()));
                    /**평단 계산 */
                    avg = (Double.parseDouble(reqBodyFormat.getStockPrice()) * Double.parseDouble(reqBodyFormat.getStockAmount()) +
                            (Double.parseDouble(portFolioDataList.get(i).getStockPrice()) * (Double.parseDouble(portFolioDataList.get(i).getStockAmount()))))
                            / (amt);
                    avgchk = true;
                    portFolioDataList.remove(i);
                    break;
                }
            }
            if (avgchk) {
                portFolioData.setStockPrice(String.valueOf(Math.round(avg * 100) / 100.0));
                portFolioData.setStockAmount(String.valueOf(Math.round(amt * 100) / 100.0));
            }
            portFolioDataList.add(portFolioData);

            if (method.equals("buy")) {
                double newCurretMoney = 0;
                /**판매금액 만큼 더해 주기*/
                if(portFolioData.getStockType().equals("overseas")){
                    newCurretMoney = portFolioDetail.getPortFolioDollarMoney() + Double.parseDouble(currentMoney);
                }else{
                    newCurretMoney = portFolioDetail.getPortFolioWonMoney() + Double.parseDouble(currentMoney);
                }
                if (newCurretMoney < 0) {
                    return new ResponseInfo(-1, "Fail", "cant buy");
                }
                portFolioDetail.setPortFolioWonMoney(newCurretMoney);
                double doubleCurrentMoney = Double.parseDouble(currentMoney);
                History history = History.builder().
                        userId(userId).portFolioName(portFolioName).type(method).
                        money(String.valueOf(Math.round(doubleCurrentMoney * 100) / 100.0)).
                        regdt(new StringUtil().makeTodayDate()).portFolioData(portFolioData).
                        build();
                historyMongoRepository.save(history);
            }
        } else {
            return new ResponseInfo(-1, "Wrong method");
        }

        portFolioDetail.setPortFolioDataList(portFolioDataList);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));
        return new ResponseInfo(0, method + " 성공", portFolioDataList);
    }

    public ResponseInfo inputWon(String method, String userId, String portFolioName, int money, String moneyType) {

        ResponseInfo responseInfo = new ResponseInfo();
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = portFolio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);
        double wonMoney = portFolioDetail.getPortFolioWonMoney();
        double dollarMoney = portFolioDetail.getPortFolioDollarMoney();

        if (moneyType.equals("won")) {
            if (method.equals("input")) {
                wonMoney += money;
            } else if (method.equals("output")) {
                if (wonMoney - money < 0) {
                    return new ResponseInfo(-1, "Fail");
                }
                wonMoney -= money;
            }
        } else if (moneyType.equals("dollar")) {
            if (method.equals("input")) {
                dollarMoney += money;
            } else if (method.equals("output")) {
                if (dollarMoney - money < 0) {
                    return new ResponseInfo(-1, "Fail");
                }
                dollarMoney -= money;
            }
        } else {
            return new ResponseInfo(-1, "Fail", "moneyType error");
        }
        portFolioDetail.setPortFolioWonMoney(wonMoney);
        portFolioDetail.setPortFolioDollarMoney(Math.round(dollarMoney * 100) / 100.0);
        portFolioDetail.setUpDt(new StringUtil().makeTodayDate());
        map.put(portFolioName, portFolioDetail);
        portFolioMongoRespository.save(new PortFolio(userId, map));

        responseInfo.setReturnCode(0);
        responseInfo.setReturnMsg("Success");
        responseInfo.setData(moneyType + "::" + method + "::" + money + "::성공");
        return responseInfo;
    }

    public ResponseInfo findAllPortFolio(String userId) {
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        if (null == portFolio) {
            return new ResponseInfo(-1, "[존재하지 않는 사용자 입니다.]");
        }
        int portFolioWonSum =0;
        int portFoliodollarSum =0;
        List<PortFolioData> portFolioDetailList = new ArrayList<>();
        HashMap<String, PortFolioDetail> map  = portFolio.getPortFolioDetailMap();
        Iterator<String> iterator = map.keySet().iterator();

        while(iterator.hasNext()){
            String key = iterator.next(); // 키 얻기
            PortFolioDetail portFolioDetail = portFolio.getPortFolioDetailMap().get(key);
            portFolioWonSum += portFolioDetail.getPortFolioWonMoney();
            portFoliodollarSum += portFolioDetail.getPortFolioDollarMoney();
            for(int i=0; i<portFolioDetail.getPortFolioDataList().size();i++){
                portFolioDetailList.add(portFolioDetail.getPortFolioDataList().get(i));
            }
        }

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("portFolioWonMoney",portFolioWonSum);
        dataMap.put("portFolioDollarMoney",portFoliodollarSum);
        dataMap.put("portFolioDataList",portFolioDetailList);

        return new ResponseInfo(1,"Success",dataMap);
    }
}
