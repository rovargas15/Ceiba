/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:12 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.exception.UnknownError
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.UserCacheRepository
import com.test.ceiba.domain.repository.UserRemoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetAllUserUCTest {
    private val userRemoteRepository: UserRemoteRepository = mockk()
    private val userCacheRepository: UserCacheRepository = mockk()
    private val getAllUserUC = GetAllUserUC(userRemoteRepository, userCacheRepository)

    @Test
    fun giveEmptyWhenGetUserCacheThenResultListUser() = runBlocking {
        // Give
        val user: User = mockk()
        coEvery { userCacheRepository.getUser() } answers {
            flowOf(Result.success(listOf()))
        }

        coEvery { userRemoteRepository.getUser() } answers {
            flowOf(Result.success(listOf(user)))
        }

        coEvery { userCacheRepository.insertUsers(*listOf(user).toTypedArray()) } answers {
            flowOf(Result.success(Unit))
        }

        // When
        getAllUserUC.invoke().collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), listOf(user))
        }

        // Then
        coVerify(exactly = 1) {
            userCacheRepository.getUser()
            userRemoteRepository.getUser()
            userCacheRepository.insertUsers(*listOf(user).toTypedArray())
        }
        confirmVerified(userCacheRepository, userRemoteRepository)
    }

    @Test
    fun giveUserWhenGetUserCacheThenResultListUser() = runBlocking {
        // Give
        val user: User = mockk()
        coEvery { userCacheRepository.getUser() } answers {
            flowOf(Result.success(listOf(user)))
        }

        // When
        getAllUserUC.invoke().collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), listOf(user))
        }

        // Then
        coVerify(exactly = 1) {
            userCacheRepository.getUser()
        }
        confirmVerified(userCacheRepository)
    }

    @Test
    fun giveFailureWhenGetUserRemoteThenResultFailure() = runBlocking {
        // Give
        coEvery { userCacheRepository.getUser() } answers {
            flowOf(Result.success(listOf()))
        }
        coEvery { userRemoteRepository.getUser() } answers {
            flowOf(Result.failure(UnknownError))
        }

        // When
        getAllUserUC.invoke().collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), UnknownError)
        }

        // Then
        coVerify(exactly = 1) {
            userCacheRepository.getUser()
            userRemoteRepository.getUser()
        }
        confirmVerified(userCacheRepository, userRemoteRepository)
    }
}
