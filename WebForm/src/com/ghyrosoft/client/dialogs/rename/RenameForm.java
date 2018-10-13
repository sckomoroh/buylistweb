package com.ghyrosoft.client.dialogs.rename;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RenameForm extends Composite
{
	private static final RenameFormUiBinder uiBinder = GWT.create(RenameFormUiBinder.class);
	
	@UiField Button renameButton;
	@UiField Button cancelButton;
	@UiField TextBox nameEditBox;

    private final RenameDialog parent;

    private RenameFormListener listener;

	interface RenameFormUiBinder extends UiBinder<Widget, RenameForm>
	{
	}

	public RenameForm(RenameDialog parent, String text)
	{
		initWidget(uiBinder.createAndBindUi(this));

        this.parent = parent;

        nameEditBox.setText(text);
	}

    public void focus()
    {
        nameEditBox.setFocus(true);
    }

    public void setListener(RenameFormListener listener)
    {
        this.listener = listener;
    }

	@UiHandler("renameButton")
	public void onRenameClick(ClickEvent event)
	{
		if (listener != null)
        {
            listener.onRenameClicked(nameEditBox.getText());
        }

        parent.hide();
	}
	
	@UiHandler("cancelButton")
	public void onCancelButton(ClickEvent event)
	{
        parent.hide();
	}
}
