package eu.accesa.pricecomparator.catalog.dto;

import java.time.LocalDate;

public record PriceDto(
        String    productId,
        String    store,
        LocalDate date,
        double    unitPrice
) {}