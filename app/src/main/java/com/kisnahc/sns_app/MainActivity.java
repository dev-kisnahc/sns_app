package com.kisnahc.sns_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhn.android.naverlogin.OAuthLogin;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                BottomNavigate(item.getItemId());
                return true;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.nav_1);


    }
    private void BottomNavigate(int itemId) {

        String tag = String.valueOf(itemId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();

        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }
        Fragment fragment = fragmentManager.findFragmentByTag(tag);

        if (fragment == null) {
            if (itemId == R.id.nav_1) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.nav_2) {
                fragment = new ChatFragment();
            } else if (itemId == R.id.nav_3) {
                fragment = new SearchFragment();
            } else {
                fragment = new UserFragment();
            }
            fragmentTransaction.add(R.id.contents_layout, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }
}