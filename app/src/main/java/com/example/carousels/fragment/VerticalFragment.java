package com.example.carousels.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.carousels.GlideImageLoader;
import com.example.carousels.R;
import com.example.carousels.databinding.VerticalFragmentBinding;
import com.github.carousels.Carousels;
import com.github.carousels.CarouselsConstants;
import com.github.carousels.bean.Page;
import com.github.carousels.bean.Type;

import java.util.ArrayList;

/**
 * @author wangzhichao
 * @date 20-9-16
 */
public class VerticalFragment extends Fragment {


    private VerticalFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = VerticalFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCarousels1();
    }

    private void setupCarousels1() {
        ArrayList<Page<?, Type>> pages = new ArrayList<>();
        pages.add(new Page<Object, Type>(R.drawable.pic_1, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.pic_2, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.pic_3, Type.TYPE_IMAGE));
        binding.carousels.pageList(pages)
                .imageLoader(new GlideImageLoader())
                .scrollDuration(CarouselsConstants.DEFAULT_SCROLL_DURATION)
                .pageIndicator(binding.pageIndicator)
                .loopMode(Carousels.LoopMode.RESTART)
                .startIndex(2)
                .offscreenPageLimit(3)
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

    public static VerticalFragment newInstance() {

        Bundle args = new Bundle();

        VerticalFragment fragment = new VerticalFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
