package com.example.carousels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carousels.databinding.MainJavaActivityBinding;
import com.github.carousels.Carousels;
import com.github.carousels.CarouselsConstants;
import com.github.carousels.bean.Page;
import com.github.carousels.bean.Type;
import com.example.carousels.transformer.ZoomOutPageTransformer;
import com.github.carousels.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Java 版本代码
 * @author wangzhichao
 * @date 2020-9-10
 */
public class MainJavaActivity extends AppCompatActivity {

    private  MainJavaActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MainJavaActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        View adView1 = getLayoutInflater().inflate(R.layout.ad_layout, null);
//        ((TextView) adView1.findViewById(R.id.tv)).setText("A");
//        View adView2 = getLayoutInflater().inflate(R.layout.ad_layout, null);
//        ((TextView) adView2.findViewById(R.id.tv)).setText("B");
//        View adView3 = getLayoutInflater().inflate(R.layout.ad_layout, null);
//        ((TextView) adView3.findViewById(R.id.tv)).setText("C");
//        View adView4 = getLayoutInflater().inflate(R.layout.ad_layout, null);
//        ((TextView) adView4.findViewById(R.id.tv)).setText("D");
        ArrayList<Page<?, Type>> pages = new ArrayList<>();
//        pages.add(new Page<Object, Type>(R.drawable.b1, Type.TYPE_IMAGE));
//        pages.add(new Page<Object, Type>(R.drawable.b2, Type.TYPE_IMAGE));
//        pages.add(new Page<Object, Type>(adView1, Type.TYPE_VIEW));
//        pages.add(new Page<Object, Type>(adView2, Type.TYPE_VIEW));
//        pages.add(new Page<Object, Type>(adView3, Type.TYPE_VIEW));
//        pages.add(new Page<Object, Type>(adView4, Type.TYPE_VIEW));
//        pages.add(new Page<Object, Type>(R.drawable.b3, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.a, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.b, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.c, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.d, Type.TYPE_IMAGE));
        final List<String> titles = new ArrayList<>();
        titles.add("标题A");
        titles.add("标题B");
        titles.add("标题C");
        titles.add("标题D");
        binding.carousels.pageList(pages)
                .imageLoader(new GlideImageLoader())
                .scrollDuration(CarouselsConstants.DEFAULT_SCROLL_DURATION)
                .pageIndicator(binding.pageIndicator)
                .loopMode(Carousels.LoopMode.RESTART)
                .startIndex(2)
                .offscreenPageLimit(5)
//                .pageTransformer(true, new ZoomOutPageTransformer())
                .addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        binding.title.setText(titles.get(position));
                    }
                })
                .cornerRadius(16)
                .autoPlay(true)
                .start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.carousels.startAutoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.carousels.stopAutoPlay();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainJavaActivity.class);
        context.startActivity(starter);
    }
}
