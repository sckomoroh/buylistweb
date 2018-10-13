package com.ghyrosoft.client.dialogs.login;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * User: sckomoroh
 * Date: 1/25/13
 * Time: 3:56 PM
 */
public class LoginDialog extends DialogBox {
    private final LoginForm form;

    public LoginDialog()
    {
    	setText("Login");

        setModal(true);
        setGlassEnabled(true);

        form = new LoginForm();

        add(form);

        center();
    }

    public void setListener(LoginDialogListener listener)
    {
        form.setListener(listener);
    }
}
