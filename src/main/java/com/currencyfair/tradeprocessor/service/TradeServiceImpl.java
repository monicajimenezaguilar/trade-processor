package com.currencyfair.tradeprocessor.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.currencyfair.tradeprocessor.NoTradeAvailableException;
import com.currencyfair.tradeprocessor.PublishTradeException;
import com.currencyfair.tradeprocessor.model.CurrencyPair;
import com.currencyfair.tradeprocessor.model.MarketVolumeIndicator;
import com.currencyfair.tradeprocessor.model.Trade;
import com.currencyfair.tradeprocessor.model.TradeDao;

@Service
public class TradeServiceImpl implements TradeService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceImpl.class);
	private static final String API_RFT_URL = "http://localhost:8081/api/getTrade";
	
	@Autowired
	private MarketVolumeCalculator marketVolumeCalculator;

	@Autowired
	private PairVolumeCalculator pairVolumeCalculator;

	@Autowired
	private TradeMatcher tradeMatcher;


	@Override
	public TradeDao getTrade() {
		RestTemplate restTemplate = new RestTemplate();
		Trade trade = restTemplate.getForObject(API_RFT_URL, Trade.class);
		if (trade != null) {
			marketVolumeCalculator.initOrUpdateMarketVolume(MarketVolumeIndicator.PROCESSED);
		}
		TradeDao tradeDao = null;
		try {
			tradeDao = new TradeDao.TradeDaoBuilder(trade).build();
			pairVolumeCalculator.initOrUpdatePairVolume(tradeDao.getCurrencyPair());
			marketVolumeCalculator.initOrUpdateMarketVolume(MarketVolumeIndicator.LIVE);
			tradeMatcher.initOrUpdateBuyersAndSellers(tradeDao);
			if (tradeMatcher.isTradeMatched(tradeDao)) {
				marketVolumeCalculator.initOrUpdateMarketVolume(MarketVolumeIndicator.MATCHED);
				marketVolumeCalculator.decrementMarketVolume(MarketVolumeIndicator.LIVE);
			}
			LOGGER.info("Streaming tradeDao: {}", tradeDao);
		} catch (NoTradeAvailableException e) {
			LOGGER.warn(e.toString());
		} catch (PublishTradeException e) {
			marketVolumeCalculator.initOrUpdateMarketVolume(MarketVolumeIndicator.INVALID);
			LOGGER.warn(e.toString());
		}

		return tradeDao;
	}
	
	@Override
	public Map<CurrencyPair, Long> getPairVolume() {
		return pairVolumeCalculator.getPairVolume();
	}

	
	@Override
	public Map<MarketVolumeIndicator, Long> getMarketVolume() {
		return marketVolumeCalculator.getMarketVolume();
	}

}
