package com.github.carousels.indicator;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import com.github.carousels.Carousels;
import com.github.carousels.viewpager.ViewPager;
import com.github.carousels.viewpager.ViewPagerContainer;

/**
 * 页面指示器的抽象基类
 *
 * 提供了 {@link PageIndicator} 接口的默认实现
 *
 * @author wangzhichao
 * @date 2020-9-16
 */
public abstract class BasePageIndicator extends View implements PageIndicator {
    protected ViewPagerContainer viewPager;
    protected int currentPage;
    protected int scrollState;
    protected int pageCount;
    protected float pageOffset;

    public BasePageIndicator(Context context) {
        super(context);
    }

    public BasePageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setViewPager(ViewPagerContainer view) {
        if (viewPager == view) {
            return;
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        viewPager = view;
        ViewParent parent = ((View) viewPager).getParent();
        if (parent instanceof Carousels) {
            ((Carousels) parent).addOnPageChangeListener(this);
        }
        invalidate();
    }

    @Override
    public void setViewPager(ViewPagerContainer view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(item);
        currentPage = item;
        invalidate();
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    // ------------- 来自 ViewPager 的 OnPageChangeListener 的监听回调 start ------------
    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;
    }

    /**
     * 对于需要滑动效果的页面指示器，保持默认实现就可以。
     * 对于不需要滑动效果的页面指示器，此方法需要空实现。
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPage = position;
        // positionOffset 的取值范围是 0 到 1.
        pageOffset = positionOffset;
        if (currentPage == pageCount - 1) {
            pageOffset = 0f;
            if (positionOffset >= 0.5f) {
                currentPage = 0;
            }
        }
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            currentPage = position;
            invalidate();
        }
    }
    // ------------- 来自 ViewPager 的 OnPageChangeListener 的监听回调 end   ------------

    @Override
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * 是否满足绘制条件
     * @return true，表示不可以绘制；反之，表示可以。
     */
    protected boolean cannotDraw() {
        if (viewPager == null) {
            return true;
        }
        if (viewPager.getAdapter() == null) {
            return true;
        }
        final int count = pageCount;
        return count == 0;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration")
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
