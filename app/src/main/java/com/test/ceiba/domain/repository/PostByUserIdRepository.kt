/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:50 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.repository

import com.test.ceiba.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostByUserIdRepository {

    fun getPostByUserId(userId: Int): Flow<Result<List<Post>>>
}
