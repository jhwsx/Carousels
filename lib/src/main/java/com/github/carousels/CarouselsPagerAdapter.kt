@file:JvmName("CarouselsConstants")
package com.github.carousels

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.viewpager.widget.PagerAdapter
import com.github.carousels.util.logd

/**
 * 轮播图页面适配器
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
class CarouselsPagerAdapter(private val list: List<View>) : PagerAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        logd(TAG, "instantiateItem: $position")
        val view = list[position]
        val parent: ViewParent? = view.parent
        if (parent != null && parent is ViewGroup) {
            parent.removeView(view)
        }
//        val mlp: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT)
//        mlp.bottomMargin = 8
//        mlp.topMargin = 8
//        mlp.leftMargin = 8
//        mlp.rightMargin = 8
//        container.addView(view, mlp)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        logd(TAG, "destroyItem: $position")
        container.removeView(obj as View)
    }

    companion object {
        private const val TAG = "CarouselsPagerAdapter"
    }
}