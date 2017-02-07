package budget.homebank.monsieur_h.homebudget;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void addOperation(Operation operation) {
        operations.add(operation);
    }

    public int getKey() {
        return key;
    }

    public int getParentKey() {
        return parentKey;
    }


    public void setParentKey(int parentKey) {
        this.parentKey = parentKey;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", key=" + key +
                ", parentKey=" + parentKey +
                ", monthlyBudget=" + Arrays.toString(monthlyBudget) +
                ", children=" + children.size() +
                ", operations=" + operations.size() +
                ", flags=" + flags +
                '}';
    }

}
