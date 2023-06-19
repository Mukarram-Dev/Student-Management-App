package com.mukarram.superioruniversity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentStatePagerAdapter;

import Fragmnets.AddTimeTable;
import Fragmnets.FragmentTimeTable;

public class PageAdapter extends FragmentStatePagerAdapter {

    int numOfTabs = 0;

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTabs = behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentTimeTable tab1 = new FragmentTimeTable();
                return tab1;
            case 1:
                AddTimeTable tab2 = new AddTimeTable();
                return tab2;

            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }
}