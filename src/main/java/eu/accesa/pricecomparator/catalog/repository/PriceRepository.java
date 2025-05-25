package eu.accesa.pricecomparator.catalog.repository;

import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<PriceRecord, Long> {
    Optional<PriceRecord> findFirstByProductIdOrderByDateDesc(String productId);
    /* --- query-uri deja folosite în servicii / controllere --- */

    // toate preţurile dintr-o zi
    List<PriceRecord> findByDate(LocalDate date);

    // istoric complet pentru un produs (ordonat cronologic)
    List<PriceRecord> findByProductIdOrderByDateAsc(String productId);

    // istoric într-un interval [from, to] inclusiv – ordonat cronologic
    List<PriceRecord> findByProductIdAndDateBetweenOrderByDateAsc(
            String productId, LocalDate from, LocalDate to);

    // aceeaşi interogare, dar fără ORDER BY (TrendService agreghează şi sortează ulterior)
    List<PriceRecord> findByProductIdAndDateBetween(
            String productId, LocalDate from, LocalDate to);
}