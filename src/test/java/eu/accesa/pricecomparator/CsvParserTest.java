package eu.accesa.pricecomparator;


import eu.accesa.pricecomparator.common.util.CsvParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class CsvParserTest {

    @Test
    void parse_returnsAllLines() throws Exception {
        InputStream in = getClass().getResourceAsStream("/test-data/price-sample.csv");
        List<String[]> rows = CsvParser.parse(in);      // depinde de semnătură
        assertThat(rows).hasSize(3);
    }
}
