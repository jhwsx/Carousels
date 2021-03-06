@file:JvmName("CarouselsConstants")

package com.github.carousels

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.viewpager.widget.PagerAdapter
import com.github.carousels.util.CarouselsLog

/**
 * 轮播图页面适配器
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
class CarouselsPagerAdapter(private val carousels: Carousels, private val list: List<View>) :
    PagerAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        CarouselsLog.d(TAG, "instantiateItem: $position")
        val view = list[position]
        val parent: ViewParent? = view.parent
        if (parent != null && parent is ViewGroup) {
            parent.removeView(view)
        }
        container.addView(view)

        view.setOnClickListener {
            carousels.onCarouselsPageClickListener?.onCarouselsPageClicked(
                carousels.toRealPosition(
                    position
                )
            )
        }
        view.setOnLongClickListener {
            carousels.onCarourselsPageLongClickListener?.onCarouselsPageLongClicked(
                carousels.toRealPosition(
                    position
                )
            ) ?: false
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        CarouselsLog.d(TAG, "destroyItem: $position")
        container.removeView(obj as View)
    }

    companion object {
        private const val TAG = "CarouselsPagerAdapter"
    }
}