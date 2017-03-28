package budget.homebank.monsieur_h.homebudget.homebank;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private final int key;
    private final String name;
    private int currencyKey;
    private float initialAmount;
    private List<Operation> operations = new ArrayList<Operation>();
    private int flags;
    private Currency currency;
    private XHB xhb;

    public Account(int key, String name, int curr, float initialAmount) {

        this.key = key;
        this.name = name;
        currencyKey = curr;
        this.initialAmount = initialAmount;
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
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setXhb(XHB xhb) {
        this.xhb = xhb;
    }

    public float getFuture() {
        float sum = 0;
        for (Operation op : operations) {
            sum += op.getAmount();
        }
        return sum + getInitialAmount();
    }

    public float getInitialAmount() {
        return initialAmount;
    }
}
