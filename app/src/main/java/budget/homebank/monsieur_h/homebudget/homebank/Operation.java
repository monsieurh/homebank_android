package budget.homebank.monsieur_h.homebudget.homebank;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Operation {
    private final int accountKey;
    private final Date date;
    private double amount;
    private int payeeKey;
    private int categoryKey;
    private String wording;
    private int flags;
    private Payee payee;
    private Account account;
    private List<SubOperation> subOperations = new ArrayList<>();
    private XHB xhb;
    private OperationFlags.Status status = OperationFlags.Status.TXN_STATUS_NONE;

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
        this.account = op.account;
        this.subOperations.addAll(op.subOperations);
        this.xhb = op.xhb;
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

    public double getAmount() {
        return amount;
    }

    public double getAmountForCategory(int categoryKey) {
        if (subOperations.size() == 0) {
            return amount;
        }

        float sum = 0;
        for (SubOperation sub : subOperations) {
            if (sub.categoryKey != categoryKey) continue;
            sum += sub.amount;
        }
        return sum;
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

    public int getAccountKey() {
        return accountKey;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setSubOperations(List<SubOperation> split) {
        subOperations.clear();
        subOperations.addAll(split);
    }

    public List<SubOperation> getSuboperations() {
        return subOperations;
    }

    public void setXhb(XHB xhb) {
        this.xhb = xhb;
    }

    public OperationFlags.Status getStatus() {
        return status;
    }

    public void setStatus(int statusCode) {
        switch (statusCode) {
            case 0:
                this.status = OperationFlags.Status.TXN_STATUS_NONE;
                break;
            case 2:
                this.status = OperationFlags.Status.TXN_STATUS_RECONCILED;
                break;
        }
    }

    public boolean isFuture() {
        return getDate().compareTo(new Date()) > 0;
    }

    void round() {
        final double amount = getAmount();
        final Currency currency = getAccount().getCurrency();
        final int precision = currency.decimalPrecision;

        final double precisionMultiplier = Math.pow(10, precision);
        this.amount = (float) (Math.round(amount * precisionMultiplier) / precisionMultiplier);
        if (this.amount != amount) {
            Log.e("PRECISION", "Error in precision of operation amount. Fixing...");
        }
    }
}
