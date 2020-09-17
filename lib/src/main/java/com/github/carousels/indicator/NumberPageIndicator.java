package com.github.carousels.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.carousels.R;
import com.github.carousels.viewpager.ViewPager;

import org.jetbrains.annotations.NotNull;

/**
 * 数字页面指示器
 *
 * @author wangzhichao
 * @date 2020-9-11
 */
public class NumberPageIndicator extends BasePageIndicator {

    private static final String TAG = "NumberPageIndicator";

    private String separator;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint.FontMetrics fontMetrics;
    private Rect rect = new Rect();
    public NumberPageIndicator(Context context) {
        this(context, null);
    }

    public NumberPageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.vpiNumberPageIndicatorStyle);
    }

    public NumberPageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) return;
        Resources resources = getResources();
        int defaultPageTextColor = ContextCompat.getColor(context, R.color.default_number_indicator_page_text_color);
        float defaultPageTextSize = resources.getDimension(R.dimen.default_number_indicator_page_text_size);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPageIndicator, defStyleAttr, 0);
        int pageTextColor = a.getColor(R.styleable.NumberPageIndicator_pageTextColor, defaultPageTextColor);
        float pageTextSize = a.getDimension(R.styleable.NumberPageIndicator_pageTextSize, defaultPageTextSize);
        separator = a.getString(R.styleable.NumberPageIndicator_separator);

        paint.setDither(true);
        paint.setColor(pageTextColor);
        paint.setTextSize(pageTextSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);

        fontMetrics = paint.getFontMetrics();

        Drawable background = a.getDrawable(R.styleable.NumberPageIndicator_android_background);
        if (background != null) {
            setBackground(background);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String text = getText();
        paint.getTextBounds(text, 0, text.length(), rect);
        float textWidth = paint.measureText(text);
        float textHeight = rect.height();
        int width = (int) (getPaddingStart() + textWidth + getPaddingEnd());
        int height = (int) (getPaddingTop() + textHeight + getPaddingBottom());
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long start = System.currentTimeMillis();
        String text = getText();
        float baseline = getHeight() * 0.5f - (fontMetrics.top + fontMetrics.bottom) * 0.5f;
        canvas.drawText(text, getWidth() * 0.5f, baseline, paint);
        long end = System.currentTimeMillis();
        Log.d(TAG, "onDraw: cost=" + (end - start));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        invalidate();
    }

    // ---------------- 状态保存与恢复 start ----------------------
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = currentPage;
        return savedState;
    }
    // ---------------- 状态保存与恢复 end  ----------------------

    @NotNull
    private String getText() {
        return (currentPage + 1) + separator + pageCount;
    }
}
