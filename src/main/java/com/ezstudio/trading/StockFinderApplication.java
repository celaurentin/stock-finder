package com.ezstudio.trading;

import com.ezstudio.trading.exception.AlphaVantageApiException;
import com.ezstudio.trading.model.Stock;
import com.ezstudio.trading.service.StockHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ezstudio.trading.util.Constants.API_RATE_LIMIT_MESSAGE;

@SpringBootApplication
public class StockFinderApplication {

	@Autowired
	StockHistoryService stockHistoryService;

	public static void main(String[] args) {
		new SpringApplicationBuilder(StockFinderApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean
	public CommandLineRunner run() {
		return args -> {
			List<Stock> pendingStocks;
			int apiCalls = 0;

			pendingStocks = stockHistoryService.getPendingStocks();
			for (Stock pendingStock : pendingStocks) {
				// API restriction 500 calls per day
				if (apiCalls < 500) {
					System.out.println("Processing: " + pendingStock.toString());
					try {
						stockHistoryService.processStockMetrics(pendingStock);
					} catch (AlphaVantageApiException e) {
						if (e.getMessage().contains(API_RATE_LIMIT_MESSAGE)) break;
						else {
							// API restriction 5 calls per minute
							TimeUnit.SECONDS.sleep(13);
							apiCalls++;
							continue;
						}
					}

					// API restriction 5 calls per minute
					TimeUnit.SECONDS.sleep(13);
					apiCalls++;
				} else {
					System.out.println("Processed: " + apiCalls);
					break;
				}
			}
		};
	}
}
