package com.ghyrosoft.client.dialogs.newname;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/30/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewNameDialog extends DialogBox
{
    public NewNameDialog(NewNameFormListener listener)
    {
    	setText("New name");

    	setModal(true);
        setGlassEnabled(true);

        NewNameForm form = new NewNameForm(this);
        form.setListener(listener);

        add(form);

        center();

        form.focus();
    }
}
