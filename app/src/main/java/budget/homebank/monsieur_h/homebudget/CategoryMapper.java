package budget.homebank.monsieur_h.homebudget;

import android.support.annotation.NonNull;
import android.util.Log;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryMapper {
    private List<Category> allCategories = new ArrayList<>();
    private Category NO_CATEGORY = new Category("NO_CATEGORY", 0);

    void linkParents() {
        for (Category child : allCategories) {
            if (child.getParentKey() != 0) {
                Category parent = find(child.getParentKey());
                parent.addChild(child);
                child.setParent(parent);
            }
        }
    }

    void addOperations(List<Operation> operationList) {
        Collections.sort(operationList, new Comparator<Operation>() {
            @Override
            public int compare(Operation o1, Operation o2) {
                return o1.getCategoryKey() - o2.getCategoryKey();
            }
        });

        for (Operation operation : operationList) {
            find(operation.getCategoryKey()).addOperation(operation);
        }
    }

    private Category find(int key) {
        for (Category cat :
                allCategories) {
            if (cat.getKey() == key) {
                return cat;
            }
        }
        Log.e("CATEGORY", String.format("Did not find category for key %d", key));
        return NO_CATEGORY;
    }

    void addFromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        Category cat = createCategory(attributes);
        addParent(attributes, cat);
        addMonthlyBudget(attributes, cat);
        addFlags(attributes, cat);

        allCategories.add(cat);
    }

    private void addFlags(NamedNodeMap attributes, Category cat) {
        Node flags = attributes.getNamedItem("flags");
        if (flags != null) {
            cat.setFlags(Integer.parseInt(flags.getNodeValue()));
        }
    }

    private void addMonthlyBudget(NamedNodeMap attributes, Category cat) {
        for (int i = 0; i <= 12; i++) {
            Node budgetNode = attributes.getNamedItem(String.format("b%d", i));
            if (budgetNode != null) {
                cat.setBudget(i, Float.parseFloat(budgetNode.getNodeValue()));
            }
        }
    }

    private void addParent(NamedNodeMap attributes, Category cat) {
        if (attributes.getNamedItem("parent") != null) {
            cat.setParentKey(Integer.parseInt(attributes.getNamedItem("parent").getNodeValue()));
        }
    }

    @NonNull
    private Category createCategory(NamedNodeMap attributes) {
        return new Category(
                attributes.getNamedItem("name").getNodeValue(),
                Integer.parseInt(attributes.getNamedItem("key").getNodeValue())
        );
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Category cat : allCategories) {
            str.append(cat.toString());
            str.append("\n");
        }
        return str.toString();
    }

}
