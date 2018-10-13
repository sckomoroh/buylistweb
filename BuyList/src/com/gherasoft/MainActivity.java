package com.gherasoft;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;
import com.gherasoft.buyList.view.BuyListPage;
import com.gherasoft.onlineList.adapter.OnlineListHelper;
import com.gherasoft.onlineList.view.OnlineListPage;
import com.gherasoft.onlineList.view.OnlineListPageListener;
import com.gherasoft.openFile.view.OpenFilePage;
import com.gherasoft.openFile.view.OpenFilePageListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements OpenFilePageListener, OnlineListPageListener
{
    private BuyListPage mBuyListPage;
    private OpenFilePage mOpenFilePage;
    private OnlineListPage mOnlineListPage;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);
        List<View> pages = new ArrayList<View>();

        View buyListPage = inflater.inflate(R.layout.main, null);
        View openFilePage = inflater.inflate(R.layout.open_dialog, null);
        View loginPage = inflater.inflate(R.layout.login_view, null);

        pages.add(loginPage);
        pages.add(buyListPage);
        //pages.add(openFilePage);

        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(pages);
        mViewPager = new ViewPager(this);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);

        setContentView(mViewPager);

        mOpenFilePage = new OpenFilePage(openFilePage, this);
        mBuyListPage = new BuyListPage(buyListPage, this);

        mOpenFilePage.setListener(this);

        Button loginButton = (Button) loginPage.findViewById(R.id.loginButton);
        Button registerButton = (Button) loginPage.findViewById(R.id.registerButton);
        final EditText loginEditBox = (EditText) loginPage.findViewById(R.id.loginEditText);
        final EditText passwordEditBox = (EditText) loginPage.findViewById(R.id.passwordEditText);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String login = loginEditBox.getText().toString();
                String password = passwordEditBox.getText().toString();

                if (OnlineListHelper.login(login, password))
                {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View onlineListPage = inflater.inflate(R.layout.online_list, null);

                    SamplePagerAdapter pagerAdapter = (SamplePagerAdapter) mViewPager.getAdapter();
                    pagerAdapter.pages.set(0, onlineListPage);
                    mViewPager.forceLayout();

                    mOnlineListPage = new OnlineListPage(onlineListPage, MainActivity.this);
                    mOnlineListPage.setListener(MainActivity.this);

                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String login = loginEditBox.getText().toString();
                String password = passwordEditBox.getText().toString();

                if (OnlineListHelper.register(login, password))
                {
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View onlineListPage = inflater.inflate(R.layout.online_list, null);

                    SamplePagerAdapter pagerAdapter = (SamplePagerAdapter) mViewPager.getAdapter();
                    pagerAdapter.pages.set(0, onlineListPage);
                    mViewPager.forceLayout();

                    mOnlineListPage = new OnlineListPage(onlineListPage, MainActivity.this);
                    mOnlineListPage.setListener(MainActivity.this);

                    pagerAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buylist_option_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();

        MenuInflater inflater = getMenuInflater();

        if (mViewPager.getCurrentItem() == BuyListPage.INDEX)
        {
            inflater.inflate(R.menu.buylist_option_menu, menu);
        }

        if (mViewPager.getCurrentItem() == OnlineListPage.INDEX && mOnlineListPage != null)
        {
            inflater.inflate(R.menu.onlinelist_option_menu, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.expandAll:
                mBuyListPage.expandAll();
                return true;

            case R.id.collapseAll:
                mBuyListPage.collapseAll();
                return true;

            case R.id.refreshOnlineLists:
                mOnlineListPage.refreshLists();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFileSelected(Document fileXmlContent)
    {
        mBuyListPage.setXmlContent(fileXmlContent.getDocumentElement());
        mViewPager.setCurrentItem(BuyListPage.INDEX);
    }

    @Override
    public void onContentLoaded(Document doc)
    {
        mBuyListPage.setXmlContent((Element) doc.getDocumentElement().getFirstChild());
        mViewPager.setCurrentItem(BuyListPage.INDEX);
    }
}
