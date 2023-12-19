package com.ezstudio.trading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class MonthlyAdjusted {

    @JsonProperty("Meta Data")
    private MetaData metaData;
    @JsonProperty("Monthly Adjusted Time Series")
    private Map<String, Map<String, String>> timeSeries;

    public MonthlyAdjusted() {
        // Always populated by a service layer method
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Map<String, Map<String, String>> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Map<String, Map<String, String>> timeSeries) {
        this.timeSeries = timeSeries;
    }
}
