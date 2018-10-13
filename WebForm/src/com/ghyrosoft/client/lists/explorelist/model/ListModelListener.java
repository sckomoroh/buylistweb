package com.ghyrosoft.client.lists.explorelist.model;

/**
 * User: ezvigunov
 * Date: 1/30/13
 * Time: 8:09 PM
 */
public interface ListModelListener
{
    public void onAddItem(int index);

    public void onRemoveItem(int index);

    public void onClear();
}
