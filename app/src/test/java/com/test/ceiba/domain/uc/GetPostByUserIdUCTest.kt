/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:13 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.exception.UnknownError
import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.repository.PostByUserIdRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetPostByUserIdUCTest {

    private val postByUserIdRepository: PostByUserIdRepository = mockk()
    private val getPostByUserIdUC = GetPostByUserIdUC(postByUserIdRepository)

    @Test
    fun giveUsersWhenInvokeThenFlowResultSuccess() = runBlocking {
        // Give
        val post: Post = mockk()
        every { postByUserIdRepository.getPostByUserId(1) } answers {
            flowOf(
                Result.success(
                    listOf(
                        post
                    )
                )
            )
        }

        // When
        getPostByUserIdUC.invoke(1).collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), listOf(post))
        }

        // Then
        verify { postByUserIdRepository.getPostByUserId(1) }
        confirmVerified(postByUserIdRepository)
    }

    @Test
    fun giveFailureWhenInvokeThenFlowResultFailure() = runBlocking {
        // Give
        every { postByUserIdRepository.getPostByUserId(1) } answers {
            flowOf(
                Result.failure(UnknownError)
            )
        }

        // When
        getPostByUserIdUC.invoke(1).collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), UnknownError)
        }

        // Then
        verify { postByUserIdRepository.getPostByUserId(1) }
        confirmVerified(postByUserIdRepository)
    }
}
