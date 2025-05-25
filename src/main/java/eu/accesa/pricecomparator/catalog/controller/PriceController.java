package eu.accesa.pricecomparator.catalog.controller;

import eu.accesa.pricecomparator.analytics.dto.TrendPointDto;
import eu.accesa.pricecomparator.analytics.service.TrendService;
import eu.accesa.pricecomparator.catalog.dto.PriceDto;
import eu.accesa.pricecomparator.catalog.service.PriceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService   priceService;
    private final TrendService   trendService;

    public PriceController(PriceService priceService,
                           TrendService trendService) {
        this.priceService = priceService;
        this.trendService = trendService;
    }

    /* ------------ import CSV ------------ */
    @PostMapping("/import")
    public void importCsv(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws Exception {
        priceService.importPrices(date);
    }

    /* ------------ listare preţuri (unitPrice) pe o zi ------------ */
    @GetMapping
    public List<PriceDto> listByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return priceService.listByDate(date);
    }

    /* ------------ analytics: MIN şi AVG pe zi ------------ */
    @GetMapping("/history/min")
    public List<TrendPointDto> dailyMin(
            @RequestParam String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return trendService.getDailyMin(productId, from, to);   // ← nume corect
    }

    @GetMapping("/history/avg")
    public List<TrendPointDto> dailyAvg(
            @RequestParam String productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return trendService.getDailyAvg(productId, from, to);   // ← nume corect
    }
}
