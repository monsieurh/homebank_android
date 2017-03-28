package budget.homebank.monsieur_h.homebudget.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.ExpandableCategoryAdapter;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;

public class BudgetSummaryActivity extends AppCompatActivity implements OnClickListener {


    private static final int DBX_CHOOSE_FILE_REQUEST = 0;  // You can change this if needed
    private static final String DBX_APP_KEY = "ljtfuzjpqye9hne";
    private static final int LOCAL_CHOOSE_FILE_REQUEST = 2;
    private static final int PERMISSION_CUSTOM_CODE = 16;
    private XHB xhb = new XHB();
    private ExpandableListView expandableListView;
    private ExpandableCategoryAdapter listAdapter;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_budget_summary, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
    }
}
