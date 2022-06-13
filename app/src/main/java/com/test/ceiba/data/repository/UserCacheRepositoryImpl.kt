/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:21 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import com.test.ceiba.data.cache.db.UserDao
import com.test.ceiba.data.cache.entity.UserEntity
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.repository.UserCacheRepository
import com.test.ceiba.ui.main.utils.Mapper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserCacheRepositoryImpl(
    private val userDao: UserDao,
    private val mapFromUser: Mapper<UserEntity, User>,
    private val mapFromUserEntity: Mapper<User, UserEntity>
) : UserCacheRepository {

    override fun getUser() = userDao.getAll().map { result ->
        Result.success(result.map(mapFromUser))
    }.catch { error ->
        Result.failure<User>(error)
    }

    override fun getUserByQuery(query: String) = userDao.getUserByQuery("%$query%").map { result ->
        Result.success(result.map(mapFromUser))
    }.catch { error ->
        Result.failure<User>(error)
    }

    override fun insertUsers(vararg user: User) = flow {
        val result = userDao.insertUser(
            users = user.map(mapFromUserEntity).toTypedArray()
        )
        emit(Result.success(result))
    }.catch { error ->
        Result.failure<User>(error)
    }

    override fun getUserById(userId: Int) = userDao.getUserById(userId).map {
        Result.success(mapFromUser(it))
    }.catch { error ->
        Result.failure<User>(error)
    }
}
