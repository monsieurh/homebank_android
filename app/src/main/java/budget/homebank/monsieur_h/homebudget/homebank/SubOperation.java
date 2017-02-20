package budget.homebank.monsieur_h.homebudget.homebank;

public class SubOperation {
    int categoryKey;
    Category category;
    float amount;
    String memo;

    public SubOperation(int categoryKey, float amount, String memo) {
        this.categoryKey = categoryKey;
        this.amount = amount;
        this.memo = memo;
    }

}
