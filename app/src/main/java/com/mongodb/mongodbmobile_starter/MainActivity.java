package com.mongodb.mongodbmobile_starter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static BottomNavigationView bottomNav;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialize Stitch and Authenticate

           You can authenticate using:
               .AuthWithApiKey()
               .AuthWithUserPass(username, password)
               .AuthWithFacebook()
               .AuthWithGoogle()
               .AuthWithAnon()
               ... or build your own custom auth

            Because auth is asynchronous, pass a new OnAuthCompleted listener.
        */
        new StitchHandler(getApplicationContext()).AuthWithApiKey(new StitchHandler.OnAuthCompleted() {
            @Override
            public void onSuccess() {
                // We put further UI work in here so
                // that the user cannot load data before
                // we authenticate.
                initializeUI();
            }

            @Override
            public void onfail(Exception e) {
                // Auth failed. Show the exception to the user
                showAlert(e.toString() + "\n\nPlease fix this error and restart the app.");
            }
        });
    }

    /// Handlers for the tabbed & swipeable UI
    private void initializeUI() {
        mPager = findViewById(R.id.pager);
        CustomFragmentManager mPagerAdapter = new CustomFragmentManager(getSupportFragmentManager());
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

    private void showAlert(String msg) {
        AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Stitch Error")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
}
