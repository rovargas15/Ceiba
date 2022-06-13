/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:17 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import com.test.ceiba.data.remote.dto.UserResponse
import com.test.ceiba.data.remote.enpoint.UserApi
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.DomainExceptionRepository
import com.test.ceiba.domain.repository.UserRemoteRepository
import com.test.ceiba.ui.main.utils.Mapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserRemoteRepositoryImpl(
    private val userApi: UserApi,
    private val mapUserResponseToUser: Mapper<UserResponse, User>,
    private val domainExceptionRepository: DomainExceptionRepository
) : UserRemoteRepository {

    override fun getUser() = flow {
        val response = userApi.getUsers()
        emit(
            Result.success(
                response.map(mapUserResponseToUser)
            )
        )
    }.catch { error ->
        emit(Result.failure(domainExceptionRepository.manageError(error)))
    }
}
