package com.example.carousels.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.carousels.R;
import com.example.carousels.databinding.NestViewpager2ActivityBinding;
import com.example.carousels.fragment.AdsFragment;
import com.example.carousels.fragment.HorizontalFragment;
import com.example.carousels.fragment.VerticalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhichao
 * @date 20-9-16
 */
public class NestViewPager2Activity extends AppCompatActivity {

    private NestViewpager2ActivityBinding binding;

    private static final int INDEX_HOME = 0;
    private static final int INDEX_DASHBOARD = 1;
    private static final int INDEX_NOTIFICATIONS = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NestViewpager2ActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(HorizontalFragment.newInstance());
        fragments.add(VerticalFragment.newInstance());
        fragments.add(AdsFragment.newInstance());
        binding.viewPager.setAdapter(new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case INDEX_HOME:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_horizontal);
                        break;
                    case INDEX_DASHBOARD:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_vertical);
                        break;
                    case INDEX_NOTIFICATIONS:
                        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_ad);
                        break;
                }
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_horizontal:
                        binding.viewPager.setCurrentItem(INDEX_HOME, false);
                        return true;
                    case R.id.navigation_vertical:
                        binding.viewPager.setCurrentItem(INDEX_DASHBOARD, false);
                        return true;
                    case R.id.navigation_ad:
                        binding.viewPager.setCurrentItem(INDEX_NOTIFICATIONS, false);
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NestViewPager2Activity.class);
        context.startActivity(starter);
    }
}
