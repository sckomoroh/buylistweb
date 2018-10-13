package com.gherasoft.onlineList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gherasoft.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 1/16/13
 * Time: 9:44 AM
 */
public class OnlineListAdapter extends BaseAdapter {
    private List<String> mLists = new ArrayList<String>();
    private Context mContext;

    public OnlineListAdapter(Context context)
    {
        mContext = context;
    }

    public void clear()
    {
        mLists.clear();
    }

    public void addList(String listName)
    {
        mLists.add(listName);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.online_list_item, null);

        TextView textView = (TextView) view.findViewById(R.id.listItemTextView);
        textView.setText(mLists.get(i));

        return view;
    }
}
