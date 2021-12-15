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
        map.put(portFolioName, new PortFolioDetail(portFolioType, 0, 0, date, date, new ArrayList<>()));
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
        List<PortFolioData> returnData = new ArrayList<>();
        try {
            PortFolio polio = portFolioMongoRespository.findBy_id(userId);
            if (null == polio) {
                return new ResponseInfo(-1, "[존재하지 않는 사용자 입니다..]");
            }
            PortFolioDetail portFolioDetail = polio.getPortFolioDetailMap().get(portFolioName);
            List<PortFolioData> portFolioDetailList = portFolioDetail.getPortFolioDataList();
            if (null == portFolioDetail) {
                return new ResponseInfo(-1, "[없는 포트폴리오명입니다.]");
            }

            if(type.equals("all")){
                returnData = portFolioDetailList;
            }else{
                returnData = this.makePortFolioDetailList(portFolioDetailList,type);
            }

            return new ResponseInfo(0, "Success", returnData);
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
        for(int i=0; i<portFolioDetailList.size(); i++){
            if(portFolioDetailList.get(i).getStockType().equals(type)){
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
                currentPrice(reqBodyFormat.getCurrentPrice()).
                purchasePrice(reqBodyFormat.getPurchasePrice()).
                totSum(reqBodyFormat.getTotSum()).
                totProfit(reqBodyFormat.getTotProfit()).
                totAmount(reqBodyFormat.getTotAmount())
                .build();

        boolean removechk = true;
        String currentMoney =  "0";

        /** 이미 존재한다면 기존에 있던것을 삭제하고 새로운 데이터를 집어넣음*/
        for (int i = 0; i < portFolioDataList.size(); i++) {
            if(portFolioDataList.get(i).getStockName().equals(portFolioData.getStockName())){
                currentMoney = portFolioDataList.get(i).getTotSum();
                portFolioDataList.remove(i);
                removechk = false;
                break;
            }
        }

        if (method.equals("delete") || method.equals("sell")) {
            if (removechk) {
                return new ResponseInfo(-1, "Delete Fail");
            }

            if(method.equals("sell")){
                int newCurretMoney =  portFolioDetail.getPortFolioWonMoney() + Integer.parseInt(currentMoney);
                portFolioDetail.setPortFolioWonMoney(newCurretMoney);
            }

        } else if (method.equals("update") || method.equals("buy")) {
            portFolioDataList.add(portFolioData);

            if(method.equals("buy")){
                int newCurretMoney =  portFolioDetail.getPortFolioWonMoney() - Integer.parseInt(currentMoney);
                portFolioDetail.setPortFolioWonMoney(newCurretMoney);
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

    public ResponseInfo inputWon(String method, String userId, String portFolioName, int money) {

        ResponseInfo responseInfo = new ResponseInfo();
        PortFolio portFolio = portFolioMongoRespository.findBy_id(userId);
        HashMap<String, PortFolioDetail> map = portFolio.getPortFolioDetailMap();
        PortFolioDetail portFolioDetail = map.get(portFolioName);
        int won = portFolioDetail.getPortFolioWonMoney();

        if(method.equals("input")){
            won += money;
        }else if(method.equals("output")){
            if(won-money<0){
                return new ResponseInfo(-1,"Fail");
            }
            won -= money;
        }
        responseInfo.setReturnCode(0);
        responseInfo.setReturnMsg("Success");
        responseInfo.setData(won);
        return responseInfo;
    }
}
