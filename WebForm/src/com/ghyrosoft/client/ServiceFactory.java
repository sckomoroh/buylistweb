package com.ghyrosoft.client;

import com.google.gwt.core.client.GWT;

public class ServiceFactory
{
    private static final BuyListServiceAsync ourInstance = GWT.create(BuyListService.class);

    public static synchronized BuyListServiceAsync getInstance()
    {
        return ourInstance;
    }
}
