package com.androidapp.jdklokhandwala.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.custom.TfTextView;

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
        tabLayout = (TabLayout) parentView.findViewById(R.id.tabLayout);

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mAdapter.addFragment(new OrderHFragment().newInstance(), "Order History");
        mAdapter.addFragment(new InquiryHFragment().newInstance(), "Inquiry History");
        viewPager.setAdapter(mAdapter);
//        viewPager.setOffscreenPageLimit(0);

        //init tab layout
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mAdapter.getTabView(i));
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

        public View getTabView(int position) {
            TfTextView tfTextView = new TfTextView(getContext());
            tfTextView.setText(mFragmentTitleList.get(position));
            tfTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            tfTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tfTextView.setGravity(Gravity.CENTER);
            return tfTextView;
        }
    }
}
