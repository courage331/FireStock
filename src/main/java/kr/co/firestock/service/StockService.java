package kr.co.firestock.service;

import kr.co.firestock.repository.StocksMongoRepository;
import kr.co.firestock.repository.ScheduledStockRepository;
import kr.co.firestock.vo.ResponseInfo;
import kr.co.firestock.vo.ScheduledStock;
import kr.co.firestock.vo.Stocks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class StockService {

    @Autowired
    ScheduledStockRepository scheduledStockRepository;

    @Autowired
    StocksMongoRepository stocksMongoRepository;

    public ResponseInfo inputStocks(String stocks) {
        ResponseInfo responseInfo = new ResponseInfo();
        String[] stockDatas = stocks.split(",");

        List<ScheduledStock> stocksList = new ArrayList<>();

        for (int i = 0; i < stockDatas.length; i++) {
            stocksList.add(new ScheduledStock(stockDatas[i]));
        }

        scheduledStockRepository.saveAll(stocksList);

        return responseInfo;
    }

    public ResponseInfo findStocks() {

        List<ScheduledStock> stocksList = scheduledStockRepository.findAll();

        return new ResponseInfo(1,"선택된 주식 정보 리스트 조회 성공",stocksList);
    }

    public ResponseInfo findAllStocks() {

        List<Stocks> stocksList = stocksMongoRepository.findAll();

        return new ResponseInfo(1,"선택된 주식 정보 리스트 조회 성공",stocksList);
    }
}
