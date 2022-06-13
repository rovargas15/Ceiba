/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:03 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.ceiba.data.cache.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
