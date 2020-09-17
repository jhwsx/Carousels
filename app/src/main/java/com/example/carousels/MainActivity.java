package com.example.carousels;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carousels.databinding.MainActivityBinding;
import com.example.carousels.java.MainJavaActivity;

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
        binding.materialButtonJava.setOnClickListener(this);
        binding.materialButtonKotlin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.material_button_java:
                MainJavaActivity.start(this);
                break;
            case R.id.material_button_kotlin:
                MainKotlinActivity.start(this);
                break;
            default:
                break;
        }
    }
}
