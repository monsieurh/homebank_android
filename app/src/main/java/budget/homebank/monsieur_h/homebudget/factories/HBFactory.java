package budget.homebank.monsieur_h.homebudget.factories;

import org.w3c.dom.Node;

public interface HBFactory<T> {
    T fromNode(Node node);
}
