package com.ghyrosoft.client.lists.buylist.controller.ui;

/**
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 5:29 PM
 */
public interface SubItemWidgetListener
{
    public void onEditButtonClick(String text, int parentIndex, int index);

    public void onRemoveButtonClick(int parentIndex, int index);
}
