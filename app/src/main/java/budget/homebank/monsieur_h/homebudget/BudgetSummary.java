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
import com.dropbox.chooser.android.DbxChooser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class BudgetSummary extends AppCompatActivity {


    static final int DBX_CHOOSE_FILE_REQUEST = 0;  // You can change this if needed
    private static final String DBX_APP_KEY = "ktvth6u26gs18v4";
    private static final String DBX_APP_SECRET = "jwed8hoj12jldlv";
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSE_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                Log.d("main", "Link to selected file: " + result.getLink());
            }
        } else if (requestCode == LOCAL_CHOOSE_FILE_REQUEST) {
            Uri fileUri = data.getData();
            Log.d("main", "Found file " + fileUri);
            try {
//     FileInputStream inputStream = new FileProvider().openAssetFile(fileUri, "").createInputStream();

                InputStream fileInputStream = getApplicationContext().getContentResolver().openInputStream(fileUri);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(false);
                dbf.setValidating(false);
                Document parse = dbf.newDocumentBuilder().parse(fileInputStream);
                NodeList categories = parse.getElementsByTagName("cat");
                for (int i = 0; i < categories.getLength(); i++) {
                    NamedNodeMap attrs = categories.item(i).getAttributes();
                    Log.d("CAT", "debug: " + categories.item(i).getNodeValue());
                    for (int j = 0; j < attrs.getLength(); j++) {
                        Log.d("CAT", "Category debug: " + attrs.item(i).getNodeValue());
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
            mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(BudgetSummary.this, DBX_CHOOSE_FILE_REQUEST);
            return true;
        }

        if (id == R.id.link_local_file) {
            Intent getXhbFileIntent = new Intent();
            getXhbFileIntent.setAction(Intent.ACTION_GET_CONTENT);
            getXhbFileIntent.setType("*/*");
            try {
                startActivityForResult(getXhbFileIntent, LOCAL_CHOOSE_FILE_REQUEST);
            } catch (ActivityNotFoundException e) {

                Snackbar.make(this.findViewById(R.id.app_bar), "No file manager", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
