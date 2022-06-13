/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 5:46 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.utils

import android.app.Activity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.test.ceiba.ui.main.activity.MainActivity
import com.test.ceiba.ui.main.navigation.AppNavigation
import com.test.ceiba.ui.theme.CeibaTheme
import dagger.hilt.android.testing.HiltAndroidRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class BaseUITest(private val dispatcher: Dispatcher) {

    @get:Rule
    val hiltRule by lazy { HiltAndroidRule(this) }

    @get:Rule
    val composeTestRule by lazy { createAndroidComposeRule<MainActivity>() }

    private lateinit var mockServer: MockWebServer

    init {
        startMockServer()
    }

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun startMockServer() {
        mockServer = MockWebServer()
        mockServer.dispatcher = dispatcher
        mockServer.start(MOCK_WEB_SERVER_PORT)
    }

    protected fun enqueueResponses(vararg response: MockResponse) {
        response.forEach { mockServer.enqueue(it) }
    }

    @After
    open fun tearDown() {
        mockServer.shutdown()
    }

    fun setMainContent(
        block: Activity.() -> Unit,
        navHost: (NavHostController.() -> Unit?)? = null
    ) {
        composeTestRule.activity.apply {
            composeTestRule.setContent {
                CeibaTheme {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                    )
                    navHost?.invoke(navController)
                }
            }
        }
        block(composeTestRule.activity)
    }
}

private const val MOCK_WEB_SERVER_PORT = 8080
