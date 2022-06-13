/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 6:12 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.test.ceiba.R
import com.test.ceiba.data.cache.db.UserDao
import com.test.ceiba.data.cache.entity.UserEntity
import com.test.ceiba.utils.BaseUITest
import com.test.ceiba.utils.FILE_EMPTY_RESPONSE
import com.test.ceiba.utils.FILE_SUCCESS_USER_RESPONSE
import com.test.ceiba.utils.mockResponse
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class UserScreenTest : BaseUITest(dispatcher = QueueDispatcher()) {

    private val successResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_USER_RESPONSE, HttpURLConnection.HTTP_OK)

    private val errorResponse: MockResponse
        get() = mockResponse(FILE_EMPTY_RESPONSE, HttpURLConnection.HTTP_INTERNAL_ERROR)

    @Inject
    lateinit var userDao: UserDao

    private fun getUserAll() = every { userDao.getAll() } answers {
        flowOf(
            listOf(
                UserEntity(
                    id = 1,
                    name = "Leanne Graham",
                    email = "Sincere@april.biz",
                    phone = "1-770-736-8031 x56442"
                ),
                UserEntity(
                    id = 2,
                    name = "Ervin Howell",
                    email = "Shanna@melissa.tv",
                    phone = "010-692-6593 x09125"
                )
            )
        )
    }

    private fun getUserByQuery() = every { userDao.getUserByQuery("Leanne") } answers {
        flowOf(
            listOf(
                UserEntity(
                    id = 1,
                    name = "Leanne Graham",
                    email = "Sincere@april.biz",
                    phone = "1-770-736-8031 x56442"
                )
            )
        )
    }

    @Test
    fun testUserScreenRemoteData() {
        every { userDao.getAll() } returns flowOf(emptyList())
        enqueueResponses(successResponse)
        setMainContent(
            block = {
                with(composeTestRule) {
                    onNodeWithText(getString(R.string.search_hint)).assertIsDisplayed()

                    onNodeWithTag("listUser").assertIsDisplayed()

                    onNodeWithText("Leanne Graham").assertIsDisplayed()
                    onNodeWithText("Sincere@april.biz").assertIsDisplayed()
                    onNodeWithText("1-770-736-8031 x56442").assertIsDisplayed()

                    onNodeWithText("Ervin Howell").assertIsDisplayed()
                    onNodeWithText("Shanna@melissa.tv").assertIsDisplayed()
                    onNodeWithText("010-692-6593 x09125").assertIsDisplayed()
                }
            }
        )
    }

    @Test
    fun testUserScreenRoomData() {
        getUserAll()
        setMainContent(
            block = {
                with(composeTestRule) {
                    onNodeWithText(getString(R.string.search_hint)).assertIsDisplayed()

                    onNodeWithTag("listUser").assertIsDisplayed()

                    onNodeWithText("Leanne Graham").assertIsDisplayed()
                    onNodeWithText("Sincere@april.biz").assertIsDisplayed()
                    onNodeWithText("1-770-736-8031 x56442").assertIsDisplayed()

                    onNodeWithText("Ervin Howell").assertIsDisplayed()
                    onNodeWithText("Shanna@melissa.tv").assertIsDisplayed()
                    onNodeWithText("010-692-6593 x09125").assertIsDisplayed()
                }
            }
        )
    }

    @Test
    fun testUserScreenError() {
        enqueueResponses(errorResponse)
        every { userDao.getAll() } answers { flowOf(listOf()) }
        setMainContent(
            block = {
                with(composeTestRule) {
                    onNodeWithTag("ContentError").assertIsDisplayed()
                    onNodeWithText(getString(R.string.search_message_error)).assertIsDisplayed()
                    onNodeWithText(getString(R.string.btn_retry)).assertIsDisplayed()
                }
            }
        )
    }

    @Test
    fun testUserScreenGetUserByQuery() {
        getUserByQuery()
        getUserAll()
        setMainContent(
            block = {
                with(composeTestRule) {
                    onNodeWithText(getString(R.string.search_hint))
                        .performTextInput("L")

                    waitForAssertion {
                        onNodeWithTag("ContentLoader").assertIsDisplayed()
                    }

                    waitForAssertion {
                        onNodeWithTag("listUser").assertIsDisplayed()
                    }

                    onNodeWithText("Leanne Graham").assertIsDisplayed()
                    onNodeWithText("Sincere@april.biz").assertIsDisplayed()
                    onNodeWithText("1-770-736-8031 x56442").assertIsDisplayed()
                }
            }
        )
    }
}

fun ComposeTestRule.waitForAssertion(
    timeoutMillis: Long = TimeUnit.MINUTES.toMillis(1),
    assertion: () -> Unit
) {
    waitUntil(timeoutMillis) {
        try {
            assertion.invoke()
            true
        } catch (error: AssertionError) {
            false
        }
    }
}
