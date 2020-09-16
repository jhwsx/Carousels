package com.github.carousels.indicator

import com.github.carousels.viewpager.ViewPager
import com.github.carousels.viewpager.ViewPagerContainer

/**
 * A PageIndicator is responsible to show an visual indicator on the total views
 * number and the current visible view.
 */
interface PageIndicator : ViewPager.OnPageChangeListener {
    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view
     */
    fun setViewPager(view: ViewPagerContainer?)

    /**
     * Bind the indicator to a ViewPager.
     *
     * @param view
     * @param initialPosition
     */
    fun setViewPager(view: ViewPagerContainer?, initialPosition: Int)

    /**
     *
     * Set the current page of both the ViewPager and indicator.
     *
     *
     * This **must** be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).
     *
     * @param item
     */
    fun setCurrentItem(item: Int)

    /**
     * Notify the indicator that the fragment list has changed.
     */
    fun notifyDataSetChanged()

    fun setOrientation(orientation: Int)

    fun setPageCount(pageCount: Int)
}