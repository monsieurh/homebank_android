package budget.homebank.monsieur_h.homebudget.homebank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomebankMapper {
    private List<Category> categories = new ArrayList<>();
    private Category NO_CATEGORY = new Category("NO_CATEGORY", 0);
    private Payee NO_PAYEE = new Payee(0, "NO_PAYEE");
    private List<Payee> payees = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();

    public List<Category> getTopCategoriesForMonthlyBudget(int month) {
        List<Category> topLevel = new ArrayList<>();
        for (Category category : categories) {
            if (category.getMonthlyBudget(month) == 0 && !category.hasFlag(CategoryFlags.GF_BUDGET)) {
                continue;
            }
            Category copy = new Category(category);
            copy.filterForMonth(month);
            if (!copy.hasParent()) {
                topLevel.add(copy);
            }
        }

        for (Category cat : topLevel) {
            filterOutNoBudgetAccounts(cat);
        }

        Collections.sort(topLevel, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return topLevel;
    }

    public void filterOutNoBudgetAccounts(Category currentCat) {
        for (Category childCat : currentCat.getChildren()) {
            filterOutNoBudgetAccounts(childCat);
        }

        for (int i = currentCat.getOperations().size() - 1; i >= 0; i--) {
            final Operation o = currentCat.getOperations().get(i);
            if (o.getAccount().hasFlag(AccountFlags.AF_NOBUDGET)) {
                currentCat.removeOperation(o);
            }
        }
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
            Account account = findAccount(op.getAccountKey());
            account.addOperation(op);
            op.setAccount(account);
        }
    }

    private Account findAccount(int key) {
        for (Account a : accounts) {
            if (a.getKey() == key) {
                return a;
            }
        }
//        Log.e("HBMapper", "Could not find Account " + key);
        return null;
    }

    private Payee findPayee(int key) {
        for (Payee p : payees) {
            if (p.getKey() == key) {
                return p;
            }
        }
//        Log.e("HBMapper", "Could not find Payee " + key);
        return NO_PAYEE;
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
//        Log.e("HBMapper", "Could not find Category" + key);
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

    public void addPayee(Payee payee) {
        payees.add(payee);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
