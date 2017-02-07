package budget.homebank.monsieur_h.homebudget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

public class MyViewAdapter extends BaseExpandableListAdapter {
    private final LayoutInflater inflater;
    private List<Category> groupList;

    MyViewAdapter(Context context, List<Category> initialList) {
        this.groupList = initialList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Category cat = (Category) getGroup(i);
        if (view == null) {
            view = inflater.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(cat.getName());

        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean isLastChild, View view, ViewGroup viewGroup) {
        Category category = (Category) getChild(i, j);
        if (view == null) {
            view = inflater.inflate(R.layout.child_row, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.sequence);
        sequence.setText(category.getName());
        TextView childItem = (TextView) view.findViewById(R.id.childitem);
        childItem.setText("" + category.getKey());

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
}
