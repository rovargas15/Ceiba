/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:45 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.remote.enpoint

import PostResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PostApi {

    @GET(GET_POST)
    suspend fun getPostByUserId(
        @Query("userId") userId: Int
    ): List<PostResponse>
}

private const val GET_POST = "posts"
