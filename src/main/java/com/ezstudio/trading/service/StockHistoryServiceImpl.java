package com.ezstudio.trading.service;

import com.ezstudio.trading.exception.AlphaVantageApiException;
import com.ezstudio.trading.model.MonthlyAdjusted;
import com.ezstudio.trading.model.Stock;
import com.ezstudio.trading.model.StockMetric;
import com.ezstudio.trading.repository.StockHistoryRepository;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.BasicHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ezstudio.trading.util.Constants.*;

@Service
public class StockHistoryServiceImpl implements StockHistoryService {
    private static final Logger log = LoggerFactory.getLogger(StockHistoryServiceImpl.class);

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Override
    public List<Stock> getPendingStocks() {
        List<Stock> pendingStocks;
        pendingStocks = stockHistoryRepository.getPendingStocks();
        log.debug("Total pending stocks: {}", pendingStocks.size());
        return pendingStocks;
    }

    @Override
    public void processStockMetrics(Stock stock) throws AlphaVantageApiException {
        Object maybeErrorResponse;
        StockMetric stockMetric;
        try {
            final String urlOverHttps =
                    "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY_ADJUSTED&apikey=SHW5MMRRFPBVDHBL&symbol=" + stock.getSym();

            final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            final SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, acceptingTrustStrategy)
                    .build();
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("https", sslsf)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();

            final BasicHttpClientConnectionManager connectionManager =
                    new BasicHttpClientConnectionManager(socketFactoryRegistry);
            final CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .build();

            final HttpComponentsClientHttpRequestFactory requestFactory =
                    new HttpComponentsClientHttpRequestFactory(httpClient);
            final ResponseEntity<MonthlyAdjusted> monthlyAdjustedResponse = new RestTemplate(requestFactory)
                    .exchange(urlOverHttps, HttpMethod.GET, null, MonthlyAdjusted.class);


            if (monthlyAdjustedResponse.getBody()!=null &&
            monthlyAdjustedResponse.getBody().getTimeSeries()!=null) {
                stockMetric = calculateStockMetrics(monthlyAdjustedResponse.getBody().getTimeSeries());
                updateStockHistory(stock.getSym(), stockMetric);
            } else {
                // response could contains an error message
                maybeErrorResponse = new RestTemplate(requestFactory).exchange(
                        urlOverHttps, HttpMethod.GET, null, String.class);
                log.error("Skipping stock metric {}.", stock.getSym());
                throw new AlphaVantageApiException("AlphaVantageApiException: " + maybeErrorResponse);
            }
        } catch (Exception ex) {
            log.error("Error calling AlphaVantage API: {}", ex.getMessage());
            throw new AlphaVantageApiException(ex.getMessage());
        }
    }

    private void updateStockHistory(String sym, StockMetric metric) {
        stockHistoryRepository.updateStock(metric, sym);
        log.debug("Stock {} updated", sym);
    }


    private StockMetric calculateStockMetrics(Map<String, Map<String, String>> timeSeries) {
        log.debug("Computing Metric from stock time series...");
        StockMetric metric = new StockMetric();
        Double upperAdjustedClose;
        Double oneYearLowerAdjustedClose;
        Double twoYearsLowerAdjustedClose;
        Double fiveYearsLowerAdjustedClose;
        Double oneYearPercentage;
        Double twoYearsPercentage = 0d;
        Double fiveYearsPercentage = 0d;
        Double oneYearStd = 0d;
        Double twoYearsStd = 0d;
        Double fiveYearsStd = 0d;
        int samples;

        Double[] adjustedCloseValues = getAdjustedCloseValues(new ArrayList<>(timeSeries.values()));
        int totalSamples = adjustedCloseValues.length;
        log.debug("Total samples for this SYM: {}", totalSamples);

        upperAdjustedClose = adjustedCloseValues[0];
        if (totalSamples >= ONE_YEAR_SAMPLES) {
            oneYearLowerAdjustedClose = adjustedCloseValues[11];
            oneYearPercentage = growthRate(upperAdjustedClose, oneYearLowerAdjustedClose);
            oneYearStd = getStandardDeviation(adjustedCloseValues, 11);
            if (totalSamples >= TWO_YEARS_SAMPLES) {
                twoYearsLowerAdjustedClose = adjustedCloseValues[23];
                twoYearsPercentage = growthRate(upperAdjustedClose, twoYearsLowerAdjustedClose);
                twoYearsStd = getStandardDeviation(adjustedCloseValues, 23);
                if (totalSamples >= FIVE_YEARS_SAMPLES) {
                    samples = 60;
                    fiveYearsLowerAdjustedClose = adjustedCloseValues[59];
                    fiveYearsStd = getStandardDeviation(adjustedCloseValues, 59);
                } else {
                    samples = totalSamples;
                    fiveYearsLowerAdjustedClose = adjustedCloseValues[samples-1];
                    fiveYearsStd = getStandardDeviation(adjustedCloseValues, samples-1);
                }
                fiveYearsPercentage = growthRate(upperAdjustedClose, fiveYearsLowerAdjustedClose);
            } else {
                samples = totalSamples;
                twoYearsLowerAdjustedClose = adjustedCloseValues[samples-1];
                twoYearsPercentage = growthRate(upperAdjustedClose, twoYearsLowerAdjustedClose);
            }
        } else {
            samples = totalSamples;
            oneYearLowerAdjustedClose = adjustedCloseValues[samples-1];
            oneYearPercentage = growthRate(upperAdjustedClose, oneYearLowerAdjustedClose);
            oneYearStd = getStandardDeviation(adjustedCloseValues, samples-1);
        }

        metric.setSamples(samples);
        metric.setPerc1yr(oneYearPercentage);
        metric.setPerc2yr(twoYearsPercentage);
        metric.setPerc5yr(fiveYearsPercentage);
        metric.setStd1yr(oneYearStd);
        metric.setStd2yr(twoYearsStd);
        metric.setStd5yr(fiveYearsStd);
        log.debug(metric.toString());
        return metric;
    }

    private Double growthRate(Double upper, Double lower) {
        return ((upper / lower) * 100) - 100;
    }

    private Double[] getAdjustedCloseValues(List<Map<String, String>> values) {
        List<Double> result = new ArrayList<>();
        for (Map<String, String> entry: values) {
            result.add(Double.valueOf(entry.get(ADJUSTED_CLOSE)));
        }
        return result.toArray(new Double[0]);
    }

    private Double getStandardDeviation(Double[] values, int sliceIndex) {
        Double average = getAverage(values, sliceIndex);
        Double variance = 0d;
        for (int i = 0; i <= sliceIndex; i++) {
            variance += Math.pow((values[i] - average),2) / (sliceIndex + 1);
        }
        return Math.sqrt(variance);
    }

    private Double getAverage(Double[] values, int sliceIndex) {
        Double average = 0d;
        for (int i = 0; i <= sliceIndex; i++) {
            average += values[i];
        }
        return average / (sliceIndex + 1);
    }
}
