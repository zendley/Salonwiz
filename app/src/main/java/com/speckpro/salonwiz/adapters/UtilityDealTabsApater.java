package com.speckpro.salonwiz.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.speckpro.salonwiz.ui.utilitydeals.uifragments.ActiveDealsFragment;
import com.speckpro.salonwiz.ui.utilitydeals.uifragments.AllDealsFragment;
import com.speckpro.salonwiz.ui.utilitydeals.uifragments.InActiveDealsFragment;

public class UtilityDealTabsApater extends FragmentPagerAdapter {
    private final Context myContext;
    int totalTabs;
    public UtilityDealTabsApater(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllDealsFragment allDeals = new AllDealsFragment();
                return allDeals;
            case 1:
                ActiveDealsFragment activeDeals = new ActiveDealsFragment();
                return activeDeals;
            case 2:
                InActiveDealsFragment inactiveDeals = new InActiveDealsFragment();
                return inactiveDeals;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }


}
