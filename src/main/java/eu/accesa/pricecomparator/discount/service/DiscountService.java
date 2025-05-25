package eu.accesa.pricecomparator.discount.service;

import eu.accesa.pricecomparator.discount.dto.DiscountDto;
import eu.accesa.pricecomparator.discount.model.Discount;
import eu.accesa.pricecomparator.discount.repository.DiscountRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for retrieving top and new discounts.
 */
@Service
public class DiscountService {
    private final DiscountRepository repo;

    public DiscountService(DiscountRepository repo) {
        this.repo = repo;
    }

    /** Returns the highest-percentage discounts still active */
    public List<DiscountDto> topDiscounts(LocalDate asOf, int limit) {
        return repo
                .findByFromDateLessThanEqualAndToDateGreaterThanEqual(asOf, asOf)
                .stream()
                .sorted((a,b) -> Integer.compare(b.getPercentage(), a.getPercentage()))
                .limit(limit)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Returns discounts that started within the last 24h */
    public List<DiscountDto> newDiscounts(LocalDate asOf) {
        LocalDate since = asOf.minusDays(1);
        return repo.findByFromDateAfter(since)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private DiscountDto toDto(Discount d) {
        DiscountDto dto = new DiscountDto();
        dto.setProductId(d.getProductId());
        dto.setStore(d.getStore());
        dto.setFromDate(d.getFromDate());
        dto.setToDate(d.getToDate());
        dto.setPercentage(d.getPercentage());
        return dto;
    }
}