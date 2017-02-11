package budget.homebank.monsieur_h.homebudget.homebank;

import java.util.Date;

public class Operation {
    private final float amount;
    private final int accountKey;
    private final Date date;
    private int payeeKey;
    private int categoryKey;
    private String wording;
    private int flags;
    private Payee payee;

    public Operation(float amount, int accountKey, Date date) {

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

    public void setCategoryKey(int categoryKey) {
        this.categoryKey = categoryKey;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(int flags) {
        return (this.flags & flags) != 0;
    }

    public String getWording() {
        return wording;
    }

    public void setWording(String wording) {
        this.wording = wording;
    }

    public int getPayeeKey() {
        return payeeKey;
    }

    public void setPayeeKey(int payeeKey) {
        this.payeeKey = payeeKey;
    }

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }
}
