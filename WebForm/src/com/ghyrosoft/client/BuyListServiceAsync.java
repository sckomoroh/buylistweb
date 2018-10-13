package com.ghyrosoft.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface BuyListServiceAsync {
    void getAllLists(String user, String session, AsyncCallback<List<String>> async);

    void getList(String listName, String user, String session, AsyncCallback<String> async);

	void checkSession(String user, String session, AsyncCallback<Boolean> async);

	void login(String user, String password, AsyncCallback<String[]> async);

    void removeList(String listName, String user, String session, AsyncCallback<Boolean> async);

    void commitList(String listName, String xmlContent, String user, String session, AsyncCallback<Void> async);

    void updateList(String listName, String xmlContent, String user, String session, AsyncCallback<Void> async);

    void checkListName(String listName, String user, String session, AsyncCallback<Boolean> async);
}
