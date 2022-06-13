/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:41 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.UserCacheRepository
import com.test.ceiba.domain.repository.UserRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllUserUC(
    private val userRemoteRepository: UserRemoteRepository,
    private val userCacheRepository: UserCacheRepository
) {
    fun invoke(): Flow<Result<List<User>>> = flow {
        userCacheRepository.getUser().collect { resultCache ->
            resultCache.onSuccess { usersCache ->
                if (usersCache.isEmpty()) {
                    userRemoteRepository.getUser().collect { resultRemote ->
                        resultRemote.onSuccess { usersRemote ->
                            userCacheRepository.insertUsers(*usersRemote.toTypedArray()).collect {
                                it.onSuccess {
                                    emit(Result.success(usersRemote))
                                }.onFailure { error ->
                                    emit(Result.failure(error))
                                }
                            }
                        }.onFailure {
                            emit(Result.failure(it))
                        }
                    }
                } else {
                    emit(Result.success(usersCache))
                }
            }.onFailure {
                emit(Result.failure(it))
            }
        }
    }
}
