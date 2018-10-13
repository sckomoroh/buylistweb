package com.gherasoft.openFile.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gherasoft.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 1/9/13
 * Time: 10:30 PM
 */
public class FolderAdapter extends BaseAdapter {
    private String mCurrentPath = "/mnt/sdcard";
    private List<FolderItem> mFolderItems = new ArrayList<FolderItem>();
    private Context mContext;

    public FolderAdapter(Context context)
    {
        mContext = context;
        buildAdapter();
    }

    @Override
    public int getCount() {
        return mFolderItems.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getItem(int i) {
        return mFolderItems.get(i);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getItemId(int i) {
        return i;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FolderItem fi = mFolderItems.get(i);
        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        int imageResource = fi.isFolder() ? R.drawable.folder : R.drawable.file;

        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(imageResource);
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.gravity = Gravity.CENTER;
        layout.addView(imageView, layoutParam);

        TextView titleView = new TextView(mContext);
        titleView.setText(fi.getName());
        titleView.setTextSize(18);
        titleView.setSingleLine(true);
        layoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.leftMargin = 10;
        layout.addView(titleView, layoutParam);

        return ll;
    }

    public String getFullName(int pos)
    {
        FolderItem fi = mFolderItems.get(pos);

        return mCurrentPath + "/" + fi.getName();
    }

    public void tryRebuild(int pos)
    {
        FolderItem fi = mFolderItems.get(pos);

        if (!fi.isFolder())
        {
            return;
        }

        mCurrentPath += "/" + fi.getName();
        buildAdapter();

        notifyDataSetChanged();
    }


    private void buildAdapter()
    {
        mFolderItems.clear();

        File f = new File(mCurrentPath);
        File[] files = f.listFiles();

        mFolderItems.add(new FolderItem("..", true));

        for(File ff : files)
        {
            if (ff.isDirectory())
            {
                mFolderItems.add(new FolderItem(ff.getName(), true));
            }
        }

        for(File ff : files)
        {
            if (!ff.isDirectory() && ff.getName().endsWith(".xml"))
            {
                mFolderItems.add(new FolderItem(ff.getName(), false));
            }
        }
    }
}
