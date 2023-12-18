package com.example.multimediahub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.multimediahub.Adapter.AdapterViewPager;
import com.example.multimediahub.fragment.FragmentAudio;
import com.example.multimediahub.fragment.FragmentImages;
import com.example.multimediahub.fragment.FragmentPDFs;
import com.example.multimediahub.fragment.FragmentVideos;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class codes extends AppCompatActivity {
    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pagerMain = findViewById(R.id.pagerMain);
        bottomNav = findViewById(R.id.bottomNav);
        fragmentArrayList.add(new FragmentPDFs());
        fragmentArrayList.add(new FragmentImages());
        fragmentArrayList.add(new FragmentVideos());
        fragmentArrayList.add(new FragmentAudio());

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        //setAdapter
        pagerMain.setAdapter(adapterViewPager);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNav.setSelectedItemId(R.id.itPDFs);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.itImages);
                        break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.itVideos);
                        break;
                    case 3:
                        bottomNav.setSelectedItemId(R.id.itAudios);
                        break;
                }


                super.onPageSelected(position);
            }
        });
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               switch (item.getItemId()) {
                   case R.id.itPDFs:
                       pagerMain.setCurrentItem(0);
                       break;
                   case R.id.itImages:
                       pagerMain.setCurrentItem(1);
                       break;
                   case R.id.itVideos:
                       pagerMain.setCurrentItem(2);
                       break;
                   case R.id.itAudios:
                       pagerMain.setCurrentItem(3);
                       break;
               }


                return true;
            }
        });

    }
}
