package com.ghyrosoft.client.lists.buylist.model;

/**
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 7:31 PM
 */
public interface TreeModelListener
{
    public void onModelClear();

    public void onAddItem(int index);

    public void onAddSubItem(int index1, int index2);

    public void onRemoveItem(int index);

    public void onRemoveSubItem(int index1, int index2);

    public void onRenameItem(int index, String text);

    public void onRenameSubItem(int index1, int index2, String text);

    public void onListNameChanged(String listName);
}
