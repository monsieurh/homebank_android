package budget.homebank.monsieur_h.homebudget.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.adapters.ExpandableCategoryAdapter;
import budget.homebank.monsieur_h.homebudget.homebank.Category;

public class BudgetSummaryFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ExpandableCategoryAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_list, container, false);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandable_category_list);
        addOnClickListeners();
        refresh();
        return rootView;
    }

    private void addOnClickListeners() {
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category child = (Category) listAdapter.getChild(groupPosition, childPosition);
                if (!child.hasOperations()) {
                    return false;
                }
                Intent intent = new Intent(getActivity().getBaseContext(), OperationListActivity.class);
                intent.putExtra("CATEGORY_KEY", child.getKey());
                intent.putExtra("MONTH", listAdapter.getMonth());
                startActivity(intent);
                return true;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Category clickedCategory = (Category) listAdapter.getGroup(groupPosition);
                if (clickedCategory.hasChildren() || !clickedCategory.hasOperations()) {
                    return false;
                }
                Intent intent = new Intent(getActivity().getBaseContext(), OperationListActivity.class);
                intent.putExtra("CATEGORY_KEY", clickedCategory.getKey());
                intent.putExtra("MONTH", listAdapter.getMonth());
                startActivity(intent);
                return true;
            }
        });
    }

    public void refresh() {
        listAdapter = new ExpandableCategoryAdapter(
                getActivity(),
                HomeActivity.xhb.getTopCategoriesForMonthlyBudget(HomeActivity.CURRENT_MONTH),
                HomeActivity.CURRENT_MONTH
        );
        expandableListView.setAdapter(listAdapter);
    }
}
