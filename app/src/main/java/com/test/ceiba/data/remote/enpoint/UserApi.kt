/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:39 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.remote.enpoint

import com.test.ceiba.data.remote.dto.UserResponse
import retrofit2.http.GET

interface UserApi {

    @GET(GET_USERS)
    suspend fun getUsers(): List<UserResponse>
}

private const val GET_USERS = "users"
