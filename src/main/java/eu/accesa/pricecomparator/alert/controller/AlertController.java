package eu.accesa.pricecomparator.alert.controller;

import eu.accesa.pricecomparator.alert.dto.AlertRequestDto;
import eu.accesa.pricecomparator.alert.model.PriceAlert;
import eu.accesa.pricecomparator.alert.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alerts;

    // === constructor necesar pentru a injecta AlertService ===
    public AlertController(AlertService alerts) {
        this.alerts = alerts;
    }

    /* ---- CRUD ---- */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriceAlert create(@RequestBody @Valid AlertRequestDto dto) {
        return alerts.create(dto);
    }

    @GetMapping
    public List<PriceAlert> list() {              // getAll() trebuie să existe în AlertService
        return alerts.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable Long id) {
        alerts.deactivate(id);
    }

    /* ---- trigger manual ---- */
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void triggerCheck() {
        alerts.checkAlerts();
    }
}