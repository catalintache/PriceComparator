package eu.accesa.pricecomparator.analytics.service;

import eu.accesa.pricecomparator.analytics.dto.TrendPointDto;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import eu.accesa.pricecomparator.common.util.UnitConversionUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrendService {

    private final PriceRepository repo;

    public TrendService(PriceRepository repo) {
        this.repo = repo;
    }

    /* ---------- util ---------- */

    private TrendPointDto toDto(PriceRecord rec) {
        double unitPrice = rec.getPrice() /
                UnitConversionUtil.toBaseUnit(rec.getPackageQuantity(),
                        rec.getPackageUnit());
        TrendPointDto dto = new TrendPointDto();
        dto.setDate(rec.getDate());
        dto.setUnitPrice(unitPrice);
        return dto;
    }

    /* ---------- istoric complet ---------- */
    public List<TrendPointDto> getDailyHistory(String productId,
                                               LocalDate from,
                                               LocalDate to) {
        return repo.findByProductIdAndDateBetweenOrderByDateAsc(productId, from, to)
                .stream()
                .map(this::toDto)
                .toList();
    }

    /* ---------- minim pe zi ---------- */
    public List<TrendPointDto> getDailyMin(String productId,
                                           LocalDate from,
                                           LocalDate to) {
        return repo.findByProductIdAndDateBetweenOrderByDateAsc(productId, from, to)
                .stream()
                .map(this::toDto)
                .collect(Collectors.groupingBy(
                        TrendPointDto::getDate,
                        Collectors.minBy(Comparator.comparingDouble(TrendPointDto::getUnitPrice))))
                .values()
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparing(TrendPointDto::getDate))
                .toList();
    }

    /* ---------- medie aritmetică pe zi ---------- */
    public List<TrendPointDto> getDailyAvg(String productId,
                                           LocalDate from,
                                           LocalDate to) {

        Map<LocalDate, Double> avgMap =
                repo.findByProductIdAndDateBetweenOrderByDateAsc(productId, from, to)
                        .stream()
                        .map(this::toDto)
                        .collect(Collectors.groupingBy(
                                TrendPointDto::getDate,
                                Collectors.averagingDouble(TrendPointDto::getUnitPrice)));

        return avgMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    TrendPointDto dto = new TrendPointDto();
                    dto.setDate(e.getKey());
                    dto.setUnitPrice(e.getValue());
                    return dto;
                })
                .toList();
    }

    /* ---------- alias pt. controllerul vechi ---------- */
    public List<TrendPointDto> minPerDay(String productId,
                                         LocalDate from,
                                         LocalDate to) {
        return getDailyMin(productId, from, to);
    }

    public List<TrendPointDto> avgPerDay(String productId,
                                         LocalDate from,
                                         LocalDate to) {
        return getDailyAvg(productId, from, to);
    }
}