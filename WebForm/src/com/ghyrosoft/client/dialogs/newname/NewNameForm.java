package com.ghyrosoft.client.dialogs.newname;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/30/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewNameForm extends Composite
{
    private static final NewNameFormUiBinder uiBinder = GWT.create(NewNameFormUiBinder.class);

    @UiField
    TextBox nameEditBox;

    private final NewNameDialog parent;
    private NewNameFormListener listener;

    interface NewNameFormUiBinder extends UiBinder<Widget, NewNameForm>
    {
    }

    public NewNameForm(NewNameDialog parent)
    {
        initWidget(uiBinder.createAndBindUi(this));

        this.parent = parent;
    }

    public void focus()
    {
        nameEditBox.setFocus(true);
    }

    public void setListener(NewNameFormListener listener)
    {
        this.listener = listener;
    }

    @UiHandler("okButton")
    protected void onOkButtonClick(ClickEvent event)
    {
        if (listener != null)
        {
            listener.onNewName(nameEditBox.getText());
        }

        parent.hide();
    }

    @UiHandler("cancelButton")
    protected void onCancelButtonClick(ClickEvent event)
    {
        parent.hide();
    }
}