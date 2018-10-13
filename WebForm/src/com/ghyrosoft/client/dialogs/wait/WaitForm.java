package com.ghyrosoft.client.dialogs.wait;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WaitForm extends Composite
{
	private static WaitFormUiBinder uiBinder = GWT.create(WaitFormUiBinder.class);

    @UiField
    Label messageLabel;

	interface WaitFormUiBinder extends UiBinder<Widget, WaitForm>
	{
	}

	public WaitForm()
	{
		initWidget(uiBinder.createAndBindUi(this));
	}

    public void setMessage(String message)
    {
        messageLabel.setText(message);
    }
}
