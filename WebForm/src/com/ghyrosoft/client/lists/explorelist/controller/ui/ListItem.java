package com.ghyrosoft.client.lists.explorelist.controller.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ListItem extends Composite {

	private static final ExploreListItemUiBinder uiBinder = GWT.create(ExploreListItemUiBinder.class);

    @UiField    Label itemName;

    private ListItemListener listener;
    private int index;

	interface ExploreListItemUiBinder extends UiBinder<Widget, ListItem> {
	}

	public ListItem(String itemName, int index) {
		initWidget(uiBinder.createAndBindUi(this));

        this.itemName.setText(itemName);
        this.index = index;
	}

    public void setIndex(int index)
    {
        this.index = index;
    }

    public void setListener(ListItemListener listener)
    {
        this.listener = listener;
    }

    public void addClickHandler(ClickHandler handler)
    {
        itemName.addClickHandler(handler);
    }

    @UiHandler("itemName")
    protected void onLabelClick(ClickEvent event)
    {
        if (listener != null)
        {
            listener.onItemClick(this.index);
        }
    }

    @UiHandler("removeButton")
    protected void onRemoveButtonClick(ClickEvent event)
    {
        if (listener != null)
        {
            listener.onRemoveButtonClick(this.index);
        }
    }
}
