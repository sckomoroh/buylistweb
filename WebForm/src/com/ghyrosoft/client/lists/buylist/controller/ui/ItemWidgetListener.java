package com.ghyrosoft.client.lists.buylist.controller.ui;

/**
 * User: sckomoroh
 * Date: 1/26/13
 * Time: 5:41 PM
  */
public interface ItemWidgetListener
{
    public void onAddButtonClick(int index);

    public void onEditButtonClick(String text, int index);

    public void onRemoveButtonClick(int index);
}
