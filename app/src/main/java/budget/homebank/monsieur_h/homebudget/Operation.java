package budget.homebank.monsieur_h.homebudget;

import java.util.Date;

class Operation {
    private final float amount;
    private final int accountKey;
    private final Date date;
    private int categoryKey;

    Operation(float amount, int accountKey, Date date) {

        this.amount = amount;
        this.accountKey = accountKey;
        this.date = date;
    }

    public Operation(Operation op) {
        this.amount = op.amount;
        this.accountKey = op.accountKey;
        this.date = op.date;
        this.categoryKey = op.categoryKey;
    }


    int getCategoryKey() {
        return categoryKey;
    }

    void setCategoryKey(int categoryKey) {
        this.categoryKey = categoryKey;
    }

    Date getDate() {
        return date;
    }

    float getAmount() {
        return amount;
    }
}
