package kr.co.firestock.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import kr.co.firestock.repository.StocksMongoRepository;
import kr.co.firestock.util.StringUtil;
import kr.co.firestock.vo.CryptoCurrencyVO;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.Stocks;
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

    @Autowired
    StocksMongoRepository stocksMongoRepository;

    public ResponseInfo inputUpBitCoinData() {
        ResponseInfo responseInfo = new ResponseInfo();
        RestTemplate restTemplate = new RestTemplate();
        List<Stocks> stocksList = new ArrayList<>();
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
                Stocks stocks = new Stocks();
                String market = jsonObj.get(i).getAsJsonObject().get("market").getAsString();
                if(!market.contains("KRW")){
                    continue;
                }
                stocks.set_id(market);
                stocks.setStockInfo(market);
                stocks.setStockType("coin");
                stocks.setStockName(jsonObj.get(i).getAsJsonObject().get("korean_name").getAsString());
                JsonArray jsonArray = this.showMinuteCandle(5,market,1);
                stocks.setCurrentWonPrice(jsonArray.get(0).getAsJsonObject().get("trade_price").getAsString());
//                stocks.setCurrentDollarPrice();
                stocks.setUpdt(new StringUtil().makeTodayDate());
                stocksList.add(stocks);
                sleep(90); /** Too Many Request 피하기 위해서 사용*/
            }
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(stocksList);

            stocksMongoRepository.saveAll(stocksList);
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
            responseInfo.setReturnMsg("[가상화폐 DB insert 실패]");
        }
        responseInfo.setData(currencyVOList);
        return responseInfo;
    }

    public ResponseInfo findCryptoCurrencyInfo(String stockInfo) {
        ResponseInfo responseInfo = new ResponseInfo();
        Stocks stocks = new Stocks();
        try{
            stocks = stocksMongoRepository.findByStockInfo(stockInfo);
            responseInfo.setReturnCode(1);
            responseInfo.setReturnMsg("[가상화폐 단건 조회 성공]");
            responseInfo.setData(stocks);
        }catch (Exception e){
            log.error("[CryptoService findByMarket Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("[가상화폐 단건 조회 실패]");
        }

        return responseInfo;
    }

    public ResponseInfo findCryptoCurrencyInfos(String markets) {
        ResponseInfo responseInfo = new ResponseInfo();
        String [] names = markets.split(",");
        List<Stocks> stocksList = new ArrayList<>();
        try{
            for(int i=0; i<names.length; i++){
                stocksList.add(stocksMongoRepository.findByStockInfo(names[i]));
            }
            responseInfo.setReturnCode(0);
            responseInfo.setReturnMsg("Success");
            responseInfo.setData(stocksList);
        }catch (Exception e){
            log.error("[CryptoService findMarketIn Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("Fail");
        }
        return responseInfo;
    }

    public ResponseInfo findAllCryptoCurrencyInfo() {
        ResponseInfo responseInfo = new ResponseInfo();
        List<Stocks> stocksList = new ArrayList<>();
        try{
            stocksList = stocksMongoRepository.findAll();
            responseInfo.setReturnCode(1);
            responseInfo.setReturnMsg("[모든 가상 화폐 조회 성공]");
            responseInfo.setData(stocksList);
        }catch (Exception e){
            log.error("[CryptoService findAll Error]");
            responseInfo.setReturnCode(-1);
            responseInfo.setReturnMsg("[모든 가상 화폐 조회 실패]");
        }
        return responseInfo;
    }
}
