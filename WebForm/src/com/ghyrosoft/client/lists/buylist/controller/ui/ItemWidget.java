package com.ghyrosoft.client.lists.buylist.controller.ui;

import com.ghyrosoft.client.lists.buylist.controller.ui.ItemWidgetListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class ItemWidget extends Composite
{
	private static final ItemWidgetUiBinder uiBinder = GWT.create(ItemWidgetUiBinder.class);

    private int modelIndex;
    private final List<ItemWidgetListener> listeners = new ArrayList<ItemWidgetListener>();

    @UiField
    protected Label itemName;

	interface ItemWidgetUiBinder extends UiBinder<Widget, ItemWidget>
	{
	}

	public ItemWidget(String text, int index)
	{
		initWidget(uiBinder.createAndBindUi(this));

        itemName.setText(text);
        modelIndex = index;
	}

    public int getModelIndex()
    {
        return modelIndex;
    }

    public void setModelIndex(int index)
    {
        modelIndex = index;
    }

    public void setText(String text)
    {
        itemName.setText(text);
    }

    public String getText()
    {
        return itemName.getText();
    }

    public void addListener(ItemWidgetListener listener)
    {
        listeners.add(listener);
    }

    public void removeListener(ItemWidgetListener listener)
    {
        listeners.remove(listener);
    }

    @UiHandler("addButton")
    protected void onAddButtonClick(ClickEvent event)
    {
        for(ItemWidgetListener listener : listeners)
        {
            listener.onAddButtonClick(modelIndex);
        }
    }

    @UiHandler("editButton")
    protected void onEditButtonClick(ClickEvent event)
    {
        for (ItemWidgetListener listener : listeners)
        {
            listener.onEditButtonClick(itemName.getText(), modelIndex);
        }
    }

    @UiHandler("removeButton")
    protected void onRemoveButtonClick(ClickEvent event)
    {
        for (ItemWidgetListener listener : listeners)
        {
            listener.onRemoveButtonClick(modelIndex);
        }
    }
}
