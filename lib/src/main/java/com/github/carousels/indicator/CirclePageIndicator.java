package com.github.carousels.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.github.carousels.R;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 * 绘制指示点（一个 View 对应一个指示点）。当前 View 的位置的指示点是填充的，其他位置的指示点是描边的。
 *
 * @author wangzhichao
 * @date 2020-9-11
 */
public class CirclePageIndicator extends BasePageIndicator {

    private float radius;
    private final Paint paintPageFill = new Paint(ANTI_ALIAS_FLAG);
    private final Paint paintStroke = new Paint(ANTI_ALIAS_FLAG);
    private final Paint paintFill = new Paint(ANTI_ALIAS_FLAG);
    private int orientation;
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
        if (cannotDraw()) return;
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
    // ---------------- 状态保存与恢复 end  ----------------------

}