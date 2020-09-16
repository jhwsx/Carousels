package com.github.carousels.viewpager

import android.graphics.drawable.Drawable
import androidx.viewpager.widget.PagerAdapter


/**
 * [ViewPager] 和 [VerticalViewPager] 的统一接口
 *
 * @author wangzhichao
 * @date 20-9-9
 */
interface ViewPagerContainer {
    fun setCurrentItem(item: Int, smoothScroll: Boolean)
    fun setCurrentItem(item: Int)
    fun getCurrentItem(): Int
    @Deprecated("this function is deprecated!", ReplaceWith("addOnPageChangeListener"))
    fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener?)
    fun addOnPageChangeListener(listener: ViewPager.OnPageChangeListener)
    fun removeOnPageChangeListener(listener: ViewPager.OnPageChangeListener)
    fun clearOnPageChangeListeners()
    fun setAdapter(adapter: PagerAdapter?)
    fun getAdapter(): PagerAdapter?
    fun fakeDragBy(xOffset: Float)
    fun endFakeDrag()
    fun beginFakeDrag(): Boolean
    fun isFakeDragging(): Boolean
    fun setPageTransformer(reverseDrawingOrder: Boolean, transformer: ViewPager.PageTransformer?)
    fun getOffscreenPageLimit(): Int
    fun setOffscreenPageLimit(limit: Int)
    fun setPageMargin(marginPixels: Int)
    fun getPageMargin(): Int
    fun setPageMarginDrawable(d: Drawable?)
    fun setPageMarginDrawable(resId: Int)
}