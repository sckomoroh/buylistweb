package com.ghyrosoft.client;

import com.ghyrosoft.client.areas.main.LogoutCallback;
import com.ghyrosoft.client.areas.main.MainForm;
import com.ghyrosoft.client.dialogs.error.ErrorDialog;
import com.ghyrosoft.client.dialogs.login.LoginDialog;
import com.ghyrosoft.client.dialogs.login.LoginDialogListener;
import com.ghyrosoft.client.dialogs.wait.WaitDialogInstance;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebForm implements EntryPoint
{
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad()
	{
        /*Cookies.removeCookie("user");
        Cookies.removeCookie("session");*/

        String user = Cookies.getCookie("user");
        String session = Cookies.getCookie("session");

        WaitDialogInstance.showDialog("Check the session");
        ServiceFactory.getInstance().checkSession(user, session, new CheckSessionAsyncCallback());
    }

    private void initMainForm(String userName)
    {
		MainForm mainForm = new MainForm();

        mainForm.setLogoutCallback(new LogoutCallback()
        {
            @Override
            public void onLogout()
            {
                RootPanel.get().clear();
                Cookies.removeCookie("user");
                Cookies.removeCookie("session");

                ServiceFactory.getInstance().checkSession(null, null, new CheckSessionAsyncCallback());
            }
        });

		RootPanel.get().add(mainForm);
        Window.addResizeHandler(new WindowResizeHandler(mainForm));

        int height = Window.getClientHeight() - 100;
        mainForm.getExploreArea().resize(height);
        mainForm.getComposeArea().resize(height);
        
        mainForm.setUserName(userName);
    }

    private class WindowResizeHandler implements ResizeHandler
    {
        final MainForm mainForm;

        public WindowResizeHandler(MainForm mainForm)
        {
            this.mainForm = mainForm;
        }

        @Override
        public void onResize(ResizeEvent event) {
            this.mainForm.getExploreArea().resize(event.getHeight() - 90);
            this.mainForm.getComposeArea().resize(event.getHeight() - 90);
        }
    }

    private class CheckSessionAsyncCallback implements AsyncCallback<Boolean>
    {
        @Override
        public void onFailure(Throwable caught) {
            WaitDialogInstance.hide();

            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(Boolean result) {
            WaitDialogInstance.hide();

            if (!result)
            {
                LoginDialog dlg = new LoginDialog();
                dlg.setListener(new OnLoginDialogListener(dlg));
            }
            else
            {
                initMainForm(Cookies.getCookie("user"));
            }
        }
    }

    private class OnLoginDialogListener implements LoginDialogListener
    {
        private final LoginDialog dialog;

        public OnLoginDialogListener(LoginDialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        public void onLogin(String login, String password) {
            WaitDialogInstance.showDialog("Login");
            ServiceFactory.getInstance().login(login, password, new LoginAsyncCallback(dialog));
        }
    }

    private class LoginAsyncCallback implements AsyncCallback<String[]>
    {
        private final LoginDialog dialog;

        public LoginAsyncCallback(LoginDialog dialog)
        {
            this.dialog = dialog;
        }

        @Override
        public void onFailure(Throwable caught) {
            WaitDialogInstance.hide();
            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(String[] result) {
            WaitDialogInstance.hide();

            if (result != null)
            {
                Cookies.setCookie("user", result[0]);
                Cookies.setCookie("session", result[1]);

                dialog.hide();

                initMainForm(Cookies.getCookie("user"));
            }
        }
    }
}
