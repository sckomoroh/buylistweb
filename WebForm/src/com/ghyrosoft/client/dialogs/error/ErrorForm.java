package com.ghyrosoft.client.dialogs.error;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ErrorForm extends Composite
{
	private static ErrorFormUiBinder uiBinder = GWT.create(ErrorFormUiBinder.class);

    @UiField
    Label errorMessageLabel;

    private ErrorDialog dialog;

	interface ErrorFormUiBinder extends UiBinder<Widget, ErrorForm>
	{
	}

	public ErrorForm(String errorMessage, ErrorDialog dialog)
	{
		initWidget(uiBinder.createAndBindUi(this));

        errorMessageLabel.setText(errorMessage);
        this.dialog = dialog;
	}

    @UiHandler("closeButton")
    protected void onCloseButtonClick(ClickEvent event)
    {
        this.dialog.hide();
    }
}
