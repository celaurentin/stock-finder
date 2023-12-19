package com.ezstudio.trading.model;

public class StockMetric {
    private Double perc1yr;
    private Double std1yr;
    private Double perc2yr;
    private Double std2yr;
    private Double perc5yr;
    private Double std5yr;
    private Integer samples;

    public StockMetric() {
        // Always populated by service layer method
    }

    public Double getPerc1yr() {
        return perc1yr;
    }

    public void setPerc1yr(Double perc1yr) {
        this.perc1yr = perc1yr;
    }

    public Double getStd1yr() {
        return std1yr;
    }

    public void setStd1yr(Double std1yr) {
        this.std1yr = std1yr;
    }

    public Double getPerc2yr() {
        return perc2yr;
    }

    public void setPerc2yr(Double perc2yr) {
        this.perc2yr = perc2yr;
    }

    public Double getStd2yr() {
        return std2yr;
    }

    public void setStd2yr(Double std2yr) {
        this.std2yr = std2yr;
    }

    public Double getPerc5yr() {
        return perc5yr;
    }

    public void setPerc5yr(Double perc5yr) {
        this.perc5yr = perc5yr;
    }

    public Double getStd5yr() {
        return std5yr;
    }

    public void setStd5yr(Double std5yr) {
        this.std5yr = std5yr;
    }

    public Integer getSamples() {
        return samples;
    }

    public void setSamples(Integer samples) {
        this.samples = samples;
    }

    @Override
    public String toString() {
        return "StockMetric{" +
                "perc1yr=" + perc1yr +
                ", std1yr=" + std1yr +
                ", perc2yr=" + perc2yr +
                ", std2yr=" + std2yr +
                ", perc5yr=" + perc5yr +
                ", std5yr=" + std5yr +
                ", samples=" + samples +
                '}';
    }

}
