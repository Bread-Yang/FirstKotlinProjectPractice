package com.robin.firstkotlinproject.ui.custom

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import kotlin.properties.Delegates

/**
 * Created by Robin Yang on 10/30/17.
 */
class AutofitRecyclerView : RecyclerView {

    /**
     * notNull解释:
     *
     *  var max: Int by Delegates.notNull()
     *  // println(max) // will fail with IllegalStateException
     *  max = 10
     *  println(max) // 10
     */
    private var manager: GridLayoutManager by Delegates.notNull()
    var columnWidth = -1

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet? = null) {
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val ta = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = ta.getDimensionPixelSize(0, -1)
            ta.recycle()
        }

        manager = GridLayoutManager(context, 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            val spanCount = Math.max(1, measuredWidth / columnWidth)
            manager.spanCount = spanCount
        }
    }
}

fun ViewManager.autoFitRecycler(theme: Int = 0) = autoFitRecycler(theme) {}

// Kotlin 中 双冒号操作符 表示把一个方法当做一个参数，传递到另一个方法中进行使用，通俗的来讲就是引用一个方法
inline fun ViewManager.autoFitRecycler(theme: Int = 0,
                                       init: AutofitRecyclerView.() -> Unit) =
        ankoView(::AutofitRecyclerView, theme, init)