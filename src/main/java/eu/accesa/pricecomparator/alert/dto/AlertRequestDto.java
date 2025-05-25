package eu.accesa.pricecomparator.alert.dto;

import jakarta.validation.constraints.*;

public class AlertRequestDto {

    @NotBlank
    private String productId;

    @Email @NotBlank
    private String userId;

    @Positive
    private double targetPrice;

    /* getters / setters */
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getTargetPrice() { return targetPrice; }
    public void setTargetPrice(double targetPrice) { this.targetPrice = targetPrice; }
}