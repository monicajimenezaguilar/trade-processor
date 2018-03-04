package com.currencyfair.tradeprocessor.service;

import java.util.Map;

import com.currencyfair.tradeprocessor.model.CurrencyPair;
import com.currencyfair.tradeprocessor.model.MarketVolumeIndicator;
import com.currencyfair.tradeprocessor.model.TradeDao;

public interface TradeService {
	
	TradeDao getTrade();

	Map<CurrencyPair, Long> getPairVolume();

	Map<MarketVolumeIndicator, Long> getMarketVolume();

}
