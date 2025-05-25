package eu.accesa.pricecomparator.alert.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "price_alerts",
        indexes = @Index(name = "idx_alert_product", columnList = "productId"))
public class PriceAlert {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String productId;

    /** Email sau userId, în funcție de cum gestionezi autentificarea */
    @Email @NotBlank
    private String userId;

    @Positive
    private double targetPrice;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    /* getters / setters */

    // lombok ar face via @Data, dar las manual:
    public Long getId() { return id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public double getTargetPrice() { return targetPrice; }
    public void setTargetPrice(double targetPrice) { this.targetPrice = targetPrice; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}