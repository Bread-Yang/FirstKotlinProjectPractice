package com.robin.firstkotlinproject.ui.custom

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Robin Yang on 11/20/17.
 */
class PaddingItemDecoration internal constructor(private val m_space: Int)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect.apply {
            left = m_space
            right = m_space
            top = m_space
            bottom = m_space
        }
    }
}