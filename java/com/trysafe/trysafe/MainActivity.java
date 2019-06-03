package com.trysafe.trysafe;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.trysafe.trysafe.Models.NotesModel;
import com.trysafe.trysafe.SqlLiteHelper.DBManager;

import java.util.List;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new SearchFragment();
    final Fragment fragment3 = new NotesFragment();
    final Fragment fragment4 = new FeedsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;


    public static final String HOME = "Home";
    public static final String SEARCH = "Other";
    public static final String FEED = "Other";

    public static int currentFragment = -1;


    private ImageView mLogo;
    private ImageView mProfile;
    RelativeLayout   toolBarLayout;



    FrameLayout frameLayout;

    List<NotesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        frameLayout = findViewById(R.id.fragment_container);

        final BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fm.beginTransaction().add(R.id.fragment_container,fragment4,"4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment3,"3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment2,"2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container,fragment1,"1").commit();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  //  Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.home:

                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;

                        case R.id.search:

                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;

                        case R.id.notes:

                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;

                        case R.id.feed:

                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;
                    }

                    return false;
                }
            };



}
