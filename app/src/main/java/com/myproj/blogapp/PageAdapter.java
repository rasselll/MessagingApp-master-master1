package com.myproj.blogapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jawwad on 17/08/2017.
 */

public class PageAdapter extends FragmentPagerAdapter {

    public PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        ChatFragment cf = new ChatFragment();
        return cf;
        // return null;
    }

    @Override
    public int getCount() {
        return 1; // because we have just one Tab i.e chat
    }

    public CharSequence getPageTitle(int position) {
        return "CHATS";
    }
}
