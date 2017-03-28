package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.homebank.Account;
import budget.homebank.monsieur_h.homebudget.homebank.XHB;

public class AccountListAdapter implements ListAdapter {
    private final XHB xhb;
    private final int month;
    private final LayoutInflater inflater;

    public AccountListAdapter(Context context, XHB xhb, int month) {
        this.xhb = xhb;
        this.month = month;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = xhb.getAccounts().get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.account_row, null);
        }
        TextView accountName = (TextView) convertView.findViewById(R.id.account_name);
        accountName.setText(account.getName());

        TextView subtext = (TextView) convertView.findViewById(R.id.subtext);

        String futureAmount = String.format(xhb.getDefaultCurrency().getFormat(), account.getFutureAmount());
        String bankAmount = String.format(xhb.getDefaultCurrency().getFormat(), account.getBankAmount());

        subtext.setText(String.format("Futur: %s Banque : %s", futureAmount, bankAmount));
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
        return xhb.getAccounts().size();
    }

    @Override
    public Object getItem(int position) {
        return xhb.getAccounts().get(position);
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
