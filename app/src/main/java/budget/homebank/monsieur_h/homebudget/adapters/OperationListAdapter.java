package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.Util;
import budget.homebank.monsieur_h.homebudget.homebank.Category;
import budget.homebank.monsieur_h.homebudget.homebank.Operation;

import java.text.SimpleDateFormat;

public class OperationListAdapter implements ListAdapter {
    private final Category category;
    private final LayoutInflater inflater;
    private int month;

    public OperationListAdapter(Context context, Category category, int month) {
        this.category = category;
        this.month = month;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Operation operation = (Operation) getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_row_operation, null);
        }
        TextView text = (TextView) convertView.findViewById(R.id.operation_payee);
        text.setText(operation.getPayee().getName());

        TextView balanceView = (TextView) convertView.findViewById(R.id.operation_balance);
        balanceView.setText(String.format(Util.CURRENCY_FMT, operation.getAmount()));

        TextView dateView = (TextView) convertView.findViewById(R.id.operation_date);
        dateView.setText(SimpleDateFormat.getDateInstance().format(operation.getDate()));


        TextView wordingView = (TextView) convertView.findViewById(R.id.operation_wording);
        wordingView.setText(operation.getWording());
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
