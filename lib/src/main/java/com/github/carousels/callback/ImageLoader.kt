package com.github.carousels.callback

import android.content.Context
import android.widget.ImageView

/**
 *
 *
 * @author wangzhichao
 * @date 20-9-9
 */
interface ImageLoader {

    fun createImageView(context: Context): ImageView {
        return ImageView(context)
    }

    fun <T> loadImage(context: Context, model: T, view: ImageView)
}