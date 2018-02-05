package com.tangjd.common.helper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.tangjd.common.abs.BaseActivity;
import com.tangjd.common.abs.BaseFragment;

import java.util.List;

/**
 * Author: tangjd
 * Date: 2017/8/3
 */

public class NavigationTabStripHelper {
    // 使用示例
//        tabStrip.setTitles("  ", "  ");
//        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//            if (position == 0) {
//                setToolbarTitle("剂量率");
//            } else if (position == 1) {
//                setToolbarTitle("谱线数据");
//            }
//            ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(position)).getDataOnCreateView();
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    });
//        NavigationTabStripHelper.set(this, tabStrip, viewPager,
//            new String[]{FragmentSimple.class.getName(), FragmentPro.class.getName()},
//            null);
//        viewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(0)).getDataOnCreateView();
//            }
//        });

    public static void set(final BaseActivity context, NavigationTabStrip tabStrip, final ViewPager viewPager, String[] titles, final String[] baseFragmentNameArr, final List<Bundle> bundles) {
        tabStrip.setTitles(titles);
        viewPager.setAdapter(new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            private SparseArray<Fragment> mFragments = new SparseArray<>();

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mFragments.get(position);
                if (fragment == null) {
                    fragment = Fragment.instantiate(context, baseFragmentNameArr[position], bundles == null ? null : bundles.get(position));
                    mFragments.put(position, fragment);
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return baseFragmentNameArr.length;
            }
        });
        tabStrip.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(position)).getDataOnCreateView();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(0)).getDataOnCreateView();
            }
        });
    }

    @Deprecated
    public static void set(final BaseActivity context, NavigationTabStrip tabStrip, final ViewPager viewPager, final String[] baseFragmentNameArr, final List<Bundle> bundles) {
        viewPager.setAdapter(new FragmentPagerAdapter(context.getSupportFragmentManager()) {
            private SparseArray<Fragment> mFragments = new SparseArray<>();

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = mFragments.get(position);
                if (fragment == null) {
                    fragment = Fragment.instantiate(context, baseFragmentNameArr[position], bundles == null ? null : bundles.get(position));
                    mFragments.put(position, fragment);
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return baseFragmentNameArr.length;
            }
        });
        tabStrip.setViewPager(viewPager);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(0)).getDataOnCreateView();
            }
        });
        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        ((BaseFragment) ((FragmentPagerAdapter) viewPager.getAdapter()).getItem(position)).getDataOnCreateView();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }
        );
    }
}
