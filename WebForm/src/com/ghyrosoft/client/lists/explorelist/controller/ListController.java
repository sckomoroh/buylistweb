package com.ghyrosoft.client.lists.explorelist.controller;

import com.ghyrosoft.client.lists.explorelist.controller.ui.ListItem;
import com.ghyrosoft.client.lists.explorelist.controller.ui.ListItemListener;
import com.ghyrosoft.client.lists.explorelist.model.ListModel;
import com.ghyrosoft.client.lists.explorelist.model.ListModelListener;
import com.google.gwt.user.client.ui.FlexTable;

public class ListController implements ListModelListener
{
    private final FlexTable widget;
    private final ListModel model;
    private ListControllerListener listener;

    public ListController(FlexTable widget)
    {
        this.widget = widget;
        this.model = new ListModel();
        this.model.setListener(this);
    }

    public void setListener(ListControllerListener listener)
    {
        this.listener = listener;
    }

    public FlexTable getView()
    {
        return this.widget;
    }

    public ListModel getModel()
    {
        return this.model;
    }

    @Override
    public void onAddItem(int index) {
        ListItem item = new ListItem(this.model.getItem(index), index);
        item.setListener(new OnListItemListener());
        this.widget.setWidget(index, 0, item);
    }

    @Override
    public void onRemoveItem(int index) {
        this.widget.removeRow(index);
        rebuildIndexes();
    }

    @Override
    public void onClear() {
        this.widget.removeAllRows();
    }

    private void rebuildIndexes()
    {
        int itemCount = widget.getRowCount();
        for (int i=0; i<itemCount; i++)
        {
            ListItem listItem = (ListItem)widget.getWidget(i, 0);
            listItem.setIndex(i);
        }
    }

    private class OnListItemListener implements ListItemListener
    {
        @Override
        public void onRemoveButtonClick(int index)
        {
            if (listener != null)
            {
                listener.onRemoveItemClick(index);
            }
        }

        @Override
        public void onItemClick(int index)
        {
            if (listener != null)
            {
                listener.onListItemClick(index);
            }
        }
    }
}