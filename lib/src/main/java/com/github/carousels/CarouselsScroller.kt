package com.github.carousels

import android.content.Context
import android.os.Build
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * 轮播图 Scroller
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
class CarouselsScroller(
    context: Context,
    interpolator: Interpolator? = null,
    flywheel: Boolean = context.applicationInfo.targetSdkVersion >= Build.VERSION_CODES.HONEYCOMB
) :
    Scroller(context, interpolator, flywheel) {

    var scrollDuration = DEFAULT_SCROLL_DURATION

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }
}