package com.ghyrosoft.client.lists.buylist.controller.ui;

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

public class SubItemWidget extends Composite
{
	private static final SubItemWidgetUiBinder uiBinder = GWT.create(SubItemWidgetUiBinder.class);

    @UiField
    Label itemName;

    private int modelIndex;
    private int parentIndex;

    private final List<SubItemWidgetListener> listeners = new ArrayList<SubItemWidgetListener>();

	interface SubItemWidgetUiBinder extends UiBinder<Widget, SubItemWidget>
	{
	}

	public SubItemWidget(String itemName, int index, int parentIndex)
	{
		initWidget(uiBinder.createAndBindUi(this));
        this.itemName.setText(itemName);

        modelIndex = index;
        this.parentIndex = parentIndex;
	}

    public void addListener(SubItemWidgetListener listener)
    {
        listeners.add(listener);
    }

    public void setSubItemName(String itemName)
    {
        this.itemName.setText(itemName);
    }

    public void setIndex(int index)
    {
        modelIndex = index;
    }

    public void setParentIndex(int index)
    {
        parentIndex = index;
    }

    @UiHandler("editButton")
    protected void onEditButtonClick(ClickEvent event)
    {
        for (SubItemWidgetListener listener : listeners)
        {
            listener.onEditButtonClick(itemName.getText(), parentIndex, modelIndex);
        }
    }

    @UiHandler("removeButton")
    protected void onRemoveButtonClick(ClickEvent event)
    {
        for (SubItemWidgetListener listener : listeners)
        {
            listener.onRemoveButtonClick(parentIndex, modelIndex);
        }
    }
}
