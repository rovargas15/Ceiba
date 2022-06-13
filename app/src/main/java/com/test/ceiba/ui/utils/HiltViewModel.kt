/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 5:23 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

/**
 * Returns an existing
 * [HiltViewModel](https://dagger.dev/api/latest/dagger/hilt/android/lifecycle/HiltViewModel)
 * -annotated [ViewModel] or creates a new one scoped to the current navigation graph present on
 * the {@link NavController} back stack.
 *
 * If no navigation graph is currently present then the current scope will be used, usually, a
 * fragment or an activity.
 *
 * @sample androidx.hilt.navigation.compose.samples.NavComposable
 * @sample androidx.hilt.navigation.compose.samples.NestedNavComposable
 */
@Composable
inline fun <reified VM : ViewModel> hiltViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): VM {
    val factory = createHiltViewModelFactory(viewModelStoreOwner)
    return viewModel(viewModelStoreOwner, factory = factory)
}

@Composable
@PublishedApi
internal fun createHiltViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner
): ViewModelProvider.Factory? = if (viewModelStoreOwner is NavBackStackEntry) {
    HiltViewModelFactory(
        context = LocalContext.current,
        navBackStackEntry = viewModelStoreOwner
    )
} else {
    // Use the default factory provided by the ViewModelStoreOwner
    // and assume it is an @AndroidEntryPoint annotated fragment or activity
    null
}
