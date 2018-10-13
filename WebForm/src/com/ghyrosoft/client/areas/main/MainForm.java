package com.ghyrosoft.client.areas.main;

import com.ghyrosoft.client.areas.compose.ComposeArea;
import com.ghyrosoft.client.areas.explore.ExploreArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

public class MainForm extends Composite
{
	private static final MainFormUiBinder uiBinder = GWT.create(MainFormUiBinder.class);

	@UiField
    ComposeArea composeArea;
	@UiField
    ExploreArea exploreArea;
	@UiField TabPanel tabPanel;
	@UiField Button logoutButton;
	@UiField Label userNameLabel;

    private LogoutCallback logoutCallback;
	
	interface MainFormUiBinder extends UiBinder<Widget, MainForm>
	{
	}

	public MainForm()
	{
		initWidget(uiBinder.createAndBindUi(this));
        tabPanel.selectTab(0);
	}

    public void setLogoutCallback(LogoutCallback logoutCallback)
    {
        this.logoutCallback = logoutCallback;
    }

    public ExploreArea getExploreArea()
    {
        return this.exploreArea;
    }

    public ComposeArea getComposeArea()
    {
        return this.composeArea;
    }
    
    public void setUserName(String userName)
    {
    	userNameLabel.setText(userName);
    }

    @UiHandler(value = "logoutButton")
    protected void onLoginButtonClick(ClickEvent event)
    {
        if (logoutCallback != null)
        {
            logoutCallback.onLogout();
        }
    }
}
