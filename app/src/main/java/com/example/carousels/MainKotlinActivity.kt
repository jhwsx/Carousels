package com.example.carousels

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.carousels.databinding.MainKotlinActivityBinding
import com.github.carousels.bean.Page
import com.github.carousels.bean.Type

/**
 * Kotlin 版本代码
 * @author wangzhichao
 * @date 2020-9-10
 */
class MainKotlinActivity : AppCompatActivity() {
    private lateinit var binding: MainKotlinActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainKotlinActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adView = LayoutInflater.from(this).inflate(R.layout.ad_layout, null)

        binding.carousels.pageList(arrayListOf(
            Page(R.drawable.img_1),
            Page(R.drawable.img_2),
            Page(R.drawable.img_3),
            Page(adView, Type.TYPE_VIEW),
        ))
            .imageLoader(GlideImageLoader())
            .scrollDuration(5000)
            .start()
    }

    override fun onResume() {
        super.onResume()
        binding.carousels.startAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        binding.carousels.stopAutoPlay()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainKotlinActivity::class.java)
            context.startActivity(starter)
        }
    }
}