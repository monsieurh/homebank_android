package budget.homebank.monsieur_h.homebudget.homebank;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class Currency {
    public final int key;
    public final String iso;
    public final String name;
    public final char symbol;
    public final char decimalChar;
    public final int decimalPrecision;

    public Currency(int key, String iso, String name, char symbol, char decimalChar, int decimalPrecision) {

        this.key = key;
        this.iso = iso;
        this.name = name;
        this.symbol = symbol;
        this.decimalChar = decimalChar;
        this.decimalPrecision = decimalPrecision;
    }

    public static Currency fromNode(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        return new Currency(
                Integer.parseInt(attributes.getNamedItem("key").getNodeValue()),
                attributes.getNamedItem("iso").getNodeValue(),
                attributes.getNamedItem("name").getNodeValue(),
                attributes.getNamedItem("symb").getNodeValue().charAt(0),
                attributes.getNamedItem("dchar").getNodeValue().charAt(0),
                Integer.parseInt(attributes.getNamedItem("frac").getNodeValue())
        );
    }

    public String getFormat() {
        return String.format("%%.%df %c", decimalPrecision, symbol);//todo check for thousand separator (',' in french)
    }
}
