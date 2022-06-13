/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:11 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import com.test.ceiba.data.cache.db.UserDao
import com.test.ceiba.data.cache.entity.UserEntity
import com.test.ceiba.domain.model.User
import com.test.ceiba.ui.main.utils.Mapper
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UserCacheRepositoryImplTest {

    private val userDao: UserDao = mockk(relaxed = true)
    private val mapFromUser: Mapper<UserEntity, User> = mockk(relaxed = true)
    private val mapFromUserEntity: Mapper<User, UserEntity> = mockk(relaxed = true)
    private val userCacheRepository = UserCacheRepositoryImpl(
        userDao = userDao,
        mapFromUser = mapFromUser,
        mapFromUserEntity = mapFromUserEntity
    )

    @Test
    fun giveAllWhenGetUserThenListUser() = runBlocking {
        // Give
        val userEntity: UserEntity = mockk()
        val user: User = mockk()
        every { userDao.getAll() } answers { flowOf(listOf(userEntity)) }
        every { mapFromUser(userEntity) } returns user

        // When
        val resultFlow = userCacheRepository.getUser()

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(listOf(user), result.getOrNull())
        }

        verify(exactly = 1) {
            userDao.getAll()
            mapFromUser(userEntity)
        }
        confirmVerified(userEntity, mapFromUser, userDao)
    }

    @Test
    fun giveEmptyWhenGetUserThenListEmpty() = runBlocking {
        // Give
        every { userDao.getAll() } answers { flowOf(emptyList()) }

        // When
        val resultFlow = userCacheRepository.getUser()

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(emptyList<User>(), result.getOrNull())
        }

        verify(exactly = 1) { userDao.getAll() }
        confirmVerified(userDao)
    }

    @Test
    fun giveAllWhenGetUserByQueryThenListUser() = runBlocking {
        // Give
        val userEntity: UserEntity = mockk()
        val user: User = mockk()
        val query = "query"
        every { userDao.getUserByQuery("%$query%") } answers { flowOf(listOf(userEntity)) }
        every { mapFromUser(userEntity) } returns user

        // When
        val resultFlow = userCacheRepository.getUserByQuery(query)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(listOf(user), result.getOrNull())
        }

        verify(exactly = 1) {
            userDao.getUserByQuery("%$query%")
            mapFromUser(userEntity)
        }
        confirmVerified(userEntity, mapFromUser, userDao)
    }

    @Test
    fun giveEmptyWhenGetUserByQueryThenListEmpty() = runBlocking {
        // Give
        val query = "query"
        every { userDao.getUserByQuery("%$query%") } answers { flowOf(emptyList()) }

        // When
        val resultFlow = userCacheRepository.getUserByQuery(query)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(emptyList<User>(), result.getOrNull())
        }

        verify(exactly = 1) { userDao.getUserByQuery("%$query%") }
        confirmVerified(userDao)
    }

    @Test
    fun giveUserWhenInsertUsersThenSuccess() = runBlocking {
        // Give
        val userEntity: UserEntity = mockk()
        val user: User = mockk()
        every { userDao.insertUser(userEntity) }
        every { mapFromUserEntity(user) } returns userEntity

        // When
        val resultFlow = userCacheRepository.insertUsers(user)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(emptyList<User>(), result.getOrNull())
        }

        verify(exactly = 1) {
            userDao.insertUser(userEntity)
            mapFromUserEntity(user)
        }
        confirmVerified(userDao, mapFromUserEntity)
    }

    @Test
    fun giveUserWhenInsertUsersThenFail() = runBlocking {
        // Give
        val userEntity: UserEntity = mockk()
        val user: User = mockk()
        every { userDao.insertUser(userEntity) } throws Throwable()

        // When
        val resultFlow = userCacheRepository.insertUsers(user)

        // Then
        resultFlow.collect { result ->
            assert(result.isFailure)
        }

        confirmVerified(userDao)
    }

    @Test
    fun giveUserIdWhenGetUserByIdThenSuccess() = runBlocking {
        // Give
        val userEntity: UserEntity = mockk()
        val user: User = mockk()
        val userId = 1
        every { userDao.getUserById(userId) } answers { flowOf(userEntity) }
        every { mapFromUser(userEntity) } returns user

        // When
        val resultFlow = userCacheRepository.getUserById(userId)

        // Then
        resultFlow.collect { result ->
            assert(result.isSuccess)
            Assert.assertEquals(user, result.getOrNull())
        }

        verify(exactly = 1) {
            userDao.getUserById(userId)
            mapFromUser(userEntity)
        }
        confirmVerified(userDao, mapFromUser)
    }
}
