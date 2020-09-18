package com.github.carousels

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.github.carousels.bean.Page
import com.github.carousels.bean.Type
import com.github.carousels.callback.ImageLoader
import com.github.carousels.indicator.PageIndicator
import com.github.carousels.util.WeakHandler
import com.github.carousels.util.logd
import com.github.carousels.viewpager.VerticalViewPager
import com.github.carousels.viewpager.ViewPager
import com.github.carousels.viewpager.ViewPagerContainer
import java.util.*

/**
 * 轮播图
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
class Carousels @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
    private var scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
    private val handler = WeakHandler()
    private var viewPagerContainer: ViewPagerContainer
    private var pageCount = 0
    private var autoPlay: Boolean
    private var scrollDuration: Int
    private var loopMode: LoopMode

    // 不支持方法设置
    private var orientation: Int = HORIZONTAL
    private var delayTime: Long = DEFAULT_DELAY_TIME
    private val pageList: MutableList<Page<*, Type>> = mutableListOf()
    private var imageLoader: ImageLoader? = null
    private var pageIndicator: PageIndicator? = null
    private var startIndex: Int = 0
    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Carousels)
        orientation = ta.getInt(R.styleable.Carousels_carousels_orientation, 0)
        inflate(
            context,
            if (orientation > 0) R.layout.carousels_vertical else R.layout.carousels,
            this
        )
        viewPagerContainer = findViewById(R.id.carousels_view_pager)
        val imageScaleTypeIndex: Int = ta.getInt(R.styleable.Carousels_carousels_imageScaleType, -1)
        if (imageScaleTypeIndex >= 0) {
            scaleType = sScaleTypeArray[imageScaleTypeIndex]
        }
        autoPlay = ta.getBoolean(R.styleable.Carousels_carousels_autoPlay, false)
        scrollDuration = ta.getInt(
            R.styleable.Carousels_carousels_scrollDuration,
            DEFAULT_SCROLL_DURATION
        )
        val loopModeIndex = ta.getInt(
            R.styleable.Carousels_carousels_loopMode,
            LoopMode.RESTART.ordinal
        )
        loopMode = when (loopModeIndex) {
            0 -> LoopMode.RESTART
            else -> LoopMode.REVERSE
        }
        delayTime = ta.getInt(R.styleable.Carousels_carousels_delayTime, DEFAULT_DELAY_TIME.toInt())
            .toLong()
        ta.recycle()
        setupViewPagerScroller()
    }

    private fun setupViewPagerScroller() {
        val clazz =
            if (orientation == HORIZONTAL) ViewPager::class.java else VerticalViewPager::class.java
        val mScrollerField = clazz.getDeclaredField("mScroller")
        mScrollerField.isAccessible = true
        mScrollerField.set(viewPagerContainer, CarouselsScroller(context).apply {
            scrollDuration = this@Carousels.scrollDuration
        })
    }

    private var currPageIndex = 0
    private var positiveDirection = true
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (!autoPlay || pageCount <= 1) {
                return
            }
            when (loopMode) {
                LoopMode.RESTART -> {
                    currPageIndex = currPageIndex % (pageCount + 1) + 1
                }
                LoopMode.REVERSE -> {
                    if (currPageIndex == pageCount - 1) {
                        positiveDirection = false
                    }
                    if (currPageIndex == 0) {
                        positiveDirection = true
                    }
                    if (positiveDirection) {
                        currPageIndex++
                    } else {
                        currPageIndex--
                    }
                }
            }
            if (currPageIndex == 1 && loopMode == LoopMode.RESTART) {
                logd(TAG, "currPageIndex=1")
                viewPagerContainer.setCurrentItem(currPageIndex, false)
                handler.post(this)
            } else {
                logd(TAG, "currPageIndex=$currPageIndex")
                viewPagerContainer.setCurrentItem(currPageIndex)
                handler.postDelayed(this, delayTime)
            }
        }
    }


    fun start() {
        if (pageList.isEmpty()) {
            return
        }
        val viewList: MutableList<View> = arrayListOf()
        when (loopMode) {
            LoopMode.RESTART -> {
                for (i in 0..pageCount + 1) {
                    val index = when (i) {
                        0 -> pageCount - 1
                        pageCount + 1 -> 0
                        else -> i - 1
                    }
                    val page = pageList[index]
                    val view = page2View(page)
                    viewList.add(view)
                }
            }
            LoopMode.REVERSE -> {
                for (i in 0..pageCount - 1) {
                    val page = pageList[i]
                    val view = page2View(page)
                    viewList.add(view)
                }
            }
        }

        val adapter = CarouselsPagerAdapter(viewList)
        viewPagerContainer.addOnPageChangeListener(this)
        viewPagerContainer.setAdapter(adapter)
        when (loopMode) {
            LoopMode.RESTART -> {
                // 默认选中 1 号索引，即是真正的 0 号索引
                viewPagerContainer.setCurrentItem(startIndex + 1)
            }
            LoopMode.REVERSE -> {
                viewPagerContainer.setCurrentItem(startIndex)
            }
        }

        if (pageIndicator != null) {
            pageIndicator!!.setViewPager(viewPagerContainer)
            pageIndicator!!.setPageCount(pageCount)
        }
        if (autoPlay) {
            startAutoPlay()
        }
    }

    private fun page2View(page: Page<*, Type>) = when (page.type) {
        Type.TYPE_VIEW -> page.obj as View
        Type.TYPE_IMAGE -> {
            ImageView(context).apply {
                scaleType = this@Carousels.scaleType
                if (imageLoader != null) {
                    imageLoader!!.loadImage(context, page.obj, this)
                }
            }
        }
    }

    fun startAutoPlay() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delayTime)
    }

    fun stopAutoPlay() {
        handler.removeCallbacks(runnable)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (autoPlay) {
            when (ev.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                    startAutoPlay()
                }
                MotionEvent.ACTION_DOWN -> {
                    stopAutoPlay()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    enum class LoopMode {
        RESTART, REVERSE
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        logd(
            TAG,
            "onPageScrolled, position=$position, positionOffset=$positionOffset, positionOffsetPixels=$positionOffsetPixels"
        )
        if (mOnPageChangeListeners != null) {
            var i = 0
            val z = mOnPageChangeListeners!!.size
            while (i < z) {
                val listener =
                    mOnPageChangeListeners!![i]
                listener.onPageScrolled(toRealPosition(position).also {
                    logd(TAG, "onPageScrolled, realPosition=$it, position=$position")
                }, positionOffset, positionOffsetPixels)
                i++
            }
        }
    }

    override fun onPageSelected(position: Int) {
        currPageIndex = position
        dispatchOnPageSelected(toRealPosition(position), position)
    }


    private fun dispatchOnPageSelected(realPosition: Int, position: Int) {
        if (mOnPageChangeListeners != null) {
            var i = 0
            val z = mOnPageChangeListeners!!.size
            while (i < z) {
                val listener =
                    mOnPageChangeListeners!![i]
                listener.onPageSelected(realPosition.also {
                    logd(TAG, "onPageSelected, realPosition=$it, position=$position")
                })
                i++
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        logd(TAG, "onPageScrollStateChanged, state=$state")
        if (loopMode == LoopMode.RESTART) {
            when (state) {
                ViewPager.SCROLL_STATE_IDLE -> {
                    if (currPageIndex == 0) {
                        viewPagerContainer.setCurrentItem(pageCount, false)
                    } else if (currPageIndex == pageCount + 1) {
                        viewPagerContainer.setCurrentItem(1, false)
                    }
                }
                ViewPager.SCROLL_STATE_DRAGGING -> {
                    if (currPageIndex == pageCount + 1) {
                        viewPagerContainer.setCurrentItem(1, false)
                    } else if (currPageIndex == 0) {
                        viewPagerContainer.setCurrentItem(pageCount, false)
                    }
                }
            }
        }
        if (mOnPageChangeListeners != null) {
            var i = 0
            val z = mOnPageChangeListeners!!.size
            while (i < z) {
                val listener =
                    mOnPageChangeListeners!![i]
                listener.onPageScrollStateChanged(state)
                i++
            }
        }
    }

    fun scaleType(scaleType: ImageView.ScaleType): Carousels = apply {
        this.scaleType = scaleType
    }

    fun autoPlay(autoPlay: Boolean): Carousels = apply {
        this.autoPlay = autoPlay
    }

    fun scrollDuration(scrollDuration: Int): Carousels = apply {
        this.scrollDuration = scrollDuration
    }

    fun loopMode(loopMode: LoopMode) = apply {
        this.loopMode = loopMode
    }

    fun delayTime(delayTime: Long) = apply {
        this.delayTime = delayTime
    }

    fun pageList(pageList: List<Page<*, Type>>) = apply {
        this.pageList.clear()
        this.pageList.addAll(pageList)
        this.pageCount = pageList.size
    }

    fun imageLoader(imageLoader: ImageLoader) = apply {
        this.imageLoader = imageLoader
    }

    fun pageIndicator(pageIndicator: PageIndicator) = apply {
        this.pageIndicator = pageIndicator
    }

    fun startIndex(index: Int) = apply {
        this.startIndex = index
    }

    fun offscreenPageLimit(limit: Int) = apply {
        viewPagerContainer.setOffscreenPageLimit(limit)
    }

    fun pageTransformer(reverseDrawingOrder: Boolean,
                        transformer: ViewPager.PageTransformer) = apply {
        viewPagerContainer.setPageTransformer(reverseDrawingOrder, transformer)
    }

    fun pageMargin(marginPixels: Int) = apply {
        viewPagerContainer.setPageMargin(marginPixels)
    }

    private var mOnPageChangeListeners: MutableList<ViewPager.OnPageChangeListener>? = null

    fun addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) = apply {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = ArrayList<ViewPager.OnPageChangeListener>()
        }
        mOnPageChangeListeners!!.add(listener)
    }

    /**
     * Remove a listener that was previously added via
     * [.addOnPageChangeListener].
     *
     * @param listener listener to remove
     */
    fun removeOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners!!.remove(listener)
        }
    }

    /**
     * Remove all listeners that are notified of any changes in scroll state or position.
     */
    fun clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners!!.clear()
        }
    }

    private fun toRealPosition(position: Int): Int {
        if (loopMode == LoopMode.RESTART) {
            var result = 0
            if (pageCount != 0) {
                result = (position - 1) % pageCount
            }
            if (result < 0) {
                result += pageCount
            }
            return result
        } else {
            return position
        }
    }

    companion object {
        private const val TAG = "Carousels"
        private val sScaleTypeArray = arrayOf(
            ImageView.ScaleType.MATRIX,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE
        )
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}