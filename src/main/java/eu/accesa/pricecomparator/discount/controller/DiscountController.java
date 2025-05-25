package eu.accesa.pricecomparator.discount.controller;

import eu.accesa.pricecomparator.discount.dto.DiscountDto;
import eu.accesa.pricecomparator.discount.service.DiscountImportService;
import eu.accesa.pricecomparator.discount.service.DiscountService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Exposes endpoints to import and query discounts.
 */
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {
    private final DiscountImportService importSvc;
    private final DiscountService discountSvc;

    public DiscountController(DiscountImportService importSvc,
                              DiscountService discountSvc) {
        this.importSvc = importSvc;
        this.discountSvc = discountSvc;
    }

    /** POST /api/discounts/import?date=YYYY-MM-DD */
    @PostMapping("/import")
    public ResponseEntity<Void> importDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) throws Exception {
        importSvc.importDiscounts(date);
        return ResponseEntity.ok().build();
    }

    /** GET /api/discounts/top?limit=N */
    @GetMapping("/top")
    public ResponseEntity<List<DiscountDto>> topDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(discountSvc.topDiscounts(date, limit));
    }

    @GetMapping("/new")
    public ResponseEntity<List<DiscountDto>> newDiscounts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(discountSvc.newDiscounts(date));
    }
}