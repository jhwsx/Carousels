package com.github.carousels.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.github.carousels.R;


import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

/**
 * 线条页面指示器
 *
 * @author wangzhichao
 * @date 2020-9-11
 */
public class LinePageIndicator extends BasePageIndicator {

    private static final String TAG = "LinePageIndicator";

    private Paint paintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int orientation;
    private float lineLength;
    private float lineGap;
    private float strokeWidth;

    public LinePageIndicator(Context context) {
        this(context, null);
    }


    public LinePageIndicator(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.vpiLinePageIndicatorStyle);
    }

    public LinePageIndicator(Context context, @androidx.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) return;

        Resources resources = getResources();
        int defaultSelectedColor = ContextCompat.getColor(context, R.color.default_line_indicator_selected_color);
        int defaultUnselectedColor = ContextCompat.getColor(context, R.color.default_line_indicator_unselected_color);
        float defaultLineGap = resources.getDimension(R.dimen.default_line_indicator_gap_width);
        float defaultLineLength = resources.getDimension(R.dimen.default_line_indicator_line_width);
        float defaultStrokeWidth = resources.getDimension(R.dimen.default_line_indicator_stroke_width);
        int defaultOrientation = resources.getInteger(R.integer.default_line_indicator_orientation);
        boolean defaultLineRoundCap = resources.getBoolean(R.bool.defalut_line_indicator_line_round_cap);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator, defStyleAttr, 0);
        orientation = a.getInt(R.styleable.LinePageIndicator_android_orientation, defaultOrientation);
        strokeWidth = a.getDimension(R.styleable.LinePageIndicator_strokeWidth, defaultStrokeWidth);
        boolean lineRoundCap = a.getBoolean(R.styleable.LinePageIndicator_lineRoundCap, defaultLineRoundCap);
        // 选中颜色的画笔
        paintSelected.setColor(a.getColor(R.styleable.LinePageIndicator_selectedColor, defaultSelectedColor));
        paintSelected.setDither(true);
        paintSelected.setStrokeWidth(strokeWidth);
        paintSelected.setStrokeCap(lineRoundCap ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        // 未选中颜色的画笔
        paintUnselected.setColor(a.getColor(R.styleable.LinePageIndicator_unselectedColor, defaultUnselectedColor));
        paintUnselected.setDither(true);
        paintUnselected.setStrokeWidth(strokeWidth);
        paintUnselected.setStrokeCap(lineRoundCap ? Paint.Cap.ROUND : Paint.Cap.BUTT);

        lineLength = a.getDimension(R.styleable.LinePageIndicator_lineLength, defaultLineLength);
        lineGap = a.getDimension(R.styleable.LinePageIndicator_lineGap, defaultLineGap);

        Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
        if (background != null) {
            setBackground(background);
        }

        a.recycle();
    }

    //--------------- 测量过程 start --------------------
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec, getPaddingStart(), getPaddingEnd()),
                    measureShort(heightMeasureSpec, getPaddingTop(), getPaddingBottom()));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec, getPaddingStart(), getPaddingEnd()),
                    measureLong(heightMeasureSpec, getPaddingTop(),getPaddingBottom()));
        }

    }

    private int measureLong(int measureSpec, int paddingBefore, int paddingAfter) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if ((specMode == MeasureSpec.EXACTLY) || (viewPager == null)) {
            result = specSize;
        } else {
            final int count = pageCount;
            result = (int) (paddingBefore + paddingAfter + count * lineLength + (count - 1) * lineGap);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureShort(int measureSpec, int paddingBefore, int paddingAfter) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (paddingBefore + paddingAfter + strokeWidth);
        }
        return result;
    }
    // ---------------- 测量过程 end   ----------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cannotDraw())  return;
        int count = pageCount;
        if (currentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }
        int longPaddingBefore;
        int shortPaddingBefore;
        if (orientation == HORIZONTAL) {
            longPaddingBefore = getPaddingStart();
            shortPaddingBefore = getPaddingTop();
        } else {
            longPaddingBefore = getPaddingTop();
            shortPaddingBefore = getPaddingStart();
        }
        // 长边的起点
        float longOffset = longPaddingBefore;
        if (paintSelected.getStrokeCap() != Paint.Cap.BUTT) {
            longOffset += strokeWidth / 2f;
        }
        float shortOffset = shortPaddingBefore + strokeWidth / 2f;

        // 绘制未选中的页面指示器
        float startX;
        float startY;
        float stopX;
        float stopY;
        float lineLengthAndGap = lineLength + lineGap;
        float realLineLength = lineLength;
        if (paintSelected.getStrokeCap() != Paint.Cap.BUTT) {
            realLineLength -= strokeWidth;
        }
        for (int i = 0; i < count; i++) {
            float drawLong = longOffset + i * lineLengthAndGap;
            if (orientation == HORIZONTAL) {
                startX = drawLong;
                startY = shortOffset;
                stopX = startX + realLineLength;
                stopY = startY;
            } else {
                startX = shortOffset;
                startY = drawLong;
                stopX = startX;
                stopY = startY + realLineLength;
            }
            canvas.drawLine(startX, startY, stopX, stopY, paintUnselected);
        }
        // 绘制选中的页面指示器
        Log.d(TAG, "onDraw: currentPage="+currentPage);
        float currStartX;
        float currStartY;
        float currStopX;
        float currStopY;
        if (orientation == HORIZONTAL) {
            currStartX = longOffset + currentPage * lineLengthAndGap;
            currStartX += pageOffset * lineLengthAndGap;
            currStartY = shortOffset;
            currStopX = currStartX + realLineLength;
            currStopY = currStartY;
        } else {
            currStartX = shortOffset;
            currStartY = longOffset + currentPage * lineLengthAndGap;
            currStartY += pageOffset * lineLengthAndGap;
            currStopX = currStartX;
            currStopY = currStartY + realLineLength;
        }
        canvas.drawLine(currStartX, currStartY, currStopX, currStopY, paintSelected);
    }

    // ---------------- 状态保存与恢复 start ----------------------
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState)state;
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
    public int getOrientation() {
        return orientation;
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
        }    }

    public float getLineLength() {
        return lineLength;
    }

    public void setLineLength(float lineLength) {
        this.lineLength = lineLength;
        invalidate();
    }

    public float getLineGap() {
        return lineGap;
    }

    public void setLineGap(float lineGap) {
        this.lineGap = lineGap;
        invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    public void setSelectedColor(@ColorInt int selectedColor) {
        paintSelected.setColor(selectedColor);
        invalidate();
    }

    public @ColorInt int getSelectedColor() {
        return paintSelected.getColor();
    }

    public void setUnselectedColor(@ColorInt int unselectedColor) {
        paintUnselected.setColor(unselectedColor);
        invalidate();
    }

    public @ColorInt int getUnselectedColor() {
        return paintUnselected.getColor();
    }
}
