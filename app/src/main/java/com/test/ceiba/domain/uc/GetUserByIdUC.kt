/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 10:39 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.repository.UserCacheRepository

class GetUserByIdUC(private val userCacheRepository: UserCacheRepository) {

    fun invoke(userId: Int) = userCacheRepository.getUserById(userId)
}
