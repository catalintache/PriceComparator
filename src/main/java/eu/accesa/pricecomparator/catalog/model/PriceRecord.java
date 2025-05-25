package eu.accesa.pricecomparator.catalog.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "price_records")
public class PriceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String store;
    private String currency;
    private LocalDate date;

    private double packageQuantity;
    private String packageUnit;
    private double price;

    /* -------- getters / setters -------- */

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getProductId()              { return productId; }
    public void setProductId(String productId){ this.productId = productId; }

    public String getStore()                  { return store; }
    public void setStore(String store)        { this.store = store; }

    public String getCurrency()               { return currency; }
    public void setCurrency(String currency)  { this.currency = currency; }

    public LocalDate getDate()                { return date; }
    public void setDate(LocalDate date)       { this.date = date; }

    public double getPackageQuantity()        { return packageQuantity; }
    public void setPackageQuantity(double q)  { this.packageQuantity = q; }

    public String getPackageUnit()            { return packageUnit; }
    public void setPackageUnit(String u)      { this.packageUnit = u; }

    public double getPrice()                  { return price; }
    public void setPrice(double price)        { this.price = price; }

}