/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:19 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.user.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.test.ceiba.domain.exception.UnknownError
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.uc.GetAllUserUC
import com.test.ceiba.domain.uc.GetUserByQueryUC
import com.test.ceiba.ui.main.user.intent.UserEvent
import com.test.ceiba.ui.main.user.state.UserState
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

class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = DispatcherRule()

    private val getAllUserUC: GetAllUserUC = mockk(relaxed = true)
    private val getUserByQueryUC: GetUserByQueryUC = mockk(relaxed = true)
    private val userViewModel =
        UserViewModel(
            getAllUserUC = getAllUserUC,
            getUserByQueryUC = getUserByQueryUC,
            coroutineDispatcher = mainDispatcherRule.testDispatcher
        )

    @Test
    fun giveAllWhenGetUserALlThenSuccess() {
        // Give
        val user: User = mockk()
        every { getAllUserUC.invoke() } returns flowOf(Result.success(listOf(user)))

        // When
        userViewModel.process(UserEvent.GetUserALl)

        // Then
        val result = userViewModel.viewState.getOrAwaitValue()
        assert(result is UserState.Success)
        Assert.assertEquals((result as UserState.Success).users, listOf(user))

        verify(exactly = 1) { getAllUserUC.invoke() }
        confirmVerified(getAllUserUC)
    }

    @Test
    fun giveAllWhenReloadThenSuccess() {
        // Give
        val user: User = mockk()
        every { getAllUserUC.invoke() } answers { flowOf(Result.success(listOf(user))) }

        // When
        userViewModel.process(UserEvent.Reload)

        // Then
        val result = userViewModel.viewState.getOrAwaitValue()
        assert(result is UserState.Success)
        Assert.assertEquals((result as UserState.Success).users, listOf(user))

        verify(exactly = 1) { getAllUserUC.invoke() }
        confirmVerified(getAllUserUC)
    }

    @Test
    fun giveAllWhenGetUserByQueryThenSuccess() {
        // Give
        val user: User = mockk()
        every { getUserByQueryUC.invoke("query") } answers { flowOf(Result.success(listOf(user))) }

        // When
        userViewModel.process(UserEvent.GetUserByQuery("query"))

        // Then
        val result = userViewModel.viewState.getOrAwaitValue()
        assert(result is UserState.Success)
        Assert.assertEquals((result as UserState.Success).users, listOf(user))

        verify(exactly = 1) { getUserByQueryUC.invoke("query") }
        confirmVerified(getUserByQueryUC)
    }

    @Test
    fun giveFailureWhenGetUserByQueryThenError() {
        // Give
        every { getUserByQueryUC.invoke("query") } answers { flowOf(Result.failure(UnknownError)) }

        // When
        userViewModel.process(UserEvent.GetUserByQuery("query"))

        // Then
        val result = userViewModel.viewState.getOrAwaitValue()
        assert(result is UserState.Error)

        verify(exactly = 1) { getUserByQueryUC.invoke("query") }
        confirmVerified(getUserByQueryUC)
    }
}
