package com.example.carousels.java;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.carousels.R;
import com.example.carousels.databinding.MainJavaActivityBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhichao
 * @date 20-9-16
 */
public class MainJavaActivity extends AppCompatActivity {

    private  MainJavaActivityBinding binding;

    private static final int INDEX_HOME = 0;
    private static final int INDEX_DASHBOARD = 1;
    private static final int INDEX_NOTIFICATIONS = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainJavaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());
        fragments.add(DashboardFragment.newInstance());
        fragments.add(NotificationsFragment.newInstance());
        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case INDEX_HOME:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                        break;
                    case INDEX_DASHBOARD:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
                        break;
                    case INDEX_NOTIFICATIONS:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);
                        break;
                }
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        binding.viewPager.setCurrentItem(INDEX_HOME, false);
                        return true;
                    case R.id.navigation_dashboard:
                        binding.viewPager.setCurrentItem(INDEX_DASHBOARD, false);
                        return true;
                    case R.id.navigation_notifications:
                        binding.viewPager.setCurrentItem(INDEX_NOTIFICATIONS, false);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainJavaActivity.class);
        context.startActivity(starter);
    }
}
