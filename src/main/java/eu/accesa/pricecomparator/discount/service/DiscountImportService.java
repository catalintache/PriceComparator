package eu.accesa.pricecomparator.discount.service;

import eu.accesa.pricecomparator.common.util.CsvParser;
import eu.accesa.pricecomparator.discount.model.Discount;
import eu.accesa.pricecomparator.discount.repository.DiscountRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads discount CSVs for each store and saves to DB.
 */
@Service
public class DiscountImportService {
    private final CsvParser csvParser;
    private final DiscountRepository discountRepo;

    public DiscountImportService(CsvParser csvParser,
                                 DiscountRepository discountRepo) {
        this.csvParser = csvParser;
        this.discountRepo = discountRepo;
    }

    public void importDiscounts(LocalDate date) throws Exception {
        List<String> stores = List.of("lidl", "kaufland", "profi");
        List<Discount> all = new ArrayList<>();

        for (String store : stores) {
            String path = String.format("data/%s_discounts_%s.csv", store, date);
            ClassPathResource res = new ClassPathResource(path);
            if (!res.exists()) {
                continue;
            }

            try (var reader = new InputStreamReader(res.getInputStream())) {
                List<String[]> rows = csvParser.parse(reader);
                rows.stream()
                        .filter(cols -> cols.length >= 9)
                        .map(cols -> {
                            Discount d = new Discount();
                            d.setProductId(cols[0]);
                            d.setStore(store);
                            d.setFromDate(LocalDate.parse(cols[6]));
                            d.setToDate(LocalDate.parse(cols[7]));
                            d.setPercentage(Integer.parseInt(cols[8]));
                            return d;
                        })
                        .forEach(all::add);
            }
        }

        if (all.isEmpty()) {
            throw new RuntimeException("No discount files found for date " + date);
        }
        discountRepo.saveAll(all);
    }
}