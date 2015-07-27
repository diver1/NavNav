package com.example.simfur.navme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                // List of routes
                return new routeFragment();
            case 1:
                // Route player
                return new NavActivity();
        }
        return null;
    }

    @Override
    public int getCount() {
        /* Get item count - equal to number of tabs */
        return 2;
    }
}