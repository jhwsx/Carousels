package com.github.carousels.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import com.github.carousels.Carousels;
import com.github.carousels.R;
import com.github.carousels.viewpager.ViewPager;
import com.github.carousels.viewpager.ViewPagerContainer;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 * 绘制指示点（一个 View 对应一个指示点）。当前 View 的位置的指示点是填充的，其他位置的指示点是描边的。
 */
public class CirclePageIndicator extends View implements PageIndicator {

    private float radius;
    private final Paint paintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint paintStroke = new Paint(ANTI_ALIAS_FLAG);
    private final Paint paintFill = new Paint(ANTI_ALIAS_FLAG);
    private ViewPagerContainer viewPager;
    private int currentPage;
    private float pageOffset;
    private int scrollState;
    private int orientation;
    private int pageCount;
    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) return;

        // Load defaults from resources
        final Resources res = getResources();
        final int defaultPageColor = res.getColor(R.color.default_circle_indicator_page_color);
        final int defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color);
        final int defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation);
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);
        final float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
        final float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);

        // Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator, defStyle, 0);

        orientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation, defaultOrientation);
        // 表示页的填充色
        paintPageFill.setDither(true);
        paintPageFill.setStyle(Paint.Style.FILL);
        paintPageFill.setColor(a.getColor(R.styleable.CirclePageIndicator_pageColor, defaultPageColor));
        // 表示描边色
        paintStroke.setDither(true);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setColor(a.getColor(R.styleable.CirclePageIndicator_strokeColor, defaultStrokeColor));
        paintStroke.setStrokeWidth(a.getDimension(R.styleable.CirclePageIndicator_strokeWidth, defaultStrokeWidth));
        // 表示当前页的填充色
        paintFill.setDither(true);
        paintFill.setStyle(Paint.Style.FILL);
        paintFill.setColor(a.getColor(R.styleable.CirclePageIndicator_fillColor, defaultFillColor));
        // 表示点的外围半径
        radius = a.getDimension(R.styleable.CirclePageIndicator_radius, defaultRadius);

        Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
        if (background != null) {
          setBackground(background);
        }

        a.recycle();
    }


    public void setPageColor(int pageColor) {
        paintPageFill.setColor(pageColor);
        invalidate();
    }

    public int getPageColor() {
        return paintPageFill.getColor();
    }

    public void setFillColor(int fillColor) {
        paintFill.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return paintFill.getColor();
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL:
            case VERTICAL:
                this.orientation = orientation;
                requestLayout();
                break;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
    }

    public int getOrientation() {
        return orientation;
    }

    public void setStrokeColor(int strokeColor) {
        paintStroke.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return paintStroke.getColor();
    }

    public void setStrokeWidth(float strokeWidth) {
        paintStroke.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return paintStroke.getStrokeWidth();
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    public float getRadius() {
        return radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!canDraw()) {
            return;
        }

        int count = pageCount;
        if (currentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        int longPaddingBefore;
        int shortPaddingBefore;
        if (orientation == HORIZONTAL) {
            longPaddingBefore = getPaddingLeft();
            shortPaddingBefore = getPaddingTop();
        } else {
            longPaddingBefore = getPaddingTop();
            shortPaddingBefore = getPaddingLeft();
        }

        final float threeRadius = radius * 3;
        final float shortOffset = shortPaddingBefore + radius;
        float longOffset = longPaddingBefore + radius;

        float dX;
        float dY;

        float realRadius = radius - paintStroke.getStrokeWidth() / 2.0f;
        float pageFillRadius = realRadius;
        if (paintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= paintStroke.getStrokeWidth() / 2.0f;
        }

        // Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            if (orientation == HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = drawLong;
            }
            // Only paint fill if not completely transparent
            if (paintPageFill.getAlpha() > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, paintPageFill);
            }

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != realRadius) {
                canvas.drawCircle(dX, dY, realRadius, paintStroke);
            }
        }

        //Draw the filled circle according to the current scroll
        // 绘制标识当前滑动的指示点
        float cx = currentPage * threeRadius;
            cx += pageOffset * threeRadius;
        if (orientation == HORIZONTAL) {
            dX = longOffset + cx;
            dY = shortOffset;
        } else {
            dX = shortOffset;
            dY = longOffset + cx;
        }
        canvas.drawCircle(dX, dY, realRadius, paintFill);
    }

    @Override
    public void setViewPager(ViewPagerContainer view) {
        if (viewPager == view) {
            return;
        }
//        if (mViewPager != null) {
//            mViewPager.setOnPageChangeListener(null);
//        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        viewPager = view;
        ViewParent parent = ((View) viewPager).getParent();
        if (parent instanceof Carousels) {
            ((Carousels) parent).addOnPageChangeListener(this);
        }
//        mViewPager.addOnPageChangeListener(this);
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

    //--------------- 测量过程 start --------------------
    /*
     * 测量过程
     *
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec
     *            A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureLong(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (viewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = pageCount;
            // 左 padding + 右 padding + count * 2 * 原点半径 + 间距（count - 1 个原点半径）
            result = (int)(getPaddingLeft() + getPaddingRight()
                    + (count * 2 * radius) + (count - 1) * radius + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec
     *            A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = (int)(2 * radius + getPaddingTop() + getPaddingBottom() + 1);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    // ---------------- 测量过程 end   ----------------


    // ---------------- 状态保存与恢复 start ----------------------
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPage = savedState.currentPage;
//        mSnapPage = savedState.currentPage;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = currentPage;
        return savedState;
    }

    @Override
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
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
    // ---------------- 状态保存与恢复 end  ----------------------

    /**
     * 是否满足绘制条件
     * @return true，表示可以绘制；反之，表示不可以。
     */
    private boolean canDraw() {
        if (viewPager == null) {
            return false;
        }
        if (viewPager.getAdapter() == null) {
            return false;
        }
        final int count = pageCount;
        return count != 0;
    }
}