/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:50 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.uc

import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.repository.PostByUserIdRepository
import kotlinx.coroutines.flow.Flow

class GetPostByUserIdUC(
    private val postByUserIdRepository: PostByUserIdRepository
) {
    fun invoke(userId: Int): Flow<Result<List<Post>>> =
        postByUserIdRepository.getPostByUserId(userId)
}
