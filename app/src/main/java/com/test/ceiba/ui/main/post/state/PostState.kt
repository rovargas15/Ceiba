/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 2:40 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.post.state

import com.test.ceiba.ui.main.models.UserPostResult

sealed interface PostState {
    object Loader : PostState
    object Error : PostState
    data class Success(val userPostResult: UserPostResult) : PostState
}
