// src/main/java/eu/accesa/pricecomparator/catalog/service/PriceHistoryService.java
package eu.accesa.pricecomparator.catalog.service;

import eu.accesa.pricecomparator.catalog.dto.PriceDto;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import eu.accesa.pricecomparator.common.util.UnitConversionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceHistoryService {
    private final PriceRepository priceRepository;

    public PriceHistoryService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /** full history */
    public List<PriceDto> getHistory(String productId) {
        return priceRepository.findByProductIdOrderByDateAsc(productId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** history in a date‐range */
    public List<PriceDto> getHistory(
            String productId,
            LocalDate from,
            LocalDate to
    ) {
        return priceRepository
                .findByProductIdAndDateBetweenOrderByDateAsc(productId, from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private PriceDto toDto(PriceRecord rec) {
        double baseQty = UnitConversionUtil.toBaseUnit(rec.getPackageQuantity(), rec.getPackageUnit());
        double unitPrice = rec.getPrice() / baseQty;
        return new PriceDto(
                rec.getProductId(),
                rec.getStore(),
                rec.getDate(),
                unitPrice
        );
    }
}
