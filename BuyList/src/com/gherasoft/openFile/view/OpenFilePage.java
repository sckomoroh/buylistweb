package com.gherasoft.openFile.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gherasoft.R;
import com.gherasoft.openFile.adapter.FolderAdapter;
import com.gherasoft.openFile.adapter.FolderItem;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * User: sckomoroh
 * Date: 1/16/13
 * Time: 10:12 AM
 */
public class OpenFilePage implements AdapterView.OnItemClickListener {
    private FolderAdapter mAdapter;
    private OpenFilePageListener mListener;

    public static final int INDEX = 2;

    public OpenFilePage(View view, Context context) {
        try {
            mAdapter = new FolderAdapter(context);
            ListView listView = (ListView) view.findViewById(R.id.openListView);
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setListener(OpenFilePageListener listener) {
        mListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FolderItem item = (FolderItem) mAdapter.getItem(i);

        if (!item.isFolder()) {
            buildModelFromFile(mAdapter.getFullName(i));
        } else {
            mAdapter.tryRebuild(i);
        }
    }

    private void buildModelFromFile(String fileName) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(fileName));

            if (mListener != null) {
                mListener.onFileSelected(doc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
