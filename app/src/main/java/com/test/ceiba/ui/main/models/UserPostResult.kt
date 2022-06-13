/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 10:37 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.models

import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.model.User

data class UserPostResult(
    val user: User,
    val posts: List<Post>
)
