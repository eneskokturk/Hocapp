package com.example.hocapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES= new int[]{R.string.tab_text_1,R.string.tab_text_2};

    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm){

        super(fm);
        mContext= context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Tab1Fragment.newInstance();
            case 1:
                return Tab2Fragment.newInstance();

            default:
                return null;
        }

    }

    public CharSequence getPageTitle(int position){
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {

        //Show 2 total pages.
        return 2;
    }
}
