/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:41 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.UserCacheRepository
import kotlinx.coroutines.flow.Flow

class GetUserByQueryUC(
    private val userCacheRepository: UserCacheRepository
) {
    fun invoke(query: String): Flow<Result<List<User>>> = userCacheRepository.getUserByQuery(query)
}
