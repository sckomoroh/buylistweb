package com.gherasoft.buyList.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.gherasoft.R;
import org.w3c.dom.Element;

/**
 * User: sckomoroh
 * Date: 1/15/13
 * Time: 9:58 AM
 */
public class ExpListAdapter extends BaseExpandableListAdapter
{

    private Model mModel = new Model();
    private Context mContext;
    private ItemListener mItemListener;

    public ExpListAdapter(Context context)
    {
        mContext = context;
    }

    public void setXmlContent(Element rootElement)
    {
        mModel.buildModel(rootElement);
        notifyDataSetChanged();
    }

    public void setItemListener(ItemListener listener)
    {
        mItemListener = listener;
    }

    public void setModelListener(ModelListener listener)
    {
        mModel.setListener(listener);
    }

    @Override
    public int getGroupCount()
    {
        return mModel.groupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mModel.getGroup(groupPosition).getItemsCount();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mModel.getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mModel.getGroup(groupPosition).getItem(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent)
    {
        GroupModel groupModel = mModel.getGroup(groupPosition);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.group_view, null);

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(groupModel.getName());

        int groupColor = Color.GREEN;

        if (groupModel.getGroupState() == GroupState.Uncompleted)
        {
            groupColor = Color.RED;
        } else if (groupModel.getGroupState() == GroupState.SomeCompleted)
        {
            groupColor = Color.YELLOW;
        }

        textGroup.setTextColor(groupColor);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent)
    {
        final int groupIndex = groupPosition;
        final int childIndex = childPosition;

        ItemModel itemModel = mModel.getGroup(groupPosition).getItem(childPosition);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.child_view, null);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.textChild);
        checkBox.setText(itemModel.getName());
        checkBox.setChecked(itemModel.isChecked());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                mModel.getGroup(groupIndex).getItem(childIndex).setChecked(b);
                ExpListAdapter.this.notifyDataSetInvalidated();

                if (mItemListener != null)
                {
                    int checked = 0;
                    int unchecked = 0;

                    for (GroupModel groupModel : mModel.getGroups())
                    {
                        for (ItemModel itemModel : groupModel.getItems())
                        {
                            if (itemModel.isChecked())
                            {
                                checked++;
                            } else
                            {
                                unchecked++;
                            }
                        }
                    }

                    mItemListener.checkCountChanged(checked, unchecked);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}