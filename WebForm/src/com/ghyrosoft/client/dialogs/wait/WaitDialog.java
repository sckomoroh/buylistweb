package com.ghyrosoft.client.dialogs.wait;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/29/13
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class WaitDialog extends DialogBox
{
    private WaitForm form;

    public WaitDialog()
    {
    	setText("Wait");

    	setModal(true);
        setGlassEnabled(true);

        this.form = new WaitForm();
        this.add(form);
    }

    public void show(String message)
    {
        form.setMessage(message);
        center();
    }
}
