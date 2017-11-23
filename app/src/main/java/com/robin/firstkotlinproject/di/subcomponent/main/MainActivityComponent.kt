package com.robin.firstkotlinproject.di.subcomponent.main

import com.robin.firstkotlinproject.ui.screens.main.MainActivity
import com.robin.firstkotlinproject.di.scope.ActivityScope
import dagger.Subcomponent

/**
 * Created by Robin Yang on 10/30/17.
 */
@ActivityScope
@Subcomponent(modules = arrayOf(
        MainActivityModule::class
))
interface MainActivityComponent {

    fun injectTo(activity: MainActivity)
}