package com.example.carousels.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carousels.R;
import com.example.carousels.databinding.MainActivityBinding;

/**
 * @author wangzhichao
 * @date 2020-9-10
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private  MainActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.materialButtonViewpager.setOnClickListener(this);
        binding.materialButtonViewpager2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.material_button_viewpager:
                NestViewPagerActivity.start(this);
                break;
            case R.id.material_button_viewpager2:
                NestViewPager2Activity.start(this);
                break;
            default:
                break;
        }
    }
}
