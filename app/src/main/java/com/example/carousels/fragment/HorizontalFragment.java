package com.example.carousels.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.carousels.GlideImageLoader;
import com.example.carousels.R;
import com.example.carousels.databinding.HorizontalFragmentBinding;
import com.github.carousels.Carousels;
import com.github.carousels.CarouselsConstants;
import com.github.carousels.bean.Page;
import com.github.carousels.bean.Type;
import com.github.carousels.callback.OnCarouselsPageClickListener;
import com.github.carousels.callback.OnCarouselsPageLongClickListener;
import com.github.carousels.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhichao
 * @date 20-9-16
 */
public class HorizontalFragment extends Fragment {

    private HorizontalFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HorizontalFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupCarousels1();
        setupCarousels2();
    }

    private void setupCarousels2() {
        ArrayList<Page<?, Type>> pages = new ArrayList<>();
        pages.add(new Page<Object, Type>("https://wanandroid.com/blogimgs/bfcf57e5-aa5d-4ca3-9ca9-245dcbfd31e9.png", Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>("https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png", Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>("https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png", Type.TYPE_IMAGE));
        pages.add(new Page<Object, Type>("https://www.wanandroid.com/blogimgs/90c6cc12-742e-4c9f-b318-b912f163b8d0.png", Type.TYPE_IMAGE));
        final List<String> titles = new ArrayList<>();
        titles.add("【Android开发教程】高级UI：自定义ViewGroup与UI性能优化");
        titles.add("我们新增了一个常用导航Tab~");
        titles.add("一起来做个App吧");
        titles.add("flutter 中文社区");
        binding.carousels2.pageList(pages)
                .imageLoader(new GlideImageLoader())
                .scrollDuration(CarouselsConstants.DEFAULT_SCROLL_DURATION)
                .pageIndicator(binding.pageIndicator2)
                .loopMode(Carousels.LoopMode.REVERSE)
                .startIndex(2)
                .offscreenPageLimit(3)
                .addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        binding.title2.setText(titles.get(position));
                    }
                })
                .autoPlay(true)
                .setOnCarouselsPageLongClickListener(new OnCarouselsPageLongClickListener() {
                    @Override
                    public boolean onCarouselsPageLongClicked(int position) {
                        Toast.makeText(getContext(), "long click " + position, Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .setOnCarouselsPageClickListener(new OnCarouselsPageClickListener() {
                    @Override
                    public void onCarouselsPageClicked(int position) {
                        Toast.makeText(getContext(), "click " + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .start();
    }

    private void setupCarousels1() {
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
        binding.carousels2.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.carousels.stopAutoPlay();
        binding.carousels2.stopAutoPlay();
    }

    public static HorizontalFragment newInstance() {

        Bundle args = new Bundle();

        HorizontalFragment fragment = new HorizontalFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
