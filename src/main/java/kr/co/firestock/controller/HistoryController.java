package kr.co.firestock.controller;

import kr.co.firestock.service.HistoryService;
import kr.co.firestock.vo.History;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;


    /**
     * 거래내역에 데이터를 추가한다...
     *
     * ex) localhost:8080/api/v1/history/input/data
     *
     * reqeustBody
     {
         "userId" : "test",
         "portFolioName" : "test2",
         "type" : "sell",
         "money" : "0",
         "regdt" : "0",
         "portFolioData" : {
             "stockInfo" : "주식코드32",
             "stockName" : "주식명32",
             "stockType" : "overseas",
             "purchasePrice" : "15000",
             "currentPrice" : "300",
             "totSum" : "15000",
             "totProfit" : "-4.44%",
             "totAmount" : "-179700"
        }
     }

     *
     *
     * */
    @PostMapping("/input/data")
    public ResponseInfo createHistory(HttpServletRequest request, @RequestBody History history){
        log.info("[Start Create History info][{}][{}]",request.getRequestURL(), history.toString());
        ResponseInfo responseInfo = historyService.createHistory(history);
        log.info("[End History info][{}][{}]",request.getRequestURL(), history.toString());
        return responseInfo;
    }

    /**
     * userId는 필수
     * portFolioName, type 은 선택 type = buy(구입), sell(매매) // 추가(update), 삭제(delete)에 대해서는 히스토리에 적재 x
     *
     *
     * ex) localhost:8080/api/v1/history/find/data?userId=test3
     *
     * */
    @GetMapping("/find/data")
    public ResponseInfo findUserHistory(
            HttpServletRequest request,
            @RequestParam(value="userId", required = true, defaultValue = "")String userId,
            @RequestParam(value="portFolioName", required = true, defaultValue = "")String portFolioName,
            @RequestParam(value="type", required = true, defaultValue = "")String type
    ){
        log.info("[Start Find User History info][{}]",request.getRequestURL());
        ResponseInfo responseInfo = historyService.findUserHistory(userId,portFolioName,type);
        log.info("[End Find User History info][{}]",request.getRequestURL());

        return responseInfo;
    }

}
