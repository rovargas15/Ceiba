/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:18 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.repository

import com.test.ceiba.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRemoteRepository {

    fun getUser(): Flow<Result<List<User>>>
}
