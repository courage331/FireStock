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
//@CrossOrigin("*")
public class CryptoController {

    @Autowired
    CryptoService cryptoService;

    /**
     * upbit에 있는 모든 암호화폐의 정보를 db에 upsert한다. (최초에는 insert, 그후에는 update)
     *
     * Scheduled 주석을 풀게되면 서버 시작후 initialDelay ms 이후, fixedDelay ms 가 지날때마다 자동적으로 아래 method를 실행한다.
     * 주기(fixedDelay) 5 * 60 * 1000 -> 5분
     *
     * ex) localhost:8080/api/v1/crypto/input/upbitdata
     */

//    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 10000)
    @RequestMapping("/input/upbitdata")
    public ResponseInfo inputUpBitCoinData(){
        String Session = UUID.randomUUID().toString();
        log.info("[Start]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        ResponseInfo responseInfo = cryptoService.inputUpBitCoinData();
        log.info("[END]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        return responseInfo;
    }

    /**
     * 현재 호출용으로는 사용하지 않음
     * 나중을 위해 남겨둠 CryptoService의 showMinuteCandle에서 직접 호출진행중
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
//    @RequestMapping(method = RequestMethod.GET, value="/minute/candle/{unit}")
//    public ResponseInfo showCandle(
//            @PathVariable("unit") int unit ,
//            @RequestParam(value="market") String market,
//            @RequestParam(value="to", required=false, defaultValue = "") String to,
//            @RequestParam(value="count", required = false, defaultValue = "200") int count
//
//    ){
//
//        cryptoService.showMinuteCandle(unit, market, count);
//
//        return new ResponseInfo();
//    }


    /**
     * binance 있는 모든 암호화폐의 정보를 db에 upsert한다. (최초에는 insert, 그후에는 update)
     *
     * Scheduled 주석을 풀게되면 서버 시작후 initialDelay ms 이후, fixedDelay ms 가 지날때마다 자동적으로 아래 method를 실행한다.
     * 주기(fixedDelay) 5 * 60 * 1000 -> 5분
     *
     * ex) localhost:8080/api/v1/crypto/input/marketdata
     */
    //    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 15000)
    @RequestMapping("/input/binancedata")
    public ResponseInfo inputBinanceCoinData(){
        String Session = UUID.randomUUID().toString();
        log.info("[Start]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        ResponseInfo responseInfo = cryptoService.inputBinanceCoinData();
        log.info("[END]["+Session+"]["+ new StringUtil().makeTodayDate()+"]");
        return responseInfo;
    }

    /**
     * DB에 있는 하나의 암호화폐의 정보를 호출한다.
     * ex) localhost:8080/api/v1/crypto/find/info/BTC-ARK
     *
     * */
    @RequestMapping("/find/info/{cryptoname}")
    public ResponseInfo findCryptoCurrencyInfo(
            @PathVariable("cryptoname") String cryptoname){
        ResponseInfo responseInfo = cryptoService.findCryptoCurrencyInfo(cryptoname);
        return responseInfo;
    }

    /**
     * DB에 있는 여러개의 암호화폐의 정보를 호출한다.
     * ex) localhost:8080/api/v1/crypto/find/infos?cryptonames=BTC-ARK,BTC-ATOM
     * */
    @RequestMapping("/find/infos")
    public ResponseInfo findCryptoCurrencyInfos(
            @RequestParam(value="cryptonames") String cryptonames){
        ResponseInfo responseInfo = cryptoService.findCryptoCurrencyInfos(cryptonames);
        return responseInfo;
    }

    /**
     * DB에 있는 모든 암호화폐의 정보를 호출한다.
     * ex) localhost:8080/api/v1/crypto/find/allinfo
     * */
    @RequestMapping("/find/allinfo")
    public ResponseInfo findAllCryptoCurrencyInfo(){
        ResponseInfo responseInfo = cryptoService.findAllCryptoCurrencyInfo();
        return responseInfo;
    }
}
