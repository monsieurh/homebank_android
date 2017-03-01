package budget.homebank.monsieur_h.homebudget.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.OperationListAdapter;
import budget.homebank.monsieur_h.homebudget.factories.XhbFileParser;
import budget.homebank.monsieur_h.homebudget.homebank.Category;
import budget.homebank.monsieur_h.homebudget.homebank.Operation;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

public class OperationListActivity extends AppCompatActivity {

    private Category category;
    private ListView operationListView;
    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_list);
        int category_key = getIntent().getExtras().getInt("CATEGORY_KEY");
        month = getIntent().getExtras().getInt("MONTH");
        try {
            XHB h = XhbFileParser.parseLastfile(this);
            category = h.findCategory(category_key);
            category.filterForMonth(month);
            h.filterOutNoBudgetAccounts(category);

            setTitle(category.getName());
            operationListView = (ListView) findViewById(R.id.operation_list);

            Collections.sort(category.getOperations(), new Comparator<Operation>() {
                @Override
                public int compare(Operation o1, Operation o2) {
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
            operationListView.setAdapter(new OperationListAdapter(OperationListActivity.this, category, month));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
