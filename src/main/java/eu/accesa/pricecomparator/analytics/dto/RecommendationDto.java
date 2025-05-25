package eu.accesa.pricecomparator.analytics.dto;

public class RecommendationDto {
    private String productId;
    private String store;
    private double unitPrice;

    public RecommendationDto() {}

    public RecommendationDto(String productId, String store, double unitPrice) {
        this.productId = productId;
        this.store     = store;
        this.unitPrice = unitPrice;
    }

    // getters & setters
    public String getProductId() { return productId; }
    public void setProductId(String p) { this.productId = p; }

    public String getStore() { return store; }
    public void setStore(String s) { this.store = s; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double u) { this.unitPrice = u; }
}
