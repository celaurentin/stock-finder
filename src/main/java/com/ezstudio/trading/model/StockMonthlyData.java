package com.ezstudio.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StockMonthlyData {


    private String open;
    private String high;
    private String low;
    private String close;
    private String adjustedClose;
    private String volume;
    private String dividendAmount;

    public StockMonthlyData(String open, String high, String low, String close, String adjustedClose, String volume, String dividendAmount) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjustedClose = adjustedClose;
        this.volume = volume;
        this.dividendAmount = dividendAmount;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(String adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(String dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    @Override
    public String toString() {
        return "MonthlyAdjustedTimeSerie{" +
                "open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", close='" + close + '\'' +
                ", adjustedClose='" + adjustedClose + '\'' +
                ", volume='" + volume + '\'' +
                ", dividendAmount='" + dividendAmount + '\'' +
                '}';
    }
}
