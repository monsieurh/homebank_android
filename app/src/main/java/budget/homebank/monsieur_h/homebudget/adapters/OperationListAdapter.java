package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.homebank.Category;
import budget.homebank.monsieur_h.homebudget.homebank.Operation;
import budget.homebank.monsieur_h.homebudget.homebank.OperationFlags;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperationListAdapter implements ListAdapter {
    private final Category category;
    private final LayoutInflater inflater;
    private final String strFmt;
    private int month;

    public OperationListAdapter(Context context, Category category, int month) {
        this.category = category;
        this.month = month;
        inflater = LayoutInflater.from(context);
        strFmt = category.getXhb().getDefaultCurrency().getFormat();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation operation = (Operation) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_row_operation, null);
        }

        TextView operation_account_name = (TextView) convertView.findViewById(R.id.operation_account_name);
        operation_account_name.setText(operation.getAccount().getName());

        TextView text = (TextView) convertView.findViewById(R.id.operation_payee);
        text.setText(operation.getPayee().getName());

        TextView balanceView = (TextView) convertView.findViewById(R.id.operation_balance);
        balanceView.setText(String.format(strFmt, operation.getAmount()));

        TextView dateView = (TextView) convertView.findViewById(R.id.operation_date);
        dateView.setText(SimpleDateFormat.getDateInstance().format(operation.getDate()));


        TextView wordingView = (TextView) convertView.findViewById(R.id.operation_wording);
        wordingView.setText(operation.getWording());

        Date currentDate = new Date();
        if (operation.getDate().compareTo(currentDate) > 0 || operation.getStatus() != OperationFlags.Status.TXN_STATUS_RECONCILED) {
            ImageView icon = (ImageView) convertView.findViewById(R.id.operation_status_icon);
            icon.setImageResource(R.mipmap.ope_cleared);
        }
        return convertView;
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
        return category.getOperations().size();
    }

    @Override
    public Object getItem(int position) {
        return category.getOperations().get(position);
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
    public boolean isEmpty() {
        return getCount() == 0;
    }
}
