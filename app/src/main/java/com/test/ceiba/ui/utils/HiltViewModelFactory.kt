/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 4:59 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory

/**
 * Creates a [ViewModelProvider.Factory] to get
 * [HiltViewModel](https://dagger.dev/api/latest/dagger/hilt/android/lifecycle/HiltViewModel)
 * -annotated `ViewModel` from a [NavBackStackEntry].
 *
 * @param context the activity context.
 * @param navBackStackEntry the navigation back stack entry.
 * @return the factory.
 * @throws IllegalStateException if the context given is not an activity.
 */
@JvmName("create")
public fun HiltViewModelFactory(
    context: Context,
    navBackStackEntry: NavBackStackEntry
): ViewModelProvider.Factory {
    val activity = context.let {
        var ctx = it
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return@let ctx
            }
            ctx = ctx.baseContext
        }
        throw IllegalStateException(
            "Expected an activity context for creating a HiltViewModelFactory for a " +
                "NavBackStackEntry but instead found: $ctx"
        )
    }
    return HiltViewModelFactory.createInternal(
        activity,
        navBackStackEntry,
        navBackStackEntry.arguments,
        navBackStackEntry.defaultViewModelProviderFactory,
    )
}
