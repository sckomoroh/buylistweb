package com.gherasoft.buyList.view;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.gherasoft.R;
import com.gherasoft.buyList.adapter.ExpListAdapter;
import com.gherasoft.buyList.adapter.ItemListener;
import com.gherasoft.buyList.adapter.ModelListener;
import org.w3c.dom.Element;

/**
 * User: sckomoroh
 * Date: 1/16/13
 * Time: 10:12 AM
 */
public class BuyListPage implements ItemListener, ModelListener {
    private TextView mCompletedTextView;
    private TextView mUncompletedTextView;
    private ExpListAdapter mAdapter;
    private ExpandableListView mListView;
    private TextView mListNameTextView;

    public static final int INDEX = 1;

    public BuyListPage(View view, Context context) {
        mListView = (ExpandableListView) view.findViewById(R.id.exListView);

        mCompletedTextView = (TextView) view.findViewById(R.id.completedTextView);
        mUncompletedTextView = (TextView) view.findViewById(R.id.uncompletedTextView);
        mListNameTextView = (TextView) view.findViewById(R.id.listNameTextView);

        mAdapter = new ExpListAdapter(context);
        mAdapter.setModelListener(this);
        mAdapter.setItemListener(this);

        mListView.setAdapter(mAdapter);
    }

    @Override
    public void checkCountChanged(int checked, int unchecked) {
        mCompletedTextView.setText("" + checked);
        mUncompletedTextView.setText("" + unchecked);
    }

    @Override
    public void modelRebuilded(int itemCount, String listName) {
        mCompletedTextView.setText("0");
        mUncompletedTextView.setText("" + itemCount);
        mListNameTextView.setText(listName);
    }

    public void expandAll() {
        int groupsCount = mAdapter.getGroupCount();
        for (int i = 0; i < groupsCount; i++) {
            mListView.expandGroup(i);
        }
    }

    public void collapseAll() {
        int groupsCount = mAdapter.getGroupCount();
        for (int i = 0; i < groupsCount; i++) {
            mListView.collapseGroup(i);
        }
    }

    public void setXmlContent(Element rootElement)
    {
        mAdapter.setXmlContent(rootElement);
    }
}
