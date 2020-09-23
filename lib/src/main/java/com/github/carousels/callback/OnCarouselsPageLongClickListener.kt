package com.github.carousels.callback

/**
 * Carousels 页面常按点击监听器
 *
 * @author wangzhichao
 * @date 2020-9-23
 */
interface OnCarouselsPageLongClickListener {
    fun onCarouselsPageLongClicked(position: Int): Boolean
}