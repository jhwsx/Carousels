package com.github.carousels.callback

import android.content.Context
import android.widget.ImageView

/**
 * 图片加载接口
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
interface ImageLoader {

    fun createImageView(context: Context): ImageView {
        return ImageView(context)
    }

    fun <T> loadImage(context: Context, model: T, view: ImageView)
}