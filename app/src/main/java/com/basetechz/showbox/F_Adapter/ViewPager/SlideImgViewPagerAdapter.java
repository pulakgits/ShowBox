package com.basetechz.showbox.F_Adapter.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.basetechz.showbox.G_models.slideImgModel;
import com.basetechz.showbox.I_Activity.VideoActivity;
import com.basetechz.showbox.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideImgViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<slideImgModel> imgArrayList=new ArrayList<>();
    // constructor for slideImgViewPagerAdapter
    public SlideImgViewPagerAdapter(Context context,ArrayList<slideImgModel> imgArrayList){
        this.context = context;
        this.imgArrayList=imgArrayList;
    }
    @Override
    public int getCount() {
        return imgArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        slideImgModel model= imgArrayList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.slide_layout,null);
        ImageView imgView = view.findViewById(R.id.slideImg);

        // set OnClickListener on view to start Video activity
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("itemId",model.getSlideId());
                intent.putExtra("itemTxt",model.getSlideTxt());
                intent.putExtra("videoUrl",model.getVideoUrl());
                context.startActivity(intent);
            }
        });
        Picasso.get().load(model.getSlideImg()).into(imgView);
        container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
