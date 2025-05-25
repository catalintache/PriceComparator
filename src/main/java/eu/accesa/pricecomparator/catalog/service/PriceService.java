package eu.accesa.pricecomparator.catalog.service;

import com.opencsv.*;
import eu.accesa.pricecomparator.catalog.dto.PriceDto;
import eu.accesa.pricecomparator.catalog.mapper.PriceMapper;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;
import eu.accesa.pricecomparator.catalog.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository repo;
    private final PriceMapper     mapper;

    /** entități brute – folosit de controller-ul intern pentru debug */
    public List<PriceRecord> listRaw(LocalDate date) {
        return repo.findByDate(date);
    }

    /** DTO gata convertit (cu unitPrice) – ce vei expune prin API */
    @Transactional(readOnly = true)
    public List<PriceDto> listByDate(LocalDate date) {
        List<PriceRecord> records = repo.findByDate(date);
        return records.stream()
                .map(mapper::toDto)
                .toList();
    }

    public PriceService(PriceMapper mapper, PriceRepository repo) {
        this.mapper = mapper;
        this.repo   = repo;
    }

    // =============================  API  =============================

    /** Importă toate fişierele shop_YYYY-MM-DD.csv din resources/data */
    @Transactional
    public void importPrices(LocalDate date) {

        List<String> shops = List.of("lidl", "kaufland", "profi");
        List<PriceRecord> all = new ArrayList<>();

        for (String shop : shops) {
            String path = "data/%s_%s.csv".formatted(shop, date);
            Resource res = new ClassPathResource(path);
            all.addAll(readOneFile(res, shop, date));
        }

        repo.saveAll(all);
    }

    /** Returnează înregistrările brute pentru o zi */
    @Transactional(readOnly = true)
    public List<PriceRecord> getPrices(LocalDate date) {
        return repo.findByDate(date);
    }

    // =======================  helper privat  =========================

    private List<PriceRecord> readOneFile(Resource res,
                                          String store,
                                          LocalDate date) {

        if (!res.exists()) {
            throw new IllegalStateException("Nu am găsit fişierul " + res.getFilename());
        }

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withIgnoreQuotations(true)
                .build();

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))
                .withCSVParser(parser)
                .withSkipLines(2)          // primele două rânduri = antet
                .build()) {

            return reader.readAll()
                    .stream()
                    // rândurile goale / scurte sunt ignorate
                    .filter(cols -> cols.length >= 7)
                    .map(cols -> mapper.toEntity(cols, store, date))
                    .toList();

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Eroare la citirea fişierului " + res.getFilename(), ex);
        }
    }
}