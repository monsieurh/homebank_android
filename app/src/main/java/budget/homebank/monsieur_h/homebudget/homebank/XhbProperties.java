package budget.homebank.monsieur_h.homebudget.homebank;

import org.w3c.dom.Node;

import java.util.ArrayList;

public class XhbProperties {

    private final String homebank_version;
    private final String title;
    private final int defaultCurrencyCode;


    public XhbProperties(String homebank_version, String title, int defaultCurrencyCode, ArrayList<Currency> currencies) {

        this.homebank_version = homebank_version;
        this.title = title;
        this.defaultCurrencyCode = defaultCurrencyCode;
    }

    public String getTitle() {
        return title;
    }

    public int getDefaultCurrencyCode() {
        return defaultCurrencyCode;
    }

    public static class Builder {

        private String homebank_version;
        private String homebank_d;
        private String title;
        private int defaultCurrencyCode;
        private ArrayList<Currency> currencies;

        public Builder setHomebankNode(Node homebankNode) {
            homebank_version = homebankNode.getAttributes().getNamedItem("v").getNodeValue();
            homebank_d = homebankNode.getAttributes().getNamedItem("d").getNodeValue();
            return this;
        }

        public Builder setPropertiesNode(Node propertiesNode) {
            title = propertiesNode.getAttributes().getNamedItem("title").getNodeValue();
            defaultCurrencyCode = Integer.parseInt(propertiesNode.getAttributes().getNamedItem("curr").getNodeValue());
            return this;
        }

        public XhbProperties build() {
            return new XhbProperties(
                    homebank_version,
                    title,
                    defaultCurrencyCode,
                    currencies
            );
        }

    }
}
