package com.example.simfur.navme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Map;
import java.util.HashMap;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private Map<Integer, Fragment> mPageReferenceMap = new HashMap<Integer, Fragment>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        /* Create fragments and add to hashmap */
        Fragment fragment;
        switch (index) {
            case 0:
                // List of routes
                fragment = new RouteListFragment();
                mPageReferenceMap.put(index, fragment);
                return fragment;
            case 1:
                // Route player
                fragment = new NavFragment();
                mPageReferenceMap.put(index, fragment);
                return fragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        /* Get item count - equal to number of tabs */
        return 2;
    }

    public Fragment getFragment(int key) {
        /* Get fragment from key */
        return mPageReferenceMap.get(key);
    }
}