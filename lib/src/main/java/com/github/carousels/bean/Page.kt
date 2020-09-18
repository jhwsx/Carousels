package com.github.carousels.bean

/**
 * 页面封装类
 *
 * @author wangzhichao
 * @date 2020-9-9
 */
data class Page<O, T>(val obj: O, val type: T = Type.TYPE_IMAGE as T) where T : Type