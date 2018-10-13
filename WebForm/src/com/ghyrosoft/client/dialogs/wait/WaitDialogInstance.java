package com.ghyrosoft.client.dialogs.wait;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/29/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class WaitDialogInstance
{
    private static WaitDialog dialog;

    private WaitDialogInstance()
    {
    }

    public static void showDialog(String message)
    {
        checkInstance();

        dialog.show(message);
    }

    public static void hide()
    {
        checkInstance();

        dialog.hide();
    }

    private static void checkInstance()
    {
        if (dialog == null)
        {
            dialog = new WaitDialog();
        }
    }
}
