package eu.accesa.pricecomparator.common.util;

/** Convertește la unități de bază: 1 kg → 1000 g, 1 l → 1000 ml etc.  */
public final class UnitConversionUtil {

    private UnitConversionUtil() {}

    public static double toBaseUnit(double qty, String unit) {
        return switch (unit.toLowerCase()) {
            case "kg" -> qty * 1000;  // grame
            case "g"  -> qty;
            case "l"  -> qty * 1000;  // mililitri
            case "ml" -> qty;
            case "buc", "role" -> qty; // 1:1
            default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
        };
    }
}