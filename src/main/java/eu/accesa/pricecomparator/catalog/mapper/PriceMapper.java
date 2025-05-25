package eu.accesa.pricecomparator.catalog.mapper;

import eu.accesa.pricecomparator.catalog.dto.PriceDto;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import eu.accesa.pricecomparator.common.util.UnitConversionUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Converteşte o linie CSV în entitatea {@link PriceRecord}.
 * CSV-ul Lidl/Kaufland/Profi are 8 coloane; entitatea noastră păstrează
 * doar câmpurile relevante.
 *
 *  0-product_id | 1-product_name | 2-category | 3-brand
 *  4-qty        | 5-unit         | 6-price    | 7-currency
 */
@Component
public class PriceMapper {

    public PriceRecord toEntity(String[] cols,
                                String store,
                                LocalDate importDate) {

        if (cols.length < 7) {               // defensiv
            throw new IllegalArgumentException(
                    "Linie CSV incorectă: " + String.join(" | ", cols));
        }

        PriceRecord pr = new PriceRecord();
        pr.setProductId(cols[0].trim());

        pr.setPackageQuantity(
                Double.parseDouble(cols[4].trim().replace(',', '.')));
        pr.setPackageUnit(cols[5].trim());

        pr.setPrice(
                Double.parseDouble(cols[6].trim().replace(',', '.')));
        pr.setCurrency(cols[7].trim());

        pr.setStore(store);
        pr.setDate(importDate);

        return pr;
    }

    public PriceDto toDto(PriceRecord rec) {
        double qtyBase = UnitConversionUtil.toBaseUnit(
                rec.getPackageQuantity(),
                rec.getPackageUnit());

        return new PriceDto(
                rec.getProductId(),
                rec.getStore(),
                rec.getDate(),
                rec.getPrice() / qtyBase  // unitPrice
        );
    }
}