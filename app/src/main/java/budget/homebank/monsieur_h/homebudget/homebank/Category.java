package budget.homebank.monsieur_h.homebudget.homebank;

import budget.homebank.monsieur_h.homebudget.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Category implements Serializable {
    private final String name;
    private final int key;
    private int parentKey;
    private BigDecimal[] monthlyBudget = new BigDecimal[12];
    private float defaultMonthlyBudget = 0f;
    private List<Category> children = new ArrayList<>();
    private int flags;


    private List<Operation> operations = new ArrayList<>();
    private Category parent;
    private XHB xhb;

    Category(Category original) {
        initBudget();
        this.name = original.name;
        this.key = original.key;
        this.parentKey = original.parentKey;
        this.monthlyBudget = original.monthlyBudget.clone();
        this.defaultMonthlyBudget = original.defaultMonthlyBudget;
        this.flags = original.flags;
        this.parent = original.parent;
        this.xhb = original.xhb;
        for (Category cat : original.getChildren()) {
            children.add(new Category(cat));
        }
        for (Operation op : original.operations) {
            operations.add(new Operation(op));
        }
    }

    public Category(String name, int key) {

        this.name = name;
        this.key = key;
    }

    private void initBudget() {
        for (int i = 0; i < 12; i++) {
            monthlyBudget[i] = Util.NewBig();
        }
    }

    void addChild(Category category) {
        if (children.contains(category)) return;
        children.add(category);
    }

    public void setFlags(int flags) {

        this.flags = flags;
    }

    public void setBudget(int month, float amount) {
        if (month == 0) {
            setDefaultMonthlyBudget(amount);
        } else {
            monthlyBudget[month - 1] = Util.NewBig(amount);// '-1' because January is 0 to me, but 1 to Homebank
        }
    }

    private void setDefaultMonthlyBudget(float amount) {
        for (int i = 0; i < 12; i++) {
            monthlyBudget[i] = Util.NewBig(amount);
        }
        defaultMonthlyBudget = amount;
    }

    void addOperation(Operation operation) {
        if (operations.contains(operation)) return;
        operations.add(operation);
    }

    public int getKey() {
        return key;
    }

    int getParentKey() {
        return parentKey;
    }

    public void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    public BigDecimal getMonthlyBudget(int month) {
        BigDecimal monthly = Util.NewBig(monthlyBudget[month]);
        if (monthly != null && monthly.doubleValue() != 0) {
            return monthly;
        } else {
            for (Category child : getChildren()) {
                monthly = monthly.add(child.getMonthlyBudget(month));
            }
            return monthly;
        }
    }

    public BigDecimal getMonthlyExpense(int month) {
        BigDecimal sum = Util.NewBig();
        for (Operation ope : operations) {
            if (ope.getDate().getMonth() == month) {
                sum = sum.add(ope.getAmountForCategory(key));
            }
        }
        for (Category child : getChildren()) {
            sum = sum.add(child.getMonthlyExpense(month));
        }
        return sum;
    }

    public BigDecimal getMonthlyExpenseRatio(int month) {
        BigDecimal monthlyBudget = getMonthlyBudget(month);
        if (monthlyBudget.doubleValue() == 0) {
            return Util.NewBig();
        }
        return getMonthlyExpense(month).divide(monthlyBudget, Util.ROUND_METHOD);
    }

    @Override
    public String toString() {
        return String.format("%s : %f (%f/%f)", getName(), getMonthlyExpenseRatio(2), getMonthlyExpense(2), getMonthlyBudget(2));
    }

    public String getName() {
        return name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void filterForMonth(int month, int year) {
        for (int i = operations.size() - 1; i >= 0; i--) {
            Date date = operations.get(i).getDate();
            if (date.getMonth() != month || date.getYear() != year) {
                operations.remove(i);
            }
        }
    }

    boolean hasChild() {
        return getChildren().size() > 0;
    }

    public boolean hasFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    void removeOperation(Operation o) {
        operations.remove(o);
    }

    public boolean hasParent() {
        return !(getParent() == null);
    }

    public Category getParent() {
        return parent;
    }

    void setParent(Category parent) {
        this.parent = parent;
        this.parentKey = parent.getKey();
    }

    public boolean hasChildren() {
        return !getChildren().isEmpty();
    }

    public boolean hasOperations() {
        return operations.size() != 0;
    }

    public XHB getXhb() {
        return xhb;
    }

    public void setXhb(XHB xhb) {
        this.xhb = xhb;
    }
}
