package com.currencyfair.tradeprocessor;

public class NoTradeAvailableException extends PublishTradeException {

	private static final long serialVersionUID = -7282561611505686544L;

	public NoTradeAvailableException(String cause) {
		super(cause);
	}

}
