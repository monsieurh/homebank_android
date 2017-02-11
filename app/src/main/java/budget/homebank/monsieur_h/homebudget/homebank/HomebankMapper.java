package budget.homebank.monsieur_h.homebudget.homebank;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class HomebankMapper {
    private List<Category> categories = new ArrayList<>();
    private Category NO_CATEGORY = new Category("NO_CATEGORY", 0);
    private List<Payee> payees = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();

    public List<Category> getTopLevelCategoriesForMonth(int month) {
        List<Category> topLevel = new ArrayList<>();
        for (Category category : categories) {
            Category filtered = new Category(category);
            filtered.filterForMonth(month);
            if (filtered.hasChild()) {
                topLevel.add(filtered);
            }
        }
        return topLevel;
    }

    List<Category> getTopLevelCategories() {
        List<Category> topLevel = new ArrayList<>();
        for (Category category : categories) {
            if (category.hasChild()) {
                topLevel.add(category);
            }
        }
        return topLevel;
    }

    public void bindAll() {
        for (Category child : categories) {
            if (child.getParentKey() != 0) {
                Category parent = findCategory(child.getParentKey());
                parent.addChild(child);
                child.setParent(parent);
            }
        }
        for (Operation op : operations) {
            findCategory(op.getCategoryKey()).addOperation(op);
            op.setPayee(findPayee(op.getPayeeKey()));
        }
    }

    private Payee findPayee(int key) {
        for (Payee p : payees) {
            if (p.getKey() == key) {
                return p;
            }
        }
        Log.e("HBMapper", "Could not find Payee " + key);
        return null;
    }

    public void addOperation(Operation operation) {
        operations.add(operation);

    }

    public Category findCategory(int key) {
        for (Category cat :
                categories) {
            if (cat.getKey() == key) {
                return cat;
            }
        }
        Log.e("HBMapper", "Could not find Category" + key);
        return NO_CATEGORY;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Category cat : categories) {
            str.append(cat.toString());
            str.append("\n");
        }
        return str.toString();
    }

    public List<Category> getCategories() {
        return categories;
    }

    void filterForMonth(int month) {
        for (int i = categories.size() - 1; i >= 0; i--) {
            if (categories.get(i).getMonthlyBudget(month) == 0) {
                categories.remove(i);
            }
        }
    }

    public void addPayee(Payee payee) {
        Log.d("P", "Adding payee " + payee);
        payees.add(payee);
    }
}
