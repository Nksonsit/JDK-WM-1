package com.androidapp.jdklokhandwala.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 29-12-2016.
 */

public class MyOrderFragment extends Fragment {

    private View parentView;
    private ViewPagerAdapter mAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static MyOrderFragment newInstance() {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        return myOrderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_order, container, false);
        init();
        return parentView;
    }

    private void init() {
        //init view pager
        viewPager = (ViewPager) parentView.findViewById(R.id.viewPager);
        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mAdapter.addFragment(new OrderHFragment(), "Order History");
        mAdapter.addFragment(new InquiryHFragment(), "Inquiry History");
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(0);

        //init tab layout
        tabLayout = (TabLayout) parentView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
