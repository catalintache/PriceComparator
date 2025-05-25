// src/main/java/eu/accesa/pricecomparator/analytics/controller/AnalyticsController.java
package eu.accesa.pricecomparator.analytics.controller;

import eu.accesa.pricecomparator.analytics.dto.TrendPointDto;
import eu.accesa.pricecomparator.analytics.dto.RecommendationDto;
import eu.accesa.pricecomparator.analytics.service.TrendService;
import eu.accesa.pricecomparator.analytics.service.RecommendationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")      // <-- schimbat
public class AnalyticsController {

    private final TrendService trend;

    public AnalyticsController(TrendService trend) {
        this.trend = trend;
    }

    //  GET /api/analytics/prices/history
    @GetMapping("/prices/history")
    public List<TrendPointDto> full(@RequestParam String productId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return trend.getDailyHistory(productId, from, to);
    }

    //  GET /api/analytics/prices/history/min
    @GetMapping("/prices/history/min")
    public List<TrendPointDto> min(@RequestParam String productId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return trend.getDailyMin(productId, from, to);
    }

    //  GET /api/analytics/prices/history/avg
    @GetMapping("/prices/history/avg")
    public List<TrendPointDto> avg(@RequestParam String productId,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return trend.getDailyAvg(productId, from, to);
    }
}