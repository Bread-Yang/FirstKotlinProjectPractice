package com.robin.firstkotlinproject.ui.screens.main

import android.support.design.widget.AppBarLayout
import android.support.design.widget.AppBarLayout.LayoutParams.*
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.robin.firstkotlinproject.R
import com.robin.firstkotlinproject.ui.activity.ActivityAnkoComponent
import com.robin.firstkotlinproject.ui.custom.AutofitRecyclerView
import com.robin.firstkotlinproject.ui.custom.autoFitRecycler
import com.robin.firstkotlinproject.ui.screens.style
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.matchParent

/**
 * Created by Robin Yang on 10/30/17.
 */
class MainLayout : ActivityAnkoComponent<MainActivity> {

    lateinit var recycler: RecyclerView
    override lateinit var toolbar: Toolbar

    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {

        coordinatorLayout {

            appBarLayout {
                toolbar = themedToolbar(R.style.ThemeOverlay_AppCompat_Dark_ActionBar) {
                    backgroundResource = R.color.primary
                }.lparams(width = matchParent) {
                    scrollFlags = SCROLL_FLAG_SNAP or SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
                }
            }.lparams(width = matchParent)

            recycler = autoFitRecycler()
                    .apply(AutofitRecyclerView::style)
                    .lparams(matchParent, matchParent) {
                        behavior = AppBarLayout.ScrollingViewBehavior()
                    }
        }
    }
}