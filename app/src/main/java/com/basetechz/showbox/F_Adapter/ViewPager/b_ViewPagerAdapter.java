package com.basetechz.showbox.F_Adapter.ViewPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.A_ForYou.ForYou;
import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.B_IndianFlim.IndianFilm;
import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.C_Bollywood.Bollywood;
import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.D_Hollywood.Hollywood;
import com.basetechz.showbox.A_HomeFragment.Main_Activity.Inner_Fragments.E_Series.Series;

public class b_ViewPagerAdapter extends FragmentPagerAdapter {
    FragmentManager fm;
    public b_ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       if(position==0){
           return  new ForYou();
       } else if (position==1) {
           return  new IndianFilm();
       } else if (position==2) {
           return new Bollywood();
       }else if(position==3) {
           return new Hollywood();
       }else {
           return new Series();
       }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "For You";
        }else if(position ==1){
            return "Pan India";
        } else if (position==2) {
            return "Bollywood";
        }else if(position==3){
            return "Hollywood";
        }else {
            return "Series";
        }
    }
}
