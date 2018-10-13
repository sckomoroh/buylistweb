package com.ghyrosoft.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("buyList")
public interface BuyListService extends RemoteService
{
    List<String> getAllLists(String user, String session) throws Exception;

    String getList(String listName, String user, String session) throws Exception;

    boolean checkSession(String user, String session) throws Exception;

    String[] login(String user, String password) throws Exception;

    boolean removeList(String listName, String user, String session) throws Exception;

    void commitList(String listName, String xmlContent, String user, String session) throws Exception;

    void updateList(String listName, String xmlContent, String user, String session) throws Exception;
    
    boolean checkListName(String listName, String user, String session) throws Exception;
}
