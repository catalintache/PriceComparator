package eu.accesa.pricecomparator.analytics.util;

import eu.accesa.pricecomparator.common.util.UnitConversionUtil;
import eu.accesa.pricecomparator.catalog.model.PriceRecord;

public class UnitPriceCalculator {
    /** returns price per base unit (e.g. RON/kg) */
    public static double calculate(PriceRecord rec) {
        double baseQty = UnitConversionUtil.toBaseUnit(
                rec.getPackageQuantity(), rec.getPackageUnit());
        return rec.getPrice() / baseQty;
    }
}