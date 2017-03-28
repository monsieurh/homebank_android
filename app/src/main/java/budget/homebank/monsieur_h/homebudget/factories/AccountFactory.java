package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.homebank.Account;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AccountFactory implements HBFactory<Account> {
    @Override
    public Account fromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        float initialAmount = 0;
        if (attributes.getNamedItem("initial") != null) {
            initialAmount = Float.parseFloat(attributes.getNamedItem("initial").getNodeValue());
        }
        Account account = new Account(
                Integer.parseInt(attributes.getNamedItem("key").getNodeValue()),
                attributes.getNamedItem("name").getNodeValue(),
                Integer.parseInt(attributes.getNamedItem("curr").getNodeValue()),
                initialAmount
        );

        Node flags = attributes.getNamedItem("flags");
        if (flags != null) {
            account.setFlags(Integer.parseInt(flags.getNodeValue()));
        }
        return account;
    }
}
