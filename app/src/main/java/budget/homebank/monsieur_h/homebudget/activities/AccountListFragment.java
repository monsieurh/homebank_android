package budget.homebank.monsieur_h.homebudget.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.AccountListAdapter;

import java.util.Calendar;

public class AccountListFragment extends Fragment {

    private ListView accountListView;
    private AccountListAdapter accountListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_list, container, false);
        accountListView = (ListView) rootView.findViewById(R.id.account_list);
        accountListAdapter = new AccountListAdapter(getActivity(), HomeActivity.xhb, Calendar.getInstance().get(Calendar.MONTH));
        accountListView.setAdapter(accountListAdapter);
        return rootView;
    }
}
