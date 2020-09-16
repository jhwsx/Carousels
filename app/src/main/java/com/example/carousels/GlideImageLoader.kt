package com.example.carousels

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.carousels.callback.ImageLoader

/**
 *
 *
 * @author wangzhichao
 * @date 20-9-9
 */
class GlideImageLoader : ImageLoader {
    override fun <T> loadImage(context: Context, model: T, view: ImageView) {
        Glide.with(view)
            .load(model)
            .into(view)
    }
}