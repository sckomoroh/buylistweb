package com.gherasoft.buyList.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 1/15/13
 * Time: 10:28 AM
 */
public class GroupModel {
    private List<ItemModel> mItems  = new ArrayList<ItemModel>();
    private String mName;

    public GroupModel(String name)
    {
        mName = name;
    }

    public GroupState getGroupState()
    {
        boolean hasChecked = false;
        boolean hasUnchecked = false;

        for (ItemModel itemModel : mItems)
        {
            if (itemModel.isChecked())
            {
                hasChecked = true;
            }
            else
            {
                hasUnchecked = true;
            }
        }

        if (hasChecked && hasUnchecked)
        {
            return GroupState.SomeCompleted;
        }

        if (hasChecked)
        {
            return GroupState.Completed;
        }

        return GroupState.Uncompleted;
    }

    public List<ItemModel> getItems()
    {
        return mItems;
    }

    public String getName()
    {
        return mName;
    }

    public void addItem(String name)
    {
        mItems.add(new ItemModel(name));
    }

    public ItemModel getItem(int index)
    {
        return mItems.get(index);
    }

    public int getItemsCount()
    {
        return mItems.size();
    }
}
