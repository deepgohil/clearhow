package com.example.quietly;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class shopfregment extends Fragment {

public shopfregment(){

}
View view;
TableLayout tablayout;
ImageSlider image_slider;

ImageView topbar;
////+17343578288

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_shopfregment, container, false);

//    test on click
    topbar=view.findViewById(R.id.topbar);
    topbar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getActivity(),testactivity.class));

        }
    });


    ///////////////////Slider image code withought title
    image_slider=view.findViewById(R.id.image_slider);
    final List<SlideModel> remoteimage=new ArrayList<>();
    FirebaseDatabase.getInstance().getReference().child("slider")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data:snapshot.getChildren())
                    {
                        remoteimage.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));
                    }
                    image_slider.setImageList(remoteimage,ScaleTypes.FIT);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    /////////////////////////Code ends
//////////////////////////////////////////tab layout
    TabLayout tabLayout=view.findViewById(R.id.tablayout);



    final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);

    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
    mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    viewPager.setAdapter(new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()));
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//    tabLayout.setupWithViewPager(viewPager);
//    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });

        return view;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new canteenFragment();
                case 1:
                    return new stationeryFragment();


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}


/////////////////////slider image code with title
//
//    Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        view=inflater.inflate(R.layout.fragment_shopfregment, container, false);
////    imageframe=view.findViewById(R.id.imageframe);
////
////    imageframe.setOnClickListener(new View.OnClickListener() {
////        @Override
////        public void onClick(View v) {
////            imageframe.setImageResource(R.drawable.frameimg2);
////        }
////    });
//
//        image_slider=view.findViewById(R.id.image_slider);
//final List<SlideModel> remoteimage=new ArrayList<>();
//
//        FirebaseDatabase.getInstance().getReference().child("slider")
//        .addListenerForSingleValueEvent(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot snapshot) {
//        for (DataSnapshot data:snapshot.getChildren())
//        {
//        remoteimage.add(new SlideModel(data.child("url").getValue().toString(),data.child("title").getValue().toString(), ScaleTypes.FIT));
//        }
//        image_slider.setImageList(remoteimage,ScaleTypes.FIT);
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//        });
//
//        return view;
//        }