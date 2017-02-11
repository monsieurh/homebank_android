package budget.homebank.monsieur_h.homebudget.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.ExpandableCategoryAdapter;
import budget.homebank.monsieur_h.homebudget.factories.AccountFactory;
import budget.homebank.monsieur_h.homebudget.factories.CategoryFactory;
import budget.homebank.monsieur_h.homebudget.factories.OperationFactory;
import budget.homebank.monsieur_h.homebudget.factories.PayeeFactory;
import budget.homebank.monsieur_h.homebudget.homebank.Category;
import budget.homebank.monsieur_h.homebudget.homebank.HomebankMapper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class BudgetSummaryActivity extends AppCompatActivity implements OnClickListener {


    static final HomebankMapper HOMEBANK_MAPPER = new HomebankMapper();
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;
    private final OperationFactory operationFactory = new OperationFactory();
    private final PayeeFactory payeeFactory = new PayeeFactory();
    private final AccountFactory accountFactory = new AccountFactory();
    private final CategoryFactory categoryFactory = new CategoryFactory();
    private ExpandableListView expandableListView;
    private ExpandableCategoryAdapter listAdapter;
    private Uri fileUri;
    private boolean expanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.myList);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Category clickedCategory = (Category) listAdapter.getGroup(groupPosition);
                if (clickedCategory.hasChildren()) {
                    return false;
                }
                Intent intent = new Intent(getBaseContext(), OperationListActivity.class);
                intent.putExtra("CATEGORY_KEY", clickedCategory.getKey());
                intent.putExtra("MONTH", listAdapter.getMonth());
                startActivity(intent);
                return true;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category child = (Category) listAdapter.getChild(groupPosition, childPosition);
                Intent intent = new Intent(getBaseContext(), OperationListActivity.class);
                intent.putExtra("CATEGORY_KEY", child.getKey());
                intent.putExtra("MONTH", listAdapter.getMonth());
                startActivity(intent);
                return true;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (expanded) {
                    collapseAll();
                } else {
                    expandAll();
                }
                expanded = !expanded;
            }
        });

        try {
            Uri lastFile = Uri.parse(getPreferences(MODE_PRIVATE).getString("lastFile", ""));
            parseFile(lastFile);
            updateView();
            Log.d("DEBUG", "Parsed last file automatically");
        } catch (SAXException | IOException | ParserConfigurationException | SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCAL_CHOOSE_FILE_REQUEST && resultCode == RESULT_OK) {
            int flags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
//            getContentResolver().takePersistableUriPermission(fileUri, flags);
            fileUri = data.getData();
            try {
                getPreferences(MODE_PRIVATE).edit().putString("lastFile", fileUri.toString()).apply();
                parseFile(fileUri);
                updateView();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateView() {
        int month = Calendar.getInstance().getTime().getMonth();
        Log.d("MONTH", "" + month);
        listAdapter = new ExpandableCategoryAdapter(BudgetSummaryActivity.this, HOMEBANK_MAPPER.getTopCategoriesForMonthlyBudget(month), month);
        expandableListView.setAdapter(listAdapter);
    }


    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    private void parseFile(Uri fileUri) throws SAXException, IOException, ParserConfigurationException {
        InputStream fileInputStream = this.getContentResolver().openInputStream(fileUri);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);

        Document doc = dbf.newDocumentBuilder().parse(fileInputStream);
        NodeList categories = doc.getElementsByTagName("cat");
        for (int i = 0; i < categories.getLength(); i++) {
            HOMEBANK_MAPPER.addCategory(categoryFactory.fromNode(categories.item(i)));
        }

        NodeList operations = doc.getElementsByTagName("ope");
        for (int i = 0; i < operations.getLength(); i++) {
            HOMEBANK_MAPPER.addOperation(operationFactory.fromNode(operations.item(i)));
        }

        NodeList payees = doc.getElementsByTagName("pay");
        for (int i = 0; i < payees.getLength(); i++) {
            HOMEBANK_MAPPER.addPayee(payeeFactory.fromNode(payees.item(i)));
        }

        NodeList accounts = doc.getElementsByTagName("account");
        for (int i = 0; i < accounts.getLength(); i++) {
            HOMEBANK_MAPPER.addAccount(accountFactory.fromNode(accounts.item(i)));
        }

        HOMEBANK_MAPPER.bindAll();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_summary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.link_local_file) {
            Intent readFileIntent = new Intent();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                readFileIntent.setAction(Intent.ACTION_GET_CONTENT);

            } else {
                readFileIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                readFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
            }


            readFileIntent.setType("*/*");
            try {
                startActivityForResult(readFileIntent, LOCAL_CHOOSE_FILE_REQUEST);
            } catch (ActivityNotFoundException e) {

//                Snackbar.make(this.findViewById(R.id.app_bar), "No file manager", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
    }
}
