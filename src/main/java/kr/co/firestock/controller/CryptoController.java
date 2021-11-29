package kr.co.firestock.controller;

import kr.co.firestock.service.CryptoService;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/crypto")
@CrossOrigin("*")
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    /**
     * 주기 : 1분에 한번씩
     */
    @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 10000)
    @RequestMapping("/input/marketdata")
    public ResponseInfo inputMarketData(){
        String Session = UUID.randomUUID().toString();
        log.info("[Start]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        ResponseInfo responseInfo = cryptoService.inputMarketData();
        log.info("[END]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        return responseInfo;
    }



    /**
     *
     * //시세 캔들
     * //PathVariable : 분 단위. 가능한 값 : 1, 3, 5, 15, 10, 30, 60, 240
     * RequestParam
     *
     * market: String -- 마켓 코드 (ex. KRW-BTC)
     *
     * to: String --마지막 캔들 시각 (exclusive). 포맷 : yyyy-MM-dd'T'HH:mm:ss'Z' or yyyy-MM-dd HH:mm:ss. 비워서 요청시 가장 최근 캔들
     *
     * count : int -- 캔들 갯수(최대 200개)
     *
     * */
    @RequestMapping(method = RequestMethod.GET, value="/minute/candle/{unit}")
    public ResponseInfo showCandle(
            @PathVariable("unit") int unit ,
            @RequestParam(value="market") String market,
            @RequestParam(value="to", required=false, defaultValue = "") String to,
            @RequestParam(value="count", required = false, defaultValue = "200") int count

    ){

        cryptoService.showMinuteCandle(unit, market, count);

        return new ResponseInfo();
    }

}
