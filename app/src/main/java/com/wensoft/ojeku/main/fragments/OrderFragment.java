package com.wensoft.ojeku.main.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wensoft.ojeku.R;
import com.wensoft.ojeku.main.fragments.handle_order.OrderProcessFragment;
import com.wensoft.ojeku.main.fragments.handle_order.OrderCompleteFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farhan on 2/19/17.
 */

public class OrderFragment extends Fragment {

    View view;
    PagerAdapter adapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    public OrderFragment() {
        // Required empty public constructor
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getChildFragmentManager());
        adapter.addFragment(new OrderCompleteFragment(), "Proses");
        adapter.addFragment(new OrderProcessFragment(), "Selesai");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);
        super.onCreate(savedInstanceState);
        // ButterKnife.bind(getActivity(), view);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager2);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }

}