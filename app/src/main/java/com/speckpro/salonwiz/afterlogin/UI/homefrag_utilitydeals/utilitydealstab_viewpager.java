package com.speckpro.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class utilitydealstab_viewpager extends FragmentPagerAdapter {
    private final Context myContext;
    int totalTabs;
    public utilitydealstab_viewpager(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                allFragment homeFragment = new allFragment();
                return homeFragment;
            case 1:
                activeFragment sportFragment = new activeFragment();
                return sportFragment;
            case 2:
                inactiveFragment movieFragment = new inactiveFragment();
                return movieFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}
