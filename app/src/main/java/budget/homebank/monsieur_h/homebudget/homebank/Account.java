package budget.homebank.monsieur_h.homebudget.homebank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import budget.homebank.monsieur_h.homebudget.Util;

public class Account {
    private final int key;
    private final String name;
    private int currencyKey;
    private BigDecimal initialAmount = Util.NewBig();
    private List<Operation> operations = new ArrayList<Operation>();
    private int flags;
    private Currency currency;
    private XHB xhb;

    public Account(int key, String name, int curr, float initialAmount) {

        this.key = key;
        this.name = name;
        currencyKey = curr;

        this.initialAmount = Util.NewBig(initialAmount);
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean hasFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    public void addOperation(Operation op) {
        operations.add(op);
    }

    public Currency getCurrency() {
        return currency == null ? xhb.getDefaultCurrency() : currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setXhb(XHB xhb) {
        this.xhb = xhb;
    }

    public BigDecimal getFutureAmount() {
        BigDecimal sum = getInitialAmount();
        for (Operation op : operations) {
            sum = sum.add(op.getAmount());
        }
        return getCurrency().fixNegativeZero(sum);
    }

    public BigDecimal getInitialAmount() {
        return getCurrency().fixNegativeZero(initialAmount);
    }

    public BigDecimal getBankAmount() {
        BigDecimal sum = getInitialAmount();
        for (Operation op : operations) {
            if (op.getStatus() != OperationFlags.Status.TXN_STATUS_RECONCILED) {
                continue;
            }
            sum = sum.add(op.getAmount());
        }
        return getCurrency().fixNegativeZero(sum);
    }

    public BigDecimal getTodayAmount() {//todo:dis
        BigDecimal sum = getInitialAmount();
        for (Operation op : operations) {
            if (op.isFuture()) {
                continue;
            }
            sum = sum.add(op.getAmount());
        }
        return getCurrency().fixNegativeZero(sum);
    }
}
