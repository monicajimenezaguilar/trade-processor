package com.currencyfair.tradeprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class TradeProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeProcessorApplication.class, args);
	}
}
