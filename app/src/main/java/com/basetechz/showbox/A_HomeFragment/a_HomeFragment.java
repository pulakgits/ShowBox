package com.basetechz.showbox.A_HomeFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basetechz.showbox.F_Adapter.ViewPager.b_ViewPagerAdapter;
import com.basetechz.showbox.databinding.FragmentHomeBinding;
import com.basetechz.showbox.*;

public class a_HomeFragment extends Fragment {

    public a_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);

        // set up view pager adapter in home fragment
        b_ViewPagerAdapter viewPagerAdapter = new b_ViewPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tab.setupWithViewPager(binding.viewPager);
        
        return binding.getRoot();
    }
}