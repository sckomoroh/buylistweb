package com.ghyrosoft.client.areas.explore;

import com.ghyrosoft.client.ServiceFactory;
import com.ghyrosoft.client.common.StringUtil;
import com.ghyrosoft.client.dialogs.error.ErrorDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameFormListener;
import com.ghyrosoft.client.dialogs.wait.WaitDialogInstance;
import com.ghyrosoft.client.lists.buylist.controller.TreeController;
import com.ghyrosoft.client.lists.explorelist.controller.ListController;
import com.ghyrosoft.client.lists.explorelist.controller.ListControllerListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.xml.client.XMLParser;

import java.util.List;

// TODO: make this GUI separate
public class ExploreArea extends Composite
{
	private static final ExploreAreaUiBinder uiBinder = GWT.create(ExploreAreaUiBinder.class);
	
	@UiField Button refreshButton;
	@UiField Button revertButton;
    @UiField Button addCategoryButton;
    @UiField Button commitButton;
	@UiField FlexTable exploreTable;
	@UiField ScrollPanel modifyTree;
    @UiField SplitLayoutPanel splitPanel;
    @UiField Tree editTree;
    @UiField Label listNameLabel;

    private final ListController listController;
    private final TreeController treeController;

	interface ExploreAreaUiBinder extends UiBinder<Widget, ExploreArea>
	{
	}

	public ExploreArea()
	{
		initWidget(uiBinder.createAndBindUi(this));
        treeController = new TreeController(editTree, listNameLabel);
        listController = new ListController(exploreTable);

        listController.setListener(new ItemClickHandler());

        onRefreshButtonClick(null);
	}

    public void resize(int height)
    {
        splitPanel.setHeight(height - 10 + "px");
    }

	@UiHandler("refreshButton")
	void onRefreshButtonClick(ClickEvent event)
	{
        WaitDialogInstance.showDialog("Refreshing lists");

        String user = Cookies.getCookie("user");
        String session = Cookies.getCookie("session");

        ServiceFactory.getInstance().getAllLists(
                user,
                session,
                new GetListsAsyncCallback());
	}

	@UiHandler("revertButton")
	void onRevertButtonClick(ClickEvent event)
	{
        WaitDialogInstance.showDialog("Retrieving list");

        String user = Cookies.getCookie("user");
        String session = Cookies.getCookie("session");

        ServiceFactory.getInstance().getList(
                listNameLabel.getText(),
                user,
                session,
                new GetListAsyncCallback());
    }

	@UiHandler("commitButton")
	void onCommitButtonClick(ClickEvent event)
	{
        String xmlContent = treeController.getModel().buildXmlContentFromModel();
        String listName = treeController.getModel().getListName();

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

    @UiHandler("addCategoryButton")
    void onAddCategoryButton(ClickEvent event)
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
                        treeController.getModel().addItem(name);
                    }
                }
            }
        });
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

            if (!result)
            {
                WaitDialogInstance.showDialog("Update list");
                ServiceFactory.getInstance().updateList(
                        listName,
                        xmlContent,
                        user,
                        session,
                        new UpdateListAsyncCallback());
            }
            else
            {
                new ErrorDialog(StringUtil.format(
                        "List with name '{0}' does not exists",
                        listName));
            }
        }
    }

    private class UpdateListAsyncCallback implements AsyncCallback<Void> {
        @Override
        public void onFailure(Throwable caught) {
            WaitDialogInstance.hide();
            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(Void result) {
            WaitDialogInstance.hide();
        }
    }

    private class GetListsAsyncCallback implements AsyncCallback<List<String>>
    {
        @Override
        public void onFailure(Throwable caught) {
            WaitDialogInstance.hide();
        }

        @Override
        public void onSuccess(List<String> result) {
            WaitDialogInstance.hide();

            listController.getModel().clear();

            for (String itemName : result)
            {
                listController.getModel().addItem(itemName);
            }
        }
    }

    private class GetListAsyncCallback implements AsyncCallback<String>
    {
        @Override
        public void onFailure(Throwable caught)
        {
            WaitDialogInstance.hide();
            new ErrorDialog(caught.getMessage());
        }

        @Override
        public void onSuccess(String result)
        {
            WaitDialogInstance.hide();
            treeController.getModel().buildModelFromXml(XMLParser.parse(result));

            revertButton.setEnabled(true);
            commitButton.setEnabled(true);
            addCategoryButton.setEnabled(true);
        }
    }

    private class ItemClickHandler implements ListControllerListener
    {
        @Override
        public void onListItemClick(int index)
        {
            String listName = listController.getModel().getItem(index);
            String user = Cookies.getCookie("user");
            String session = Cookies.getCookie("session");

            WaitDialogInstance.showDialog("Retrieving list");
            ServiceFactory.getInstance().getList(
                    listName,
                    user,
                    session,
                    new GetListAsyncCallback());
        }

        @Override
        public void onRemoveItemClick(int index)
        {
            String listName = listController.getModel().getItem(index);
            String user = Cookies.getCookie("user");
            String session = Cookies.getCookie("session");

            WaitDialogInstance.showDialog("Removing list");
            ServiceFactory.getInstance().removeList(listName, user, session, new RemoveListAsyncCallback());
        }
    }

    private class RemoveListAsyncCallback implements AsyncCallback<Boolean>
    {
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
        }
    }
}
