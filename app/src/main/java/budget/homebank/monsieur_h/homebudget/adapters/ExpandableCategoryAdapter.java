package budget.homebank.monsieur_h.homebudget.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.Util;
import budget.homebank.monsieur_h.homebudget.homebank.Category;

import java.util.List;

public class ExpandableCategoryAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final int month;
    private List<Category> groupList;

    public ExpandableCategoryAdapter(Context context, List<Category> initialList, int month) {
        this.groupList = initialList;
        this.inflater = LayoutInflater.from(context);
        this.month = month;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Category category = (Category) getGroup(i);
        float budget = category.getMonthlyBudget(month);
        float expense = category.getMonthlyExpense(month);
        float whatsLeft = (budget - expense) * -1;

        if (view == null) {
            view = inflater.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(category.getName());

        TextView amount = (TextView) view.findViewById(R.id.heading_amount);
        amount.setText(String.format(Util.CURRENCY_FMT, whatsLeft));

        ProgressBar bar = (ProgressBar) view.findViewById(R.id.progress);
        int progress;
        if (budget != 0) {
            progress = (int) (category.getMonthlyExpenseRatio(month) * Util.PROGRESS_PRECISION);
            progress = Math.abs(progress);
        } else {
            progress = expense == 0 ? 0 : 101;
        }
        bar.setProgress(progress);

        if (progress > 100) {
            bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            bar.getProgressDrawable().setColorFilter(Color.rgb(0, 200, 0), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean isLastChild, View view, ViewGroup viewGroup) {
        Category category = (Category) getChild(i, j);
        float budget = category.getMonthlyBudget(month);
        float expense = category.getMonthlyExpense(month);
        float whatsLeft = (budget - expense) * -1;
        if (view == null) {
            view = inflater.inflate(R.layout.category_row, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.subcategory);
        sequence.setText(category.getName());

        TextView childItem = (TextView) view.findViewById(R.id.subcategory_whatsleft);
        childItem.setText(String.format(Util.CURRENCY_FMT, whatsLeft));

        ProgressBar bar = (ProgressBar) view.findViewById(R.id.child_progress);
        int progress;
        if (budget != 0) {
            progress = (int) (category.getMonthlyExpenseRatio(month) * Util.PROGRESS_PRECISION);
            progress = Math.abs(progress);
        } else {
            progress = expense == 0 ? 0 : 101;
        }
        bar.setProgress(progress);

        if (progress > 100) {
            bar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            bar.getProgressDrawable().setColorFilter(Color.rgb(0, 200, 0), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        return view;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).getChildren().size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int j) {
        return groupList.get(i).getChildren().get(j);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public int getMonth() {
        return month;
    }
}
