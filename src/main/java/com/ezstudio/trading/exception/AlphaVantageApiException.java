package com.ezstudio.trading.exception;

public class AlphaVantageApiException extends Exception{

    public AlphaVantageApiException(String errorMessage) {
        super(errorMessage);
    }
}
