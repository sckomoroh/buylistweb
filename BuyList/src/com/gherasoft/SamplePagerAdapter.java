package com.gherasoft;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 1/8/13
 * Time: 10:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class SamplePagerAdapter extends PagerAdapter
{
    List<View> pages = null;

    public SamplePagerAdapter(List<View> pages)
    {
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(View collection, int position)
    {
        View v = pages.get(position);
        ((ViewPager) collection).addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(View collection, int position, Object view)
    {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public int getCount()
    {
        return pages.size();
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }

    @Override
    public void finishUpdate(View arg0)
    {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void startUpdate(View arg0)
    {
    }
}
