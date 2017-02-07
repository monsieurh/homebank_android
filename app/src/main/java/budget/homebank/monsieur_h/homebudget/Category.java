package budget.homebank.monsieur_h.homebudget;

import java.util.ArrayList;
import java.util.List;

class Category {
    private final String name;
    private final int key;
    private int parentKey;
    private float[] monthlyBudget = new float[13];
    private List<Category> children = new ArrayList<>();
    private int flags;
    private List<Operation> operations = new ArrayList<>();

    Category(String name, int key) {

        this.name = name;
        this.key = key;
    }

    void addChild(Category category) {
        children.add(category);
    }

    void setFlags(int flags) {

        this.flags = flags;
    }

    void setParent(Category parent) {
        this.parentKey = parent.getKey();
    }

    void setBudget(int month, float amount) {
        if (month == 0) {
            setDefaultMonthlyBudget(amount);
        }
        monthlyBudget[month] = amount;
    }

    private void setDefaultMonthlyBudget(float amount) {
        for (int i = 0; i <= 12; i++) {
            monthlyBudget[i] = amount;
        }
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


    void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    float getMonthlyBudget(int month) {
        return monthlyBudget[month];
    }

    float getMonthlyExpense(int month) {
        double sum = 0;
        for (Operation ope : operations) {
            if (ope.getDate().getMonth() == month) {
                sum += ope.getAmount();
            }
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

    public List<Category> getChildren() {
        return children;
    }

    void filterForMonth(int month) {
        for (int i = operations.size() - 1; i >= 0; i++) {
            if (operations.get(i).getDate().getMonth() != month) {
                operations.remove(i);
            }
        }
    }
}
