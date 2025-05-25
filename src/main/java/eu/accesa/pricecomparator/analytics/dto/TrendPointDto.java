package eu.accesa.pricecomparator.analytics.dto;

import java.time.LocalDate;

public class TrendPointDto {
    private LocalDate date;
    private double unitPrice;
    public TrendPointDto() {}

    public TrendPointDto(LocalDate date, double unitPrice) {
        this.date = date;
        this.unitPrice = unitPrice;
    }

    // getters & setters
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}