/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:33 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.remote.mapper

import PostResponse
import com.test.ceiba.domain.model.Post
import com.test.ceiba.ui.main.utils.Mapper

val mapFromPost: Mapper<PostResponse, Post> = { input ->
    Post(input.id, input.title, input.body)
}
