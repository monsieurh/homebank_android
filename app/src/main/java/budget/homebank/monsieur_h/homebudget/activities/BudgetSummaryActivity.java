package budget.homebank.monsieur_h.homebudget.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import budget.homebank.monsieur_h.homebudget.factories.XhbFileParser;
import budget.homebank.monsieur_h.homebudget.homebank.Category;
import budget.homebank.monsieur_h.homebudget.homebank.HomebankHistory;
import com.dropbox.chooser.android.DbxChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Calendar;

public class BudgetSummaryActivity extends AppCompatActivity implements OnClickListener {


    static final int DBX_CHOOSE_FILE_REQUEST = 0;  // You can change this if needed
    private static final String DBX_APP_KEY = "ljtfuzjpqye9hne";
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;
    private static final int PERMISSION_CUSTOM_CODE = 16;
    private HomebankHistory HISTORY = new HomebankHistory();
    private ExpandableListView expandableListView;
    private ExpandableCategoryAdapter listAdapter;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_category_list);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Category clickedCategory = (Category) listAdapter.getGroup(groupPosition);
                if (clickedCategory.hasChildren() || !clickedCategory.hasOperations()) {
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
                if (!child.hasOperations()) {
                    return false;
                }
                Intent intent = new Intent(getBaseContext(), OperationListActivity.class);
                intent.putExtra("CATEGORY_KEY", child.getKey());
                intent.putExtra("MONTH", listAdapter.getMonth());
                startActivity(intent);
                return true;
            }
        });

        checkPerms();

        try {
            HISTORY = XhbFileParser.parseLastfile(this);
            updateView();
            Log.d("DEBUG", "Parsed last file automatically");
        } catch (SAXException | IOException | ParserConfigurationException | SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean ok = true;
        for (int p : grantResults) {
            ok = ok && (p == PackageManager.PERMISSION_GRANTED);
        }
        if (!ok) {
            Log.e("PERMS", "User refused perms, should display error message");
            Snackbar.make(findViewById(R.id.toolbar), "Perms error ?", Snackbar.LENGTH_SHORT);
        }
    }

    private boolean hasPerms() {
        int perms = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        perms |= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        perms |= ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS);
        return perms != PackageManager.PERMISSION_GRANTED;
    }

    private void checkPerms() {
        if (hasPerms()) {
            Log.e("PERMS", "Missing permissions");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.MANAGE_DOCUMENTS
                    },
                    PERMISSION_CUSTOM_CODE
            );
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
                HISTORY = XhbFileParser.parse(this.getContentResolver().openInputStream(fileUri));
                updateView();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == DBX_CHOOSE_FILE_REQUEST && resultCode == RESULT_OK) {
            DbxChooser.Result result = new DbxChooser.Result(data);
            fileUri = result.getLink();

            try {//todo:duplicate code refactor
                getPreferences(MODE_PRIVATE).edit().putString("lastFile", fileUri.toString()).apply();
                HISTORY = XhbFileParser.parse(this.getContentResolver().openInputStream(fileUri));
                updateView();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        }


    }

    private void updateView() {
        int month = Calendar.getInstance().getTime().getMonth();
        Log.d("MONTH", "" + month);
        listAdapter = new ExpandableCategoryAdapter(BudgetSummaryActivity.this, HISTORY.getTopCategoriesForMonthlyBudget(month), month);
        expandableListView.setAdapter(listAdapter);
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

        if (id == R.id.about) {
            Intent about = new Intent(getBaseContext(), AboutActivity.class);
            startActivity(about);
        }

        if (id == R.id.link_dropbox_file) {
            DbxChooser mChooser = new DbxChooser(DBX_APP_KEY);
            mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(BudgetSummaryActivity.this, DBX_CHOOSE_FILE_REQUEST);
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
