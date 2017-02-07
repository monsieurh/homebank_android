package budget.homebank.monsieur_h.homebudget;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import com.dropbox.chooser.android.DbxChooser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BudgetSummaryActivity extends AppCompatActivity implements OnClickListener {


    static final int DBX_CHOOSE_FILE_REQUEST = 0;  // You can change this if needed
    private static final String DBX_APP_KEY = "ktvth6u26gs18v4";
    private static final String DBX_APP_SECRET = "jwed8hoj12jldlv";
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;
    private final CategoryMapper categoryMapper = new CategoryMapper();
    private final OperationFactory operationFactory = new OperationFactory();
    private ExpandableListView expandableListView;
    private MyViewAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.myList);
//        expandableListView.setGroupIndicator(null);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSE_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            DbxChooser.Result result = new DbxChooser.Result(data);
            try {
                parseFile(result.getLink());
                updateView();
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
            Log.d("main", "Link to selected file: " + result.getLink());
        } else if (requestCode == LOCAL_CHOOSE_FILE_REQUEST && resultCode == RESULT_OK) {
            Uri fileUri = data.getData();
            Log.d("main", "Found file " + fileUri);
            try {
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
        listAdapter = new MyViewAdapter(BudgetSummaryActivity.this, categoryMapper.getTopLevelCategoriesForMonth(2));
        expandableListView.setAdapter(listAdapter);
//        expandAll();
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
        InputStream fileInputStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);
        dbf.setValidating(false);

        Document doc = dbf.newDocumentBuilder().parse(fileInputStream);
        NodeList categories = doc.getElementsByTagName("cat");
        for (int i = 0; i < categories.getLength(); i++) {
            categoryMapper.addFromNode(categories.item(i));
        }
        categoryMapper.linkParents();

        NodeList operations = doc.getElementsByTagName("ope");
        List<Operation> operationList = new ArrayList<>();
        for (int i = 0; i < operations.getLength(); i++) {
            operationList.add(operationFactory.fromNode(operations.item(i)));
        }
        categoryMapper.addOperations(operationList);
        Log.d("CAT", categoryMapper.toString());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.link_dropbox_file) {
            DbxChooser mChooser = new DbxChooser(DBX_APP_KEY);
            mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(BudgetSummaryActivity.this, DBX_CHOOSE_FILE_REQUEST);
            return true;
        }

        if (id == R.id.link_local_file) {
            Intent getXhbFileIntent = new Intent();
            getXhbFileIntent.setAction(Intent.ACTION_GET_CONTENT);
            getXhbFileIntent.setType("*/*");
            try {
                startActivityForResult(getXhbFileIntent, LOCAL_CHOOSE_FILE_REQUEST);
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
