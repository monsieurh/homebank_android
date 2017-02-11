package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.homebank.HbCompat;
import budget.homebank.monsieur_h.homebudget.homebank.Operation;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class OperationFactory implements HBFactory<Operation> {
    @Override
    public Operation fromNode(Node operationNode) {
        NamedNodeMap attributes = operationNode.getAttributes();

        String date = attributes.getNamedItem("date").getNodeValue();
        String accountKey = attributes.getNamedItem("account").getNodeValue();
        String amount = attributes.getNamedItem("amount").getNodeValue();

        Operation operation = new Operation(
                Float.parseFloat(amount),
                Integer.parseInt(accountKey),
                HbCompat.julianToDate(Long.parseLong(date))
        );

        Node category = attributes.getNamedItem("category");
        if (category != null) {
            operation.setCategoryKey(Integer.parseInt(category.getNodeValue()));
        }

        Node wording = attributes.getNamedItem("wording");
        if (wording != null) {
            operation.setWording(wording.getNodeValue());
        }

        Node flags = attributes.getNamedItem("flags");
        if (flags != null) {
            operation.setFlags(Integer.parseInt(flags.getNodeValue()));
        }

        Node payeeKey = attributes.getNamedItem("payee");
        if (payeeKey != null) {
            operation.setPayeeKey(Integer.parseInt(payeeKey.getNodeValue()));
        }
        return operation;
    }
}
