/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:59 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.cache.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.ceiba.data.cache.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * from UserEntity")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT * from UserEntity where  name like :query")
    fun getUserByQuery(query: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg users: UserEntity)

    @Query("SELECT * from UserEntity where  id == :userId")
    fun getUserById(userId: Int): Flow<UserEntity>
}
