package com.gherasoft.buyList.adapter;

/**
 * User: sckomoroh
 * Date: 1/15/13
 * Time: 10:28 AM
 */
public class ItemModel {
    private String mName;
    private boolean mIsChecked;

    public ItemModel(String name)
    {
        mName = name;
    }

    public String getName()
    {
        return mName;
    }

    public boolean isChecked()
    {
        return mIsChecked;
    }

    public void setChecked(boolean checked)
    {
        mIsChecked = checked;
    }
}
