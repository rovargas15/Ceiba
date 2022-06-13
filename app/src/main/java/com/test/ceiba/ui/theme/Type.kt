/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:14 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 22.sp,
        color = PrimaryColor
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp
    )
)
