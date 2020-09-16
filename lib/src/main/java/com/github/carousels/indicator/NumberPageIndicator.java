package com.github.carousels.indicator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.github.carousels.viewpager.ViewPagerContainer;

import org.jetbrains.annotations.Nullable;

/**
 * 数字页面指示器
 *
 * @author wangzhichao
 * @date 2020-9-11
 */
public class NumberPageIndicator extends View implements PageIndicator {
    public NumberPageIndicator(Context context) {
        super(context);
    }

    public NumberPageIndicator(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPageIndicator(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setViewPager(@Nullable ViewPagerContainer view) {

    }

    @Override
    public void setViewPager(@Nullable ViewPagerContainer view, int initialPosition) {

    }

    @Override
    public void setCurrentItem(int item) {

    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    public void setOrientation(int orientation) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setPageCount(int pageCount) {

    }
}
