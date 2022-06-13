/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 5:45 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class InstrumentationRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader,
        className: String,
        context: Context
    ): Application {
        return super.newApplication(
            cl,
            HiltTestApplication::class.java.name,
            context
        )
    }
}
