/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 4:00 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.cache.mapper

import com.test.ceiba.data.cache.entity.UserEntity
import com.test.ceiba.data.remote.dto.UserResponse
import com.test.ceiba.domain.model.User
import com.test.ceiba.ui.main.utils.Mapper

val mapFromUser: Mapper<UserEntity, User> = { input ->
    with(input) {
        User(
            id = id,
            name = name,
            email = email,
            phone = phone
        )
    }
}

val mapFromUserEntity: Mapper<User, UserEntity> = { input ->
    with(input) {
        UserEntity(
            id = id,
            name = name,
            email = email,
            phone = phone
        )
    }
}

val mapUserResponseToUser: Mapper<UserResponse, User> = { input ->
    with(input) {
        User(
            id = id,
            name = name,
            email = email,
            phone = phone
        )
    }
}
