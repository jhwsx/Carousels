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
import com.example.carousels.databinding.NotificationsFragmentBinding;
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
public class NotificationsFragment extends Fragment {

    private NotificationsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotificationsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View adView = LayoutInflater.from(getContext()).inflate(R.layout.ad_layout, null);
        List<Page<Object, Type>> pages = new ArrayList<>();
        pages.add(new Page<Object, Type>(R.drawable.sea1, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(R.drawable.sea2, Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>(adView, Type.TYPE_VIEW));
        pages.add(new Page<Object, Type>(R.drawable.sea3, Type.TYPE_IMAGE));
        binding.carousels.pageList(pages)
                .imageLoader(new GlideImageLoader())
                .scrollDuration(CarouselsConstants.DEFAULT_SCROLL_DURATION)
                .pageIndicator(binding.pageIndicator)
                .loopMode(Carousels.LoopMode.REVERSE)
                .startIndex(0)
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

    public static NotificationsFragment newInstance() {

        Bundle args = new Bundle();

        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
