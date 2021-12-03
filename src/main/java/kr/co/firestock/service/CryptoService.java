package kr.co.firestock.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kr.co.firestock.repository.CryptoCurrencyRepository;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.CryptoCurrencyVO;
import kr.co.firestock.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Thread.sleep;

@Service
@Slf4j
public class CryptoService {

    @Autowired
    Gson gson;


    @Value("${upbit.address.market}")
    String upbitUrl;

    @Value("${upbit.address.minuteCandle}")
    String candleUrl;

    @Value("${binance.address.market}")
    String binanceUrl;

    @Resource(name="cryptoCurrencyRepository")
    CryptoCurrencyRepository cryptoCurrencyRepository;

    public ResponseInfo inputUpBitCoinData() {
        ResponseInfo responseInfo = new ResponseInfo();
        RestTemplate restTemplate = new RestTemplate();
        List<CryptoCurrencyVO> currencyVOList = new ArrayList<>();
        try {
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            HttpHeaders headers = new HttpHeaders();

            headers.add("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(upbitUrl, HttpMethod.GET, entity, String.class);
            JsonArray jsonObj = gson.fromJson(response.getBody(), JsonArray.class);
            int len = jsonObj.size();

            /**종 267개 */
            for (int i = 0; i < len; i++) {
                CryptoCurrencyVO  cryptoCurrencyVO = new CryptoCurrencyVO();
                String market = jsonObj.get(i).getAsJsonObject().get("market").getAsString();
                cryptoCurrencyVO.setMarket(market);
                cryptoCurrencyVO.setKorean_name(jsonObj.get(i).getAsJsonObject().get("korean_name").getAsString());
                cryptoCurrencyVO.setEnglish_name(jsonObj.get(i).getAsJsonObject().get("english_name").getAsString());
                JsonArray jsonArray = this.showMinuteCandle(5,market,1);
                cryptoCurrencyVO.setCandle_date_time_utc(new StringUtil().parseDateType(jsonArray.get(0).getAsJsonObject().get("candle_date_time_utc").getAsString()));
                cryptoCurrencyVO.setCandle_date_time_kst(new StringUtil().parseDateType(jsonArray.get(0).getAsJsonObject().get("candle_date_time_kst").getAsString()));
                cryptoCurrencyVO.setOpening_price(jsonArray.get(0).getAsJsonObject().get("opening_price").getAsString());
                cryptoCurrencyVO.setHigh_price(jsonArray.get(0).getAsJsonObject().get("high_price").getAsString());
                cryptoCurrencyVO.setLow_price(jsonArray.get(0).getAsJsonObject().get("low_price").getAsString());
                cryptoCurrencyVO.setTrade_price(jsonArray.get(0).getAsJsonObject().get("trade_price").getAsString());
                cryptoCurrencyVO.setTimestamp(jsonArray.get(0).getAsJsonObject().get("timestamp").getAsString());
                cryptoCurrencyVO.setCandle_acc_trade_price(jsonArray.get(0).getAsJsonObject().get("candle_acc_trade_price").getAsString());
                cryptoCurrencyVO.setCandle_acc_trade_volume(jsonArray.get(0).getAsJsonObject().get("candle_acc_trade_volume").getAsString());
                cryptoCurrencyVO.setUnit(jsonArray.get(0).getAsJsonObject().get("unit").getAsString());
                cryptoCurrencyVO.setUpdt(new StringUtil().makeTodayDate());
                currencyVOList.add(cryptoCurrencyVO);
                sleep(90); /** Too Many Request 피하기 위해서 사용*/
            }
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(currencyVOList);

            cryptoCurrencyRepository.saveAll(currencyVOList);
        } catch (Exception e) {
            log.error("[CryptoService inputUpBitCoinData Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }
        responseInfo.setReturnCode(0);
        responseInfo.setReturnMsg(new StringUtil().makeTodayDate()+ " : Success");
        return responseInfo;
    }

    public JsonArray showMinuteCandle(int unit, String market, int count) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JsonArray jsonObj = null;
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            HttpHeaders headers = new HttpHeaders();

            headers.add("Accept", "application/json");

            final HttpEntity<String> entity = new HttpEntity(headers);

//            candleurl += "/" + unit + "?market=" + market + "&to=" + to + "&count=" + count;
            String url = candleUrl + "/" + unit + "?market=" + market + "&count=" + count;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            jsonObj = gson.fromJson(response.getBody(), JsonArray.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObj;
    }

    /**1798 */
    public ResponseInfo inputBinanceCoinData() {
        ResponseInfo responseInfo = new ResponseInfo();
        RestTemplate restTemplate = new RestTemplate();
        List<CryptoCurrencyVO> currencyVOList = new ArrayList<>();
        try{
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            HttpHeaders headers = new HttpHeaders();

            headers.add("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity(headers);

            ResponseEntity<String> response = restTemplate.exchange(binanceUrl, HttpMethod.GET, entity, String.class);
            JsonArray jsonObj = gson.fromJson(response.getBody(), JsonArray.class);
            int len = jsonObj.size();
            for(int i=0; i<len; i++){
                CryptoCurrencyVO  cryptoCurrencyVO = new CryptoCurrencyVO();
                cryptoCurrencyVO.setMarket(jsonObj.get(i).getAsJsonObject().get("symbol").getAsString());
                cryptoCurrencyVO.setTrade_price(jsonObj.get(i).getAsJsonObject().get("price").getAsString());
                cryptoCurrencyVO.setUpdt(new StringUtil().makeTodayDate());
                currencyVOList.add(cryptoCurrencyVO);
            }

        }catch(Exception e){
            log.error("[CryptoService inputBinanceCoinData Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }
        responseInfo.setData(currencyVOList);
        return responseInfo;
    }

    public ResponseInfo findCryptoCurrencyInfo(String cryptoname) {
        ResponseInfo responseInfo = new ResponseInfo();
        CryptoCurrencyVO cryptoCurrencyVO = new CryptoCurrencyVO();
        try{
            cryptoCurrencyVO = cryptoCurrencyRepository.findByMarket(cryptoname);
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(cryptoCurrencyVO);
        }catch (Exception e){
            log.error("[CryptoService findByMarket Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }

        return responseInfo;
    }

    public ResponseInfo findCryptoCurrencyInfos(String cryptonames) {
        ResponseInfo responseInfo = new ResponseInfo();
        String [] names = cryptonames.split(",");
        List<CryptoCurrencyVO> currencyVOList = new ArrayList<>();
        try{
            currencyVOList = cryptoCurrencyRepository.findMarketIn(names);
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(currencyVOList);
        }catch (Exception e){
            log.error("[CryptoService findMarketIn Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }
        return responseInfo;
    }

    public ResponseInfo findAllCryptoCurrencyInfo() {
        ResponseInfo responseInfo = new ResponseInfo();
        List<CryptoCurrencyVO> currencyVOList = new ArrayList<>();
        try{
            currencyVOList = cryptoCurrencyRepository.findAll();
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(currencyVOList);
        }catch (Exception e){
            log.error("[CryptoService findAll Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }
        return responseInfo;
    }
}
