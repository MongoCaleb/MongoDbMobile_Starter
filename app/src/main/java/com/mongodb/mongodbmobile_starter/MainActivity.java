package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //for screen paging
   // private static final int NUMBER_OF_PAGES = 3;
    public static BottomNavigationView bottomNav;
    private ViewPager mPager;
    private CustomFragmentManager mPagerAdapter;

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

    private void initializeUI() {

        mPager = findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(0);
        mPagerAdapter = new CustomFragmentManager(getSupportFragmentManager());
        mPagerAdapter.addFragments(new ConfigScreenFragment());
        mPagerAdapter.addFragments(new RemoteDataFragment());
        mPagerAdapter.addFragments(new LocalDataFragment());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==1) RemoteDataFragment.RefreshData();
                if (position==2) LocalDataFragment.RefreshData();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_remote:
                        mPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_local:
                        mPager.setCurrentItem(2);
                        return true;
                }
                return false;

            }
        });

    }


}
