package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.homebank.Payee;
import org.w3c.dom.Node;

public class PayeeFactory implements HBFactory<Payee> {

    @Override
    public Payee fromNode(Node node) {
        Node key = node.getAttributes().getNamedItem("key");
        Node name = node.getAttributes().getNamedItem("name");
        return new Payee(Integer.parseInt(key.getNodeValue()), name.getNodeValue());
    }
}
