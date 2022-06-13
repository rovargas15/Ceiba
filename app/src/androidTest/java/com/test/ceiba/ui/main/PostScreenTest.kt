/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 6:12 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.test.ceiba.R
import com.test.ceiba.data.cache.db.UserDao
import com.test.ceiba.data.cache.entity.UserEntity
import com.test.ceiba.ui.main.navigation.Route
import com.test.ceiba.utils.BaseUITest
import com.test.ceiba.utils.FILE_EMPTY_RESPONSE
import com.test.ceiba.utils.FILE_SUCCESS_POST_RESPONSE
import com.test.ceiba.utils.mockResponse
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import kotlinx.coroutines.flow.flowOf
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.QueueDispatcher
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import javax.inject.Inject

@RunWith(AndroidJUnit4ClassRunner::class)
@HiltAndroidTest
class PostScreenTest : BaseUITest(dispatcher = QueueDispatcher()) {

    private val successResponse: MockResponse
        get() = mockResponse(FILE_SUCCESS_POST_RESPONSE, HttpURLConnection.HTTP_OK)

    private val errorResponse: MockResponse
        get() = mockResponse(FILE_EMPTY_RESPONSE, HttpURLConnection.HTTP_BAD_GATEWAY)

    @Inject
    lateinit var userDao: UserDao

    private fun getUserById() = every { userDao.getUserById(1) } answers {
        flowOf(
            UserEntity(
                id = 1,
                name = "Leanne Graham",
                email = "Sincere@april.biz",
                phone = "1-770-736-8031 x56442"
            )
        )
    }

    @Test
    fun testUserScreenData() {
        getUserById()
        enqueueResponses(successResponse)
        setMainContent(navHost = {
            navigate("${Route.POST}1")
        }, block = {
                with(composeTestRule) {
                    // Info User Data
                    onNodeWithTag("ContentUserPost").assertIsDisplayed()
                    onNodeWithText("Leanne Graham").assertIsDisplayed()
                    onNodeWithText("Sincere@april.biz").assertIsDisplayed()
                    onNodeWithText("1-770-736-8031 x56442").assertIsDisplayed()

                    // Info Data Post
                    onNodeWithTag("ContentListPost").assertIsDisplayed()
                    onNodeWithText("sunt aut facere repellat provident occaecati excepturi optio reprehenderit").assertIsDisplayed()
                    onNodeWithText("quia et suscipit\nsuscipit recusandae consequuntur expedita et cum\nreprehenderit molestiae ut ut quas totam\nnostrum rerum est autem sunt rem eveniet architecto").assertIsDisplayed()
                }
            })
    }

    @Test
    fun testUserScreenErrorClickRetry() {
        getUserById()
        enqueueResponses(errorResponse)
        setMainContent(navHost = {
            navigate("${Route.POST}1")
        }, block = {
                with(composeTestRule) {
                    onNodeWithTag("ContentErrorPost")
                        .assertIsDisplayed()
                    onNodeWithText(getString(R.string.search_message_error)).assertIsDisplayed()
                    onNodeWithText(getString(R.string.btn_retry)).performClick().assertIsDisplayed()
                }
                getUserById()
                enqueueResponses(successResponse)
                composeTestRule.onNodeWithTag("ContentUserPost")
                    .assertIsDisplayed()
            })
    }
}
