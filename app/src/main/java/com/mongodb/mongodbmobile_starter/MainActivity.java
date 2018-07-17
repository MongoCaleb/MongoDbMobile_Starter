package com.mongodb.mongodbmobile_starter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomNav;
    private ViewPager mPager;
    private CustomFragmentManager mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();

        // Initialize Stitch and Authenticate
        new StitchHandler(getApplicationContext()).AuthWithApiKey();

        /* You can authenticate using:
           .AuthWithApiKey()
           .AuthWithUserPass(username, password)
           .AuthWithFacebook()
           .AuthWithGoogle()
           .AuthWithAnon()
           ... or build your own custom auth
        */
    }

    /// Handlers for the tabbed & swipeable UI
    private void initializeUI() {
        mPager = findViewById(R.id.pager);
        mPagerAdapter = new CustomFragmentManager(getSupportFragmentManager());
        mPagerAdapter.addFragments(new ConfigScreenFragment());
        mPagerAdapter.addFragments(new RemoteDataFragment());
        mPagerAdapter.addFragments(new LocalDataFragment());
        mPager.setAdapter(mPagerAdapter);

        bottomNav = findViewById(R.id.navigation);

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
