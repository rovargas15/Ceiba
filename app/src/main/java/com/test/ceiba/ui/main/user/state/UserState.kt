/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 2:40 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.user.state

import com.test.ceiba.domain.model.User

sealed class UserState {
    object Loader : UserState()
    object Error : UserState()
    data class Success(val users: List<User>) : UserState()
}
