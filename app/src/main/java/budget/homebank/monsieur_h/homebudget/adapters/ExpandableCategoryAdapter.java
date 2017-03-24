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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExpandableCategoryAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private final int month;
    private final String strFmt;
    private List<Category> groupList;

    public ExpandableCategoryAdapter(Context context, List<Category> initialList, int month) {
        this.groupList = initialList;
        sortCategories();
        this.inflater = LayoutInflater.from(context);
        this.month = month;
        this.strFmt = initialList.size() > 0 ? initialList.get(0).getXhb().getDefaultCurrency().getFormat() : "%.2f";
    }

    private void sortCategories() {
        Comparator<Category> categoryComparator = new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
        Collections.sort(groupList, categoryComparator);

        for (Category cat : groupList) {
            List<Category> children = cat.getChildren();
            if (children.isEmpty())
                continue;

            Collections.sort(children, categoryComparator);
        }
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Category category = (Category) getGroup(i);
        float budget = category.getMonthlyBudget(month);
        float expense = category.getMonthlyExpense(month);
        float balance = (budget - expense);
        if (balance != 0) balance *= -1;
        if (view == null) {
            view = inflater.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(category.getName());

        TextView categoryHeading = (TextView) view.findViewById(R.id.heading_amount);
        String budgetAmount = String.format(strFmt, budget);
        String expenseAmount = String.format(strFmt, expense);
        String balanceStr = String.format(strFmt, balance);

        categoryHeading.setText(String.format(inflater.getContext().getResources().getString(R.string.balance_budget_expense), balanceStr, budgetAmount, expenseAmount));

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
        float balance = (budget - expense);
        if (balance != 0) balance *= -1;
        if (view == null) {
            view = inflater.inflate(R.layout.category_row, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.subcategory);
        sequence.setText(category.getName());

        TextView childItem = (TextView) view.findViewById(R.id.subcategory_balance);
        String balanceStr = String.format(strFmt, balance);
        String budgetAmount = String.format(strFmt, budget);
        String expenseAmount = String.format(strFmt, expense);
        childItem.setText(String.format(inflater.getContext().getResources().getString(R.string.balance_budget_expense), balanceStr, budgetAmount, expenseAmount));

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
    public boolean isChildSelectable(int i, int j) {
        Category child = (Category) getChild(i, j);
        return child.hasOperations();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public int getMonth() {
        return month;
    }
}
