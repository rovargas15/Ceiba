/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:03 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)
