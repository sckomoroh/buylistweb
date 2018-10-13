package com.ghyrosoft.client.dialogs.rename;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 12:00 AM
 */
public class RenameDialog extends DialogBox
{
    public RenameDialog(String text, RenameFormListener listener)
    {
    	setText("Rename");

    	setModal(true);
        setGlassEnabled(true);

        RenameForm renameForm = new RenameForm(this, text);
        renameForm.setListener(listener);

        add(renameForm);

        center();

        renameForm.focus();
    }
}
