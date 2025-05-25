package eu.accesa.pricecomparator;

import eu.accesa.pricecomparator.common.util.UnitConversionUtil;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class UnitConversionUtilTest {

    @Test
    void toBaseUnit_convertsKgToGrams() {
        double grams = UnitConversionUtil.toBaseUnit(2, "kg");
        assertThat(grams).isEqualTo(2000.0);
    }
}
