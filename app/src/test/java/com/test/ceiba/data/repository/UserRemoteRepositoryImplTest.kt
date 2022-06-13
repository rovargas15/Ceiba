/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:11 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import com.test.ceiba.data.remote.dto.UserResponse
import com.test.ceiba.data.remote.enpoint.UserApi
import com.test.ceiba.domain.exception.BadRequestException
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.DomainExceptionRepository
import com.test.ceiba.ui.main.utils.Mapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserRemoteRepositoryImplTest {
    private val userApi: UserApi = mockk()
    private val mapUserResponseToUser: Mapper<UserResponse, User> = mockk()
    private val domainExceptionRepository: DomainExceptionRepository = mockk()
    private val userRemoteRepository =
        UserRemoteRepositoryImpl(userApi, mapUserResponseToUser, domainExceptionRepository)

    @Test
    fun giveAllThenGetUserThenListUser() = runBlocking {
        // Give
        val userResponse: UserResponse = mockk()
        val user: User = mockk()

        coEvery { userApi.getUsers() } answers { listOf(userResponse) }
        every { mapUserResponseToUser(userResponse) } returns user

        // When
        val response = userRemoteRepository.getUser()

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(listOf(user), result.getOrNull())
        }

        coVerify(exactly = 1) {
            userApi.getUsers()
            mapUserResponseToUser(userResponse)
        }

        confirmVerified(userApi, mapUserResponseToUser, domainExceptionRepository)
    }

    @Test
    fun giveEmptyThenGetUserThenListEmpty() = runBlocking {
        // Give
        coEvery { userApi.getUsers() } answers { emptyList() }

        // When
        val response = userRemoteRepository.getUser()

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(emptyList<User>(), result.getOrNull())
        }

        coVerify(exactly = 1) {
            userApi.getUsers()
        }

        confirmVerified(userApi, mapUserResponseToUser, domainExceptionRepository)
    }

    @Test
    fun giveExceptionThenGetUserThenFailure() = runBlocking {
        // Give
        val error = Throwable()
        coEvery { userApi.getUsers() } throws error
        every { domainExceptionRepository.manageError(error) } returns BadRequestException

        // When
        val response = userRemoteRepository.getUser()

        // Then
        response.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), BadRequestException)
        }

        coVerify(exactly = 1) {
            userApi.getUsers()
            domainExceptionRepository.manageError(error)
        }

        confirmVerified(userApi, mapUserResponseToUser, domainExceptionRepository)
    }
}
