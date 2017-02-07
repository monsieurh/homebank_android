package budget.homebank.monsieur_h.homebudget;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

class OperationFactory {
    Operation fromNode(Node operationNode) {
        NamedNodeMap attributes = operationNode.getAttributes();

        String date = attributes.getNamedItem("date").getNodeValue();
        String accountKey = attributes.getNamedItem("account").getNodeValue();
        String amount = attributes.getNamedItem("amount").getNodeValue();

        Operation operation = new Operation(Float.parseFloat(amount),
                Integer.parseInt(accountKey),
                HbCompat.julianToDate(Long.parseLong(date))
        );

        Node category = attributes.getNamedItem("category");
        if (category != null) {
            operation.setCategoryKey(Integer.parseInt(category.getNodeValue()));
        }

        return operation;
    }
}
