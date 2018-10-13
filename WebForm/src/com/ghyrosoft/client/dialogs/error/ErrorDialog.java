package com.ghyrosoft.client.dialogs.error;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * User: ezvigunov
 * Date: 1/29/13
 * Time: 12:55 PM
 */
public class ErrorDialog extends DialogBox {
    public ErrorDialog(String errorMessage)
    {
    	setText("Error");
    	
        setModal(true);
        setGlassEnabled(true);

        add(new ErrorForm(errorMessage, this));

        center();
    }
}
