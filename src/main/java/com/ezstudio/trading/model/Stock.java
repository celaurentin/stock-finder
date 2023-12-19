package com.ezstudio.trading.model;

public class Stock {

    private String sym;
    private String name;
    private String sector;
    private String industry;
    private StockMetric metric;
    private Boolean processed;

    public Stock() {
        // Always populated by service layer method
    }

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public StockMetric getMetric() {
        return metric;
    }

    public void setMetric(StockMetric metric) {
        this.metric = metric;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public String toString() {
        return "StockHistory{" +
                "sym='" + sym + '\'' +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", processed=" + processed +
                '}';
    }
}
