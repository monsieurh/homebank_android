package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.homebank.Account;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class AccountFactory implements HBFactory<Account> {
    @Override
    public Account fromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        Account account = new Account(
                Integer.parseInt(attributes.getNamedItem("key").getNodeValue()),
                attributes.getNamedItem("name").getNodeValue()
        );

        Node flags = attributes.getNamedItem("flags");
        if (flags != null) {
            account.setFlags(Integer.parseInt(flags.getNodeValue()));
        }
        return account;
    }
}
