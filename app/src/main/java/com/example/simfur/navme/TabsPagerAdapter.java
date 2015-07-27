package com.example.simfur.navme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * Created by simfur on 27/07/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                //return new TopRatedFragment();
            case 1:
                // Games fragment activity
                //return new GamesFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        /* Get item count - equal to number of tabs */
        return 2;
    }
}