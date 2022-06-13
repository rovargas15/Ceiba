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

class GetUserByQueryUCTest {
    private val userCacheRepository: UserCacheRepository = mockk()
    private val getUserByQueryUC = GetUserByQueryUC(userCacheRepository)

    @Test
    fun giveEmptyWhenGetUserThenListEmpty() = runBlocking {
        // Give
        every { userCacheRepository.getUserByQuery("") } answers {
            flowOf(Result.success(emptyList()))
        }

        // When
        val resultFlow = getUserByQueryUC.invoke("")

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(emptyList<User>(), result.getOrNull())
        }

        verify(exactly = 1) { userCacheRepository.getUserByQuery("") }
        confirmVerified(userCacheRepository)
    }

    @Test
    fun giveAllWhenGetUserByQueryThenListUser() = runBlocking {
        // Give
        val user: User = mockk()
        val query = "query"
        every { userCacheRepository.getUserByQuery(query) } answers {
            flowOf(Result.success(listOf(user)))
        }

        // When
        val resultFlow = getUserByQueryUC.invoke(query)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(listOf(user), result.getOrNull())
        }

        verify(exactly = 1) {
            userCacheRepository.getUserByQuery(query)
        }
        confirmVerified(userCacheRepository)
    }

    @Test
    fun giveEmptyWhenGetUserByQueryThenListEmpty() = runBlocking {
        // Give
        val query = "query"
        every { userCacheRepository.getUserByQuery(query) } answers {
            flowOf(Result.failure(UnknownError))
        }

        // When
        val resultFlow = getUserByQueryUC.invoke(query)

        // Then
        resultFlow.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(UnknownError, result.exceptionOrNull())
        }

        verify(exactly = 1) { userCacheRepository.getUserByQuery(query) }
        confirmVerified(userCacheRepository)
    }
}
