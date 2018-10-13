package com.gherasoft.onlineList.view;

import android.content.Context;
import android.view.View;
import android.widget.*;
import com.gherasoft.R;
import com.gherasoft.onlineList.adapter.OnlineListAdapter;
import com.gherasoft.onlineList.adapter.OnlineListHelper;
import org.w3c.dom.Document;

/**
 * User: sckomoroh
 * Date: 1/16/13
 * Time: 10:13 AM
 */
public class OnlineListPage implements AdapterView.OnItemClickListener {
    private OnlineListHelper mOnlineHelper;
    private OnlineListAdapter mAdapter;
    private OnlineListPageListener mListener;

    public static final int INDEX = 0;

    public OnlineListPage(View view, Context context) {
        final ListView onlineListView = (ListView) view.findViewById(R.id.onlineListView);

        onlineListView.setOnItemClickListener(this);

        mAdapter = new OnlineListAdapter(context);
        onlineListView.setAdapter(mAdapter);

        mOnlineHelper = new OnlineListHelper(mAdapter);
        mOnlineHelper.getLists();
    }

    public void setListener(OnlineListPageListener listener)
    {
        mListener = listener;
    }

    public void refreshLists()
    {
        mAdapter.clear();
        mOnlineHelper.getLists();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String listName = (String)mAdapter.getItem(i);
        Document doc = mOnlineHelper.getList(listName);

        if (mListener != null)
        {
            mListener.onContentLoaded(doc);
        }
    }
}
