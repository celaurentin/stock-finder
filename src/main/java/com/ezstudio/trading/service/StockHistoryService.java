package com.ezstudio.trading.service;

import com.ezstudio.trading.exception.AlphaVantageApiException;
import com.ezstudio.trading.model.Stock;

import java.util.List;

public interface StockHistoryService {

    List<Stock> getPendingStocks();
    void processStockMetrics(Stock stock) throws AlphaVantageApiException;
}
