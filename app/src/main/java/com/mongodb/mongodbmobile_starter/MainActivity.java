package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //for screen paging
    private static final int NUMBER_OF_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new StitchHandler(getApplicationContext());
        initializeUI();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 1:
                    return new RemoteDataFragment();
                case 2:
                    return new LocalDataFragment();
                case 0:
                default:
                    return new ConfigScreenFragment();
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
    }

    private void initializeUI() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(0);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //if (position==1) RemoteDataFragment.RefreshData();
                //if (position==2) LocalDataFragment.RefreshData();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state==1) RemoteDataFragment.RefreshData();
                if (state==2) LocalDataFragment.RefreshData();
            }
        });
    }


}
