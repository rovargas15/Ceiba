/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:20 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.post.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.ceiba.domain.exception.UnknownError
import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.uc.GetPostByUserIdUC
import com.test.ceiba.domain.uc.GetUserByIdUC
import com.test.ceiba.ui.main.models.UserPostResult
import com.test.ceiba.ui.main.post.intent.PostEvent
import com.test.ceiba.ui.main.post.state.PostState
import com.test.ceiba.ui.utils.DispatcherRule
import com.test.ceiba.ui.utils.getOrAwaitValue
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class PostViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = DispatcherRule()

    private val getPostByUserIdUC: GetPostByUserIdUC = mockk()
    private val getUserByIdUC: GetUserByIdUC = mockk()

    private val postViewModel = PostViewModel(
        getPostByUserIdUC = getPostByUserIdUC,
        getUserByIdUC = getUserByIdUC,
        coroutineDispatcher = mainDispatcherRule.testDispatcher
    )

    @Test
    fun givePotsAllWhenEventGetPostsThenReturnUserPostResult() {
        // Give
        val user: User = mockk()
        val post: Post = mockk()
        every { getUserByIdUC.invoke(1) } answers { flowOf(Result.success(user)) }
        every { getPostByUserIdUC.invoke(1) } answers { flowOf(Result.success(listOf(post))) }

        // When
        postViewModel.process(event = PostEvent.GetPosts(1))

        // Then
        val result = postViewModel.viewState.getOrAwaitValue()
        assert(result is PostState.Success)
        Assert.assertEquals(
            (result as PostState.Success).userPostResult,
            UserPostResult(user, listOf(post))
        )

        verify(exactly = 1) {
            getUserByIdUC.invoke(1)
            getPostByUserIdUC.invoke(1)
        }
        confirmVerified(getPostByUserIdUC, getUserByIdUC)
    }

    @Test
    fun givePotsAllWhenEventReloadThenReturnUserPostResult() {
        // Give
        val user: User = mockk()
        val post: Post = mockk()
        every { getUserByIdUC.invoke(1) } answers { flowOf(Result.success(user)) }
        every { getPostByUserIdUC.invoke(1) } answers { flowOf(Result.success(listOf(post))) }

        // When
        postViewModel.process(event = PostEvent.GetPosts(1))

        // Then
        val result = postViewModel.viewState.getOrAwaitValue()
        assert(result is PostState.Success)
        Assert.assertEquals(
            (result as PostState.Success).userPostResult,
            UserPostResult(user, listOf(post))
        )

        verify(exactly = 1) {
            getUserByIdUC.invoke(1)
            getPostByUserIdUC.invoke(1)
        }
        confirmVerified(getPostByUserIdUC, getUserByIdUC)
    }

    @Test
    fun giveFailureWhenGetUserByIdUCThenReturnError() {
        // Give
        val post: Post = mockk()
        every { getUserByIdUC.invoke(1) } answers { flowOf(Result.failure(UnknownError)) }
        every { getPostByUserIdUC.invoke(1) } answers { flowOf(Result.success(listOf(post))) }

        // When
        postViewModel.process(event = PostEvent.GetPosts(1))

        // Then
        val result = postViewModel.viewState.getOrAwaitValue()
        assert(result is PostState.Error)

        verify(exactly = 1) {
            getUserByIdUC.invoke(1)
            getPostByUserIdUC.invoke(1)
        }
        confirmVerified(getPostByUserIdUC, getUserByIdUC)
    }

    @Test
    fun giveFailureWhenGetPostByUserIdUCUCThenReturnError() {
        // Give
        val user: User = mockk()
        every { getUserByIdUC.invoke(1) } answers { flowOf(Result.success(user)) }
        every { getPostByUserIdUC.invoke(1) } answers { flowOf(Result.failure(UnknownError)) }

        // When
        postViewModel.process(event = PostEvent.GetPosts(1))

        // Then
        val result = postViewModel.viewState.getOrAwaitValue()
        assert(result is PostState.Error)

        verify(exactly = 1) {
            getUserByIdUC.invoke(1)
            getPostByUserIdUC.invoke(1)
        }
        confirmVerified(getPostByUserIdUC, getUserByIdUC)
    }
}
