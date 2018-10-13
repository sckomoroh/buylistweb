package com.ghyrosoft.client.dialogs.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

public class LoginForm extends Composite
{
    private LoginDialogListener listener;

	private static final LoginFormUiBinder uiBinder = GWT.create(LoginFormUiBinder.class);

    @UiField
    TextBox loginEditBox;

    @UiField
    TextBox passwordEditBox;

    @UiField
    Label errorLabel;

    interface LoginFormUiBinder extends UiBinder<Widget, LoginForm>
	{
	}

	public LoginForm()
	{
		initWidget(uiBinder.createAndBindUi(this));

        errorLabel.setVisible(false);
	}

    public void setListener(LoginDialogListener listener)
    {
        this.listener = listener;
    }

    @UiHandler("loginButton")
    void onLoginButtonClick(ClickEvent event)
    {
        String login = loginEditBox.getText();
        String password = passwordEditBox.getText();

        if (listener != null)
        {
            listener.onLogin(login,  password);
        }
    }
}

