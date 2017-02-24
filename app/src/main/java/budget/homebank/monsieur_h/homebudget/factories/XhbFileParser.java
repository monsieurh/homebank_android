package budget.homebank.monsieur_h.homebudget.factories;

import android.app.Activity;
import android.net.Uri;
import budget.homebank.monsieur_h.homebudget.homebank.HomebankHistory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class XhbFileParser {
    private final static OperationFactory operationFactory = new OperationFactory();
    private final static PayeeFactory payeeFactory = new PayeeFactory();
    private final static AccountFactory accountFactory = new AccountFactory();
    private final static CategoryFactory categoryFactory = new CategoryFactory();

    public static HomebankHistory parse(InputStream fileInputStream) throws SAXException, IOException, ParserConfigurationException {
        HomebankHistory history = new HomebankHistory();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);

        Document doc = dbf.newDocumentBuilder().parse(fileInputStream);
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

        history.bindAll();
        return history;
    }

    public static HomebankHistory parseLastfile(Activity activity) throws IOException, ParserConfigurationException, SAXException {
        Uri lastFile = Uri.parse(activity.getPreferences(MODE_PRIVATE).getString("lastFile", ""));
        return parse(activity.getContentResolver().openInputStream(lastFile));
    }
}
