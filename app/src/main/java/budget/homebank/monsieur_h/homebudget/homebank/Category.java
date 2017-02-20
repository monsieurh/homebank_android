package budget.homebank.monsieur_h.homebudget.homebank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private final String name;
    private final int key;
    private int parentKey;
    private float[] monthlyBudget = new float[12];
    private float defaultMonthlyBudget = 0f;
    private List<Category> children = new ArrayList<>();
    private int flags;


    private List<Operation> operations = new ArrayList<>();
    private Category parent;

    Category(Category original) {
        this.name = original.name;
        this.key = original.key;
        this.parentKey = original.parentKey;
        this.monthlyBudget = original.monthlyBudget.clone();
        this.defaultMonthlyBudget = original.defaultMonthlyBudget;
        this.flags = original.flags;
        this.parent = original.parent;
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
            monthlyBudget[month - 1] = amount;// '-1' because January is 0 to me, but 1 to Homebank
        }
    }

    private void setDefaultMonthlyBudget(float amount) {
        for (int i = 0; i < 12; i++) {
            monthlyBudget[i] = amount;
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

    public float getMonthlyBudget(int month) {
        float monthly = monthlyBudget[month];
        if (monthly != 0) {
            return monthly;
        } else {
            for (Category child : getChildren()) {
                monthly += child.getMonthlyBudget(month);
            }
            return monthly;
        }
    }

    public float getMonthlyExpense(int month) {
        double sum = 0;
        for (Operation ope : operations) {
            if (ope.getDate().getMonth() == month) {
                sum += ope.getAmount();
            }
        }
        for (Category child : getChildren()) {
            sum += child.getMonthlyExpense(month);
        }
        return (float) sum;
    }

    public float getMonthlyExpenseRatio(int month) {
        return getMonthlyExpense(month) / getMonthlyBudget(month);
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

    public void filterForMonth(int month) {
        for (int i = operations.size() - 1; i >= 0; i--) {
            if (operations.get(i).getDate().getMonth() != month) {
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
}
