package budget.homebank.monsieur_h.homebudget.homebank;

import java.math.BigDecimal;

import budget.homebank.monsieur_h.homebudget.Util;

public class SubOperation {
    int categoryKey;
    Category category;
    BigDecimal amount;
    String memo;

    public SubOperation(int categoryKey, float amount, String memo) {
        this.categoryKey = categoryKey;
        this.amount = Util.NewBig(amount);
        this.memo = memo;
    }

}
