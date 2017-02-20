package budget.homebank.monsieur_h.homebudget.factories;

import budget.homebank.monsieur_h.homebudget.homebank.HbCompat;
import budget.homebank.monsieur_h.homebudget.homebank.Operation;
import budget.homebank.monsieur_h.homebudget.homebank.OperationFlags;
import budget.homebank.monsieur_h.homebudget.homebank.SubOperation;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

        if (operation.hasFlag(OperationFlags.OF_SPLIT)) {
            List<SubOperation> split = createSplitOperation(attributes);
            operation.setSubOperations(split);
        }

        return operation;
    }

    private List<SubOperation> createSplitOperation(NamedNodeMap node) {
        List<SubOperation> subOperations = new ArrayList<>();
        String splitPattern = Pattern.quote("||");

        String[] splitAmount = node.getNamedItem("samt").getNodeValue().split(splitPattern);
        String[] splitCategoryKey = node.getNamedItem("scat").getNodeValue().split(splitPattern);

        String[] splitMemo = new String[splitAmount.length];
        if (node.getNamedItem("smem") != null) {
            splitMemo = node.getNamedItem("smem").getNodeValue().split(splitPattern);
        }

        for (int i = 0; i < splitAmount.length; i++) {
            subOperations.add(new SubOperation(
                    i < splitCategoryKey.length ? Integer.parseInt(splitCategoryKey[i]) : 0,
                    Float.parseFloat(splitAmount[i]),
                    i < splitMemo.length ? splitMemo[i] : ""
            ));
        }
        return subOperations;
    }
}
