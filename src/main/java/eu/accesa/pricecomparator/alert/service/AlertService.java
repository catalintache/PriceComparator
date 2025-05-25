package eu.accesa.pricecomparator.alert.service;

import eu.accesa.pricecomparator.alert.dto.AlertRequestDto;
import eu.accesa.pricecomparator.alert.model.PriceAlert;
import eu.accesa.pricecomparator.alert.repository.AlertRepository;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class AlertService {

    private static final Logger log =
            LoggerFactory.getLogger(AlertService.class);

    private final AlertRepository alertRepo;
    private final PriceRepository priceRepo;
    private final JavaMailSender  mailSender;

    /** Constructor injection (Spring îl va apela) */
    public AlertService(AlertRepository alertRepo,
                        PriceRepository priceRepo,
                        JavaMailSender mailSender) {
        this.alertRepo  = alertRepo;
        this.priceRepo  = priceRepo;
        this.mailSender = mailSender;
    }

    /* ---------------- CRUD ---------------- */

    public PriceAlert create(AlertRequestDto dto) {
        PriceAlert a = new PriceAlert();
        a.setProductId(dto.getProductId());
        a.setUserId(dto.getUserId());
        a.setTargetPrice(dto.getTargetPrice());
        return alertRepo.save(a);
    }

    public List<PriceAlert> getAll() {
        return alertRepo.findAll();           // ori findByActiveTrue()
    }

    public void deactivate(Long id) {
        alertRepo.findById(id).ifPresent(a -> {
            a.setActive(false);
            alertRepo.save(a);
        });
    }

    /* ------------- Scheduled job ------------- */

    @Scheduled(cron = "${price-comparator.scheduling.alert-cron}")
    @Transactional
    public void checkAlerts() {
        // logica de scanare și trimitere e-mail
        log.debug("Running scheduled checkAlerts()");
    }
}