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
    private Context context;
    private List<Category> groupList;

    public MyViewAdapter(Context context, List<Category> initialList) {
        this.context = context;
        this.groupList = initialList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
//            view = inflater.inflate(R.id.category_row, null);
//        }
//        final Category item = (Category) getGroup(i);
//        TextView textView = (TextView) view;
//        textView.setText(item.toString());
//        return view;
        Category cat = (Category) getGroup(i);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_heading, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.heading);
        heading.setText(cat.toString());

        return view;
    }

    @Override
    public View getChildView(int i, int j, boolean isLastChild, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            view = inflater.inflate(R.id.item_row, null);
//        }
//        final Category item = (Category) getGroup(i);
//        TextView textView = (TextView) view;
//        textView.setText(item.toString());

        Operation operation = (Operation) getChild(i, j);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_row, null);
        }

        TextView sequence = (TextView) view.findViewById(R.id.sequence);
        sequence.setText(operation.toString());
        TextView childItem = (TextView) view.findViewById(R.id.childitem);
        childItem.setText("ChildItem");

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

    private static final class ViewHolder {
        TextView textLabel;
    }
}
