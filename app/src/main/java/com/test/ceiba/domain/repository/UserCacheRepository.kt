/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:18 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.repository

import com.test.ceiba.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserCacheRepository {

    fun getUser(): Flow<Result<List<User>>>

    fun getUserByQuery(query: String): Flow<Result<List<User>>>

    fun insertUsers(vararg user: User): Flow<Result<Unit>>

    fun getUserById(userId: Int): Flow<Result<User>>
}
