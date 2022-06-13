/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:13 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.exception.UnknownError
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.UserCacheRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetUserByIdUCTest {

    private val userCacheRepository: UserCacheRepository = mockk()
    private val getUserByIdUC = GetUserByIdUC(userCacheRepository)

    @Test
    fun giveFlowSuccessWhenGetUserByIdThenSuccess() = runBlocking {
        // Give
        val userId = 1
        val user: User = mockk()
        every { userCacheRepository.getUserById(userId) } answers {
            flowOf(Result.success(user))
        }

        // When
        val resultFlow = getUserByIdUC.invoke(userId)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(user, result.getOrNull())
        }

        verify(exactly = 1) {
            userCacheRepository.getUserById(userId)
        }
        confirmVerified(userCacheRepository)
    }

    @Test
    fun giveFlowFailureWhenGetUserByIdThenFailure() = runBlocking {
        // Give
        val userId = 1
        every { userCacheRepository.getUserById(userId) } answers {
            flowOf(Result.failure(UnknownError))
        }

        // When
        val resultFlow = getUserByIdUC.invoke(userId)

        // Then
        resultFlow.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(UnknownError, result.exceptionOrNull())
        }

        verify(exactly = 1) {
            userCacheRepository.getUserById(userId)
        }
        confirmVerified(userCacheRepository)
    }
}
