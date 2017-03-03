package budget.homebank.monsieur_h.homebudget.factories;

import android.app.Activity;
import android.net.Uri;
import budget.homebank.monsieur_h.homebudget.homebank.Currency;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;
import budget.homebank.monsieur_h.homebudget.homebank.XhbProperties;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class XhbFileParser {
    private static final String SHAREDPREFS_ID = "HOMEBANK";
    private static final String LAST_FILE_KEY = "LAST_FILE";
    private final static OperationFactory operationFactory = new OperationFactory();
    private final static PayeeFactory payeeFactory = new PayeeFactory();
    private final static AccountFactory accountFactory = new AccountFactory();
    private final static CategoryFactory categoryFactory = new CategoryFactory();

    public static XHB parse(Activity activity) throws SAXException, IOException, ParserConfigurationException {
        InputStream inputStream = activity.getContentResolver().openInputStream(getSaveFileUri(activity));
        XHB history = new XHB();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);

        Document doc = dbf.newDocumentBuilder().parse(inputStream);
        NodeList categories = doc.getElementsByTagName("cat");
        for (int i = 0; i < categories.getLength(); i++) {
            history.addCategory(categoryFactory.fromNode(categories.item(i)));
        }

        NodeList operations = doc.getElementsByTagName("ope");
        for (int i = 0; i < operations.getLength(); i++) {
            history.addOperation(operationFactory.fromNode(operations.item(i)));
        }

        NodeList payees = doc.getElementsByTagName("pay");
        for (int i = 0; i < payees.getLength(); i++) {
            history.addPayee(payeeFactory.fromNode(payees.item(i)));
        }

        NodeList accounts = doc.getElementsByTagName("account");
        for (int i = 0; i < accounts.getLength(); i++) {
            history.addAccount(accountFactory.fromNode(accounts.item(i)));
        }


        NodeList currencies = doc.getElementsByTagName("cur");
        for (int i = 0; i < currencies.getLength(); i++) {
            history.addCurrency(Currency.fromNode(currencies.item(i)));
        }

        XhbProperties props = new XhbProperties.Builder()
                .setPropertiesNode(doc.getElementsByTagName("properties").item(0))
                .setHomebankNode(doc.getElementsByTagName("homebank").item(0))
                .build();
        history.setProperties(props);

        history.bindAll();
        return history;
    }

    public static XHB parseLastfile(Activity activity) throws IOException, ParserConfigurationException, SAXException {
        return parse(activity);
    }

    private static Uri getSaveFileUri(Activity activity) {
        return Uri.parse(
                activity.getSharedPreferences(SHAREDPREFS_ID, MODE_PRIVATE).getString(LAST_FILE_KEY, "")
        );
    }

    public static void setSaveFileUri(Activity activity, Uri fileUri) {
        activity.getSharedPreferences(SHAREDPREFS_ID, MODE_PRIVATE).edit().putString(LAST_FILE_KEY, fileUri.toString()).apply();
    }
}
