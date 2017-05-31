package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.homebank.Account;
import budget.homebank.monsieur_h.homebudget.homebank.AccountFlags;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;

public class AccountListAdapter implements ListAdapter {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final int month;
    private final LayoutInflater inflater;
    private final Context context;

    public AccountListAdapter(Context context, XHB xhb, int month) {
        this.context = context;
        createAccountList(xhb);
        this.month = month;
        inflater = LayoutInflater.from(context);
    }

    public void createAccountList(XHB xhb) {
        for (Account account : xhb.getAccounts()) {
            if (account.hasFlag(AccountFlags.AF_CLOSED) || account.hasFlag(AccountFlags.AF_NOSUMMARY)) {
                continue;
            }
            accounts.add(account);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = accounts.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.account_row, null);
        }
        TextView accountName = (TextView) convertView.findViewById(R.id.account_name);
        accountName.setText(account.getName());

        TextView subtext = (TextView) convertView.findViewById(R.id.subtext);

        String futureAmount = String.format(account.getCurrency().getFormat(), account.getFutureAmount());
        String bankAmount = String.format(account.getCurrency().getFormat(), account.getBankAmount());
        String todayAmount = String.format(account.getCurrency().getFormat(), account.getTodayAmount());

        subtext.setText(String.format(context.getResources().getString(R.string.account_content), bankAmount, todayAmount, futureAmount));
        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
