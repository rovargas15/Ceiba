/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:11 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import PostResponse
import com.test.ceiba.data.remote.enpoint.PostApi
import com.test.ceiba.domain.exception.BadRequestException
import com.test.ceiba.domain.model.Post
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

class PostByUserIdRepositoryImplTest {

    private val postApi: PostApi = mockk()
    private val domainExceptionRepository: DomainExceptionRepository = mockk()
    private val mapFromPost: Mapper<PostResponse, Post> = mockk()
    private val postByUserIdRepository = PostByUserIdRepositoryImpl(
        postApi = postApi,
        mapFromPost = mapFromPost,
        domainExceptionRepository = domainExceptionRepository
    )

    @Test
    fun giveEmptyWhenGetPostsReturnListEmpty() = runBlocking {
        // Give
        coEvery { postApi.getPostByUserId(1) } answers { listOf() }

        // When
        val response = postByUserIdRepository.getPostByUserId(1)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), listOf<Post>())
        }

        coVerify(exactly = 1) { postApi.getPostByUserId(1) }
        confirmVerified(postApi)
    }

    @Test
    fun giveExceptionWhenGetPostsReturnBadRequest() = runBlocking {
        // Give
        val error = Throwable()
        coEvery { postApi.getPostByUserId(1) } throws error
        every { domainExceptionRepository.manageError(error) } returns BadRequestException

        // When
        val response = postByUserIdRepository.getPostByUserId(1)

        // Then
        response.collect { result ->
            assert(result.isFailure)
            Assert.assertEquals(result.exceptionOrNull(), BadRequestException)
        }

        coVerify(exactly = 1) {
            postApi.getPostByUserId(1)
            domainExceptionRepository.manageError(error)
        }
        confirmVerified(postApi, domainExceptionRepository)
    }

    @Test
    fun giveAllWhenGetPostsReturnListPost() = runBlocking {
        //
        val postResponse: PostResponse = mockk()
        val post: Post = mockk()
        coEvery { postApi.getPostByUserId(1) } answers { listOf(postResponse) }
        every { mapFromPost(postResponse) } returns post

        // When
        val response = postByUserIdRepository.getPostByUserId(1)

        // Then
        response.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(result.getOrNull(), listOf(post))
        }

        coVerify(exactly = 1) { postApi.getPostByUserId(1) }
        confirmVerified(postApi)
    }
}
