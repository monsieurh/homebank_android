package budget.homebank.monsieur_h.homebudget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    static final int PROGRESS_PRECISION = 100;
    private final String name;
    private final int key;
    private int parentKey;
    private float[] monthlyBudget = new float[12];
    private float defaultMonthlyBudget = 0f;
    private List<Category> children = new ArrayList<>();
    private int flags;


    private List<Operation> operations = new ArrayList<>();

    Category(Category original) {
        this.name = original.name;
        this.key = original.key;
        this.parentKey = original.parentKey;
        this.monthlyBudget = original.monthlyBudget.clone();
        this.defaultMonthlyBudget = original.defaultMonthlyBudget;
        this.flags = original.flags;
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
        children.add(category);
    }

    public void setFlags(int flags) {

        this.flags = flags;
    }

    void setParent(Category parent) {
        this.parentKey = parent.getKey();
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
        operations.add(operation);
    }

    int getKey() {
        return key;
    }

    int getParentKey() {
        return parentKey;
    }


    public void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    float getMonthlyBudget(int month) {
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

    float getMonthlyExpense(int month) {
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

    float getMonthlyExpenseRatio(int month) {
        return getMonthlyExpense(month) / getMonthlyBudget(month);
    }

    @Override
    public String toString() {
        return String.format("%s : %f (%f/%f)", getName(), getMonthlyExpenseRatio(2), getMonthlyExpense(2), getMonthlyBudget(2));
    }

    public String getName() {
        return name;
    }

    List<Category> getChildren() {
        return children;
    }

    void filterForMonth(int month) {
        for (int i = operations.size() - 1; i >= 0; i--) {
            if (operations.get(i).getDate().getMonth() != month) {
                operations.remove(i);
            }
        }
    }

    boolean hasChild() {
        return getChildren().size() > 0;
    }

    public boolean hasFlag(int flags) {
        return (this.flags & flags) != 0;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
