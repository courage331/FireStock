package kr.co.firestock.service;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.Stocks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PolygonService {

    RestTemplate restTemplate = new RestTemplate();

    String api_key = "n28dy48IHcfuA3VZm_IfdoeUpqgqRCyZ";

    Gson gson = new Gson();

    public ResponseInfo getTicker() {

        ResponseInfo responseInfo = new ResponseInfo();

        HttpHeaders headers = new HttpHeaders();
        String accessToken = "Bearer " +api_key;
        headers.add("Authorization", accessToken);
        HttpEntity<String> entity = new HttpEntity(headers);
        String url = "https://api.polygon.io/v3/reference/tickers?market=stocks&active=true&sort=name&order=desc&limit=3";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String result = response.getBody();
        JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
        log.info(jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject().toString());
        JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();
        List<Stocks> stocksList = new ArrayList();
        for(int i=0; i< jsonArray.size(); i++){
            Stocks stocks = new Stocks();
            stocks.setTicker(jsonArray.get(i).getAsJsonObject().get("ticker").getAsString());
            stocks.setName(jsonArray.get(i).getAsJsonObject().get("name").getAsString());
            stocks.setMarket(jsonArray.get(i).getAsJsonObject().get("market").getAsString());
            stocks.setLocale(jsonArray.get(i).getAsJsonObject().get("locale").getAsString());
            stocks.setPrimaryExchange(jsonArray.get(i).getAsJsonObject().get("primary_exchange").getAsString());
            stocks.setActive(jsonArray.get(i).getAsJsonObject().get("active").getAsString());
            stocks.setCurrencyName(jsonArray.get(i).getAsJsonObject().get("currency_name").getAsString());
            stocks.setCik(jsonArray.get(i).getAsJsonObject().get("cik").getAsString());
            stocks.setCompostieFigi(jsonArray.get(i).getAsJsonObject().get("composite_figi").getAsString());
            stocks.setShareClassFigi(jsonArray.get(i).getAsJsonObject().get("share_class_figi").getAsString());
            stocks.setLastUpdateUtc(jsonArray.get(i).getAsJsonObject().get("last_updated_utc").getAsString());
            stocksList.add(stocks);
        }
        responseInfo.setData(result+"");
        return responseInfo;
    }
}
