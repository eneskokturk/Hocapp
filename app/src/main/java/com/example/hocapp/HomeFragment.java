package com.example.hocapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.tabs.TabLayout;


public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        TabsPagerAdapter tabsPagerAdapter= new TabsPagerAdapter(this,getActivity().getSupportFragmentManager());

        final ViewPager viewPager=(ViewPager)view.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabsPagerAdapter);

        final TabLayout tabs=(TabLayout)view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

}
