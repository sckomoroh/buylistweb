package com.ghyrosoft.client.lists.explorelist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/30/13
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class ListModel
{
    private List<String> items = new ArrayList<String>();
    private ListModelListener listener;

    public void setListener(ListModelListener listener)
    {
        this.listener = listener;
    }

    public void addItem(String item)
    {
        items.add(item);

        fireAddItem(items.size() - 1);
    }

    public void removeItem(String item)
    {
        int index = items.indexOf(item);
        items.remove(index);

        fireRemoveItem(index);
    }

    public void removeItem(int index)
    {
        items.remove(index);

        fireRemoveItem(index);
    }

    public String getItem(int index)
    {
        return items.get(index);
    }

    public int itemCount()
    {
        return items.size();
    }

    public void clear()
    {
        items.clear();

        fireClear();
    }

    private void fireAddItem(int index)
    {
        if (listener != null)
        {
            listener.onAddItem(index);
        }
    }

    private void fireRemoveItem(int index)
    {
        if (listener != null)
        {
            listener.onRemoveItem(index);
        }
    }

    private void fireClear()
    {
        if (listener != null)
        {
            listener.onClear();
        }
    }
}
