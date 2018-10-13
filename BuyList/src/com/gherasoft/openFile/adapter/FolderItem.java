package com.gherasoft.openFile.adapter;

/**
 * User: sckomoroh
 * Date: 1/9/13
 * Time: 10:32 PM
 */
public class FolderItem {
    private String mName;
    private boolean mIsFolder;

    public FolderItem(String name, boolean isFolder)
    {
        mName = name;
        mIsFolder = isFolder;
    }

    public String getName()
    {
        return mName;
    }

    public boolean isFolder()
    {
        return mIsFolder;
    }
}
