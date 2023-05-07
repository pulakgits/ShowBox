package com.basetechz.showbox.F_Adapter.ViewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.A_ForYou.ForYou;
import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.C_Bollywood.Bollywood;
import com.basetechz.showbox.ActivitiesFragment;
import com.basetechz.showbox.MyListFragment;

public class Profile_ViewPagerAdapter extends FragmentPagerAdapter {

    FragmentManager fm;

    public Profile_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new ActivitiesFragment();
        }else {
            return new MyListFragment() ;
        }
    }

    @Override
    public int getCount() {
       return 2;
    }

    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Activities";
        }else{
            return "My Lists";
        }
    }
}
