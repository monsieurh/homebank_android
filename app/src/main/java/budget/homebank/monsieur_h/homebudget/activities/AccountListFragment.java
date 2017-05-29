package budget.homebank.monsieur_h.homebudget.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.AccountListAdapter;

public class AccountListFragment extends Fragment {

    private ListView accountListView;
    private AccountListAdapter accountListAdapter;
    private View rootView;
    private Parcelable previousListViewState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.account_list, container, false);
        if (previousListViewState == null) {
            accountListView = (ListView) rootView.findViewById(R.id.account_list);
        } else {
            accountListView.onRestoreInstanceState(previousListViewState);
            Log.d("PARCEL", "Restored account list");
        }
        accountListAdapter = new AccountListAdapter(getActivity(), HomeActivity.xhb, HomeActivity.CURRENT_MONTH);
        accountListView.setAdapter(accountListAdapter);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        previousListViewState = accountListView.onSaveInstanceState();
        Log.d("PARCEL", "Saved state");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (previousListViewState != null) {
            accountListView.onRestoreInstanceState(previousListViewState);
            Log.d("PARCEL", "Restored state");
        }
    }
}
