package com.example.carousels.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carousels.GlideImageLoader;
import com.example.carousels.R;
import com.example.carousels.databinding.HomeFragmentBinding;
import com.github.carousels.Carousels;
import com.github.carousels.CarouselsConstants;
import com.github.carousels.bean.Page;
import com.github.carousels.bean.Type;
import com.github.carousels.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhichao
 * @date 20-9-16
 */
public class HomeFragment extends Fragment {

    private HomeFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Page<Object, Type>> pages = new ArrayList<>();
        pages.add(new Page<Object, Type>(R.drawable.dragon1, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.dragon2, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.dragon3, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.dragon4, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.dragon5, Type.TYPE_IMAGE));
        final List<String> titles = new ArrayList<>();
        titles.add("但夸端午节，谁荐屈原祠。");
        titles.add("谁家儿共女，庆端阳。");
        titles.add("粽包分两髻，艾束著危冠。");
        titles.add("五月五日午，赠我一枝艾。");
        titles.add("好酒沈醉酬佳节，十分酒，一分歌。");
        binding.carousels.pageList(pages)
                .imageLoader(new GlideImageLoader())
                .scrollDuration(CarouselsConstants.DEFAULT_SCROLL_DURATION)
                .pageIndicator(binding.pageIndicator)
                .loopMode(Carousels.LoopMode.RESTART)
                .startIndex(2)
                .offscreenPageLimit(5)
                .addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        binding.title.setText(titles.get(position));
                    }
                })
                .autoPlay(true)
                .start();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.carousels.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.carousels.stopAutoPlay();
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}