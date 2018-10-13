package com.ghyrosoft.client.areas.compose;

import com.ghyrosoft.client.ServiceFactory;
import com.ghyrosoft.client.common.StringUtil;
import com.ghyrosoft.client.dialogs.error.ErrorDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameFormListener;
import com.ghyrosoft.client.dialogs.rename.RenameDialog;
import com.ghyrosoft.client.dialogs.rename.RenameFormListener;
import com.ghyrosoft.client.dialogs.wait.WaitDialogInstance;
import com.ghyrosoft.client.lists.buylist.controller.TreeController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

public class ComposeArea extends Composite
{
    private static final ComposeAreaUiBinder uiBinder = GWT.create(ComposeAreaUiBinder.class);

    @UiField
    protected VerticalPanel verticalPanel;
    @UiField
    protected Tree composeTree;
    @UiField
    protected Label listNameLabel;
    @UiField
    protected Button commitButton;

    private final TreeController controller;

    interface ComposeAreaUiBinder extends UiBinder<Widget, ComposeArea>
    {
    }

    public ComposeArea()
    {
        initWidget(uiBinder.createAndBindUi(this));

        controller = new TreeController(composeTree, listNameLabel);
    }

    public void resize(int height)
    {
        this.verticalPanel.setHeight(height + "px");
    }

    @UiHandler("addCategoryButton")
    protected void onAddCategoryClick(ClickEvent event)
    {
        new NewNameDialog(new NewNameFormListener()
        {
            @Override
            public void onNewName(String name)
            {
                if (name.length() > 0)
                {
                    if (name.contains("'") || name.contains("\""))
                    {
                        new ErrorDialog("The name cannot contains characters ' or \"");
                    }
                    else
                    {
                        controller.getModel().addItem(name);
                    }
                }
            }
        });
    }

    @UiHandler("renameListButton")
    protected void onRenameButtonClick(ClickEvent event)
    {
        new RenameDialog(listNameLabel.getText(), new RenameFormListener()
        {
            @Override
            public void onRenameClicked(String text)
            {
                if (text.length() > 0)
                {
                    if (text.contains("\"") || text.contains("'"))
                    {
                        new ErrorDialog("The name cannot contain the charasters ' or \"");
                    }
                    else
                    {
                        controller.getModel().setListName(text);
                        commitButton.setEnabled(true);
                    }
                }
            }
        });
    }

    @UiHandler("clearButton")
    protected void onClearButtonClick(ClickEvent event)
    {
        controller.getModel().clear();
    }

    @UiHandler("commitButton")
    protected void onCommitButtonClick(ClickEvent event)
    {
        String xmlContent = controller.getModel().buildXmlContentFromModel();
        String listName = controller.getModel().getListName();

        String user = Cookies.getCookie("user");
        String session = Cookies.getCookie("session");

        CheckListNameAsyncCallback asyncCallback = new CheckListNameAsyncCallback(
                listName,
                xmlContent,
                user,
                session);

        WaitDialogInstance.showDialog("Check list name");
        ServiceFactory.getInstance().checkListName(
                listName,
                user,
                session,
                asyncCallback);
    }

    private class CheckListNameAsyncCallback implements AsyncCallback<Boolean>
    {
        private String user;
        private String session;
        private String xmlContent;
        private String listName;

        public CheckListNameAsyncCallback(String listName, String xmlContent, String user, String session)
        {
            this.user = user;
            this.session = session;
            this.xmlContent = xmlContent;
            this.listName = listName;
        }

        @Override
        public void onFailure(Throwable caught)
        {
            WaitDialogInstance.hide();
            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(Boolean result)
        {
            WaitDialogInstance.hide();

            if (result)
            {
                WaitDialogInstance.showDialog("Commit list");
                ServiceFactory.getInstance().commitList(
                        listName,
                        xmlContent,
                        user,
                        session,
                        new CommitListAsyncCallback());
            } else
            {
                new ErrorDialog(StringUtil.format(
                        "List with name '{0}' already exists",
                        listName));
            }
        }
    }

    private class CommitListAsyncCallback implements AsyncCallback<Void>
    {
        @Override
        public void onFailure(Throwable caught)
        {
            WaitDialogInstance.hide();
            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(Void result)
        {
            WaitDialogInstance.hide();
        }
    }
}
