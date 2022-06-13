/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:56 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import PostResponse
import com.test.ceiba.data.remote.enpoint.PostApi
import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.repository.DomainExceptionRepository
import com.test.ceiba.domain.repository.PostByUserIdRepository
import com.test.ceiba.ui.main.utils.Mapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class PostByUserIdRepositoryImpl(
    private val postApi: PostApi,
    private val mapFromPost: Mapper<PostResponse, Post>,
    private val domainExceptionRepository: DomainExceptionRepository
) : PostByUserIdRepository {

    override fun getPostByUserId(userId: Int) = flow {
        val response = postApi.getPostByUserId(userId)
        emit(Result.success(response.map(mapFromPost)))
    }.catch { error ->
        emit(Result.failure(domainExceptionRepository.manageError(error)))
    }
}
