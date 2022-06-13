/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:14 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class AppDimensions(
    val paddingSmall: Dp = 4.dp,
    val paddingMedium: Dp = 8.dp,
    val paddingLarge: Dp = 12.dp,
    val paddingXLarge: Dp = 16.dp,
    val borderSmall: Dp = 1.dp,
    val imageSmall: Dp = 150.dp,
    val imageMedium: Dp = 200.dp,
    val heightCard: Dp = 200.dp,
    val MinSizeCard: Dp = 250.dp
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }
