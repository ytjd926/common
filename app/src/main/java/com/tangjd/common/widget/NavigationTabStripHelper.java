package com.tangjd.common.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.tangjd.common.abs.BaseActivity;

/**
 * Author: tangjd
 * Date: 2017/8/3
 */

public class NavigationTabStripHelper {
    public static void set(final BaseActivity context, NavigationTabStrip tabStrip, ViewPager viewPager, final String[] fragmentNameArr, final Bundle bundle) {
        viewPager.setAdapter(new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            private SparseArray<Fragment> mFragments = new SparseArray<>();

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mFragments.get(position);
                if (fragment == null) {
                    fragment = Fragment.instantiate(context, fragmentNameArr[position], bundle);
                    mFragments.put(position, fragment);
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return fragmentNameArr.length;
            }
        });
        tabStrip.setViewPager(viewPager);
    }
}
