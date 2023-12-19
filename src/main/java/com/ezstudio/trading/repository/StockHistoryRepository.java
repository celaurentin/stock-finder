package com.ezstudio.trading.repository;

import com.ezstudio.trading.model.Stock;
import com.ezstudio.trading.model.StockMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StockHistoryRepository {

  private final Logger logger = LoggerFactory.getLogger("StockHistoryRepository");
  private static final String GET_PENDING_STOCKS = "select trim(sym) sym, \"name\", sector, industry, processed from " +
          "stock_finder.stock_history where processed = ? order by sym";
  private static final String UPDATE_STOCK = "update stock_finder.stock_history set " +
          "perc_1yr = ?, std_1yr = ?, " +
          "perc_2yr = ?, std_2yr = ?, " +
          "perc_5yr = ?, std_5yr = ?, " +
          "samples = ?, processed = true, " +
          "updated_on = now() where sym = ?";

  @Autowired
  private JdbcTemplate jdbcTemplate;


  public List<Stock> getPendingStocks() {
    List<Stock> stocks = new ArrayList<>();
    try {
      stocks = jdbcTemplate.query(GET_PENDING_STOCKS, new BeanPropertyRowMapper<>(Stock.class));
    } catch(DataAccessException e) {
      logger.error("Error while getting all pending stocks - {}", e.getMessage());
    }
    return stocks;
  }

  public void updateStock(StockMetric metric, String symbol) {
    Object[] args = new Object[]{metric.getPerc1yr(), metric.getStd1yr(), metric.getPerc2yr(), metric.getStd2yr(),
    metric.getPerc5yr(), metric.getStd5yr(), metric.getSamples(), symbol.trim()};
    try {
      jdbcTemplate.update(UPDATE_STOCK, args);
    } catch(DataAccessException e) {
      logger.error("Error while updating stock {}: {}", symbol, e.getMessage());
    }

  }

}
