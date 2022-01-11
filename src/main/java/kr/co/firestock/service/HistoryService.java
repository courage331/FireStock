package kr.co.firestock.service;

import kr.co.firestock.repository.HistoryMongoRepository;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.History;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class HistoryService {

    @Autowired
    HistoryMongoRepository historyMongoRepository;

    public ResponseInfo createHistory(History history) {
        ResponseInfo responseInfo = new ResponseInfo();
        history.setRegdt(new StringUtil().makeTodayDate());
        historyMongoRepository.save(history);

        return responseInfo;
    }

    public ResponseInfo findUserHistory(String userId, String portFolioName, String type) {
        ResponseInfo responseInfo = new ResponseInfo();
        List<History>historyList = new ArrayList<>();

        try{
            if(portFolioName.equals("") && type.equals("")){
                historyList= historyMongoRepository.findByUserId(userId);
            }else if(!portFolioName.equals("") && type.equals("")){
                historyList= historyMongoRepository.findByUserIdAndPortFolioName(userId,portFolioName);
            }else if(portFolioName.equals("") && !type.equals("")){
                historyList= historyMongoRepository.findByUserIdAndType(userId,type);
            }else{
                historyList=historyMongoRepository.findByUserIdAndTypeAndPortFolioName(userId,type,portFolioName);
            }
            responseInfo.setReturnCode(1);
            responseInfo.setReturnMsg("[거래 내역 조회 성공]");
            responseInfo.setData(historyList);
        }catch(Exception e){
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("[거래 내역 조회 실패]");
            responseInfo.setData(e.toString());
        }

        return responseInfo;
    }
}
