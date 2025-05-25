package eu.accesa.pricecomparator.common.util;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.util.List;

/** Parser CSV cu separator „;”, sare peste header. */
@Component
public class CsvParser {

    public List<String[]> parse(Reader reader) throws Exception {

        CSVReader csvReader = new CSVReaderBuilder(reader)
                .withSkipLines(1) // ignoră prima linie (antet)
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build())
                .build();

        return csvReader.readAll();
    }
}