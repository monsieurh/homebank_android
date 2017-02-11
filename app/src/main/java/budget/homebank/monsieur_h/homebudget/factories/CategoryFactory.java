package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.Category;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class CategoryFactory implements HBFactory<Category> {
    @Override
    public Category fromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        Category category = new Category(
                attributes.getNamedItem("name").getNodeValue(),
                Integer.parseInt(attributes.getNamedItem("key").getNodeValue())
        );

        addParent(attributes, category);
        addMonthlyBudget(attributes, category);
        addFlags(attributes, category);

        return category;
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
}
