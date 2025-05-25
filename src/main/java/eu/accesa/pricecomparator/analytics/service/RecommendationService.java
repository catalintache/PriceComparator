// src/main/java/eu/accesa/pricecomparator/analytics/service/RecommendationService.java
package eu.accesa.pricecomparator.analytics.service;

import eu.accesa.pricecomparator.analytics.dto.RecommendationDto;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import eu.accesa.pricecomparator.analytics.util.UnitPriceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private final PriceRepository repo;

    public RecommendationService(PriceRepository repo) {
        this.repo = repo;
    }

    /** Best (lowest) unit-price across all stores for a given date */
    public RecommendationDto bestUnitPrice(LocalDate date) {
        PriceRecord best = repo.findByDate(date).stream()
                .min(Comparator.comparing(UnitPriceCalculator::calculate))
                .orElseThrow(() -> new IllegalArgumentException("No prices for " + date));

        return new RecommendationDto(
                best.getProductId(),
                best.getStore(),
                UnitPriceCalculator.calculate(best)
        );
    }

    /**
     * All other products on that date whose unit-price is <= the target’s.
     * Acts as “substitutes.”
     */
    public List<RecommendationDto> substitutes(
            String productId,
            LocalDate date
    ) {
        double targetPrice = repo.findByDate(date).stream()
                .filter(r -> r.getProductId().equals(productId))
                .findFirst()
                .map(UnitPriceCalculator::calculate)
                .orElseThrow(() -> new IllegalArgumentException("Product not found on " + date));

        return repo.findByDate(date).stream()
                .filter(r -> !r.getProductId().equals(productId))
                .filter(r -> UnitPriceCalculator.calculate(r) <= targetPrice)
                .map(r -> new RecommendationDto(
                        r.getProductId(),
                        r.getStore(),
                        UnitPriceCalculator.calculate(r)
                ))
                .collect(Collectors.toList());
    }
}
