package budget.homebank.monsieur_h.homebudget.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.SectionsPagerAdapter;
import budget.homebank.monsieur_h.homebudget.factories.XhbFileParser;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;
import com.dropbox.chooser.android.DbxChooser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private static final int DBX_CHOOSE_FILE_REQUEST = 0;  // You can change this if needed
    private static final String DBX_APP_KEY = "ljtfuzjpqye9hne";
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;
    private static final int PERMISSION_CUSTOM_CODE = 16;
    public static XHB xhb = new XHB();
    static int CURRENT_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    private Uri fileUri;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        checkPerms();

        try {
            xhb = XhbFileParser.parseLastfile(this);
            Log.d("DEBUG", "Parsed last file automatically");
        } catch (SAXException | IOException | ParserConfigurationException | SecurityException e) {
            e.printStackTrace();
        }
    }


    private void checkPerms() {
        if (hasPerms()) {
            Log.e("PERMS", "Missing permissions");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.MANAGE_DOCUMENTS
                    },
                    PERMISSION_CUSTOM_CODE
            );
        }
    }

    /*
    todo: apparently, permission.MANAGE_DOCUMENTS can't be persisted nor granted;
    causing a permission request on every app startup. Should investigate
     */
    private boolean hasPerms() {
        int perms = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        perms |= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        perms |= ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS);
        return perms != PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_summary, menu);
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCAL_CHOOSE_FILE_REQUEST && resultCode == RESULT_OK) {
            int flags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
            fileUri = data.getData();
            getContentResolver().takePersistableUriPermission(fileUri, flags);
            onFileSelected();
        }
        if (requestCode == DBX_CHOOSE_FILE_REQUEST && resultCode == RESULT_OK) {
            DbxChooser.Result result = new DbxChooser.Result(data);
            fileUri = result.getLink();

            onFileSelected();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onFileSelected() {
        try {
            XhbFileParser.setSaveFileUri(this, fileUri);
            xhb = XhbFileParser.parse(this);
            updateView();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


    private void updateView() {
        if (xhb != null) {
            setTitle(xhb.getProperties().getTitle());
        }
        mSectionsPagerAdapter.initFragments();
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
            mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT).launch(HomeActivity.this, DBX_CHOOSE_FILE_REQUEST);
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

}
