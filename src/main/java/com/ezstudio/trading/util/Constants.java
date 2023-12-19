package com.ezstudio.trading.util;

public final class Constants {

    private Constants() {}
    public static final String OPEN = "1. open";
    public static final String HIGH = "2. high";
    public static final String LOW = "3. low";
    public static final String CLOSE = "4. close";
    public static final String ADJUSTED_CLOSE = "5. adjusted close";
    public static final String VOLUME = "6. volume";
    public static final String COMPLETED = "completed";
    public static final String DIVIDEND_AMOUNT = "7. dividend amount";
    public static final Integer MAX_NUM_SAMPLES = 60;
    public static final Integer ONE_YEAR_SAMPLES = 12;
    public static final Integer TWO_YEARS_SAMPLES = 24;
    public static final Integer FIVE_YEARS_SAMPLES = 60;
    public static final String API_RATE_LIMIT_MESSAGE = "Our standard API call frequency is 5 calls per minute and 500 calls per day";
    public static final Integer MAX_CALLS_DAY = 25;

}
