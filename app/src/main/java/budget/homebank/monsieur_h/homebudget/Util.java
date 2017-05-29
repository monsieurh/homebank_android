package budget.homebank.monsieur_h.homebudget;

import java.math.BigDecimal;

public class Util {
    public static final float PROGRESS_PRECISION = 100;
    public static final int ROUND_METHOD = BigDecimal.ROUND_HALF_UP;
    private static final int ROUND_PRECISION = 5;

    public static BigDecimal NewBig(float value) {
        return new BigDecimal(value).setScale(ROUND_PRECISION, ROUND_METHOD);
    }

    public static BigDecimal NewBig() {
        return NewBig(0f);
    }

    public static BigDecimal NewBig(BigDecimal nullable) {
        return nullable == null ? NewBig() : nullable;
    }
}
