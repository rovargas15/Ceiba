/*
 * *
 *  * Created by Rafael Vargas on 6/12/22, 11:03 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.db

import com.test.ceiba.data.cache.db.AppDatabase
import com.test.ceiba.data.cache.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object DatabaseModuleTest {

    private val appDatabase = mockk<AppDatabase>(relaxed = true)
    private val userDao = mockk<UserDao>(relaxed = true)

    @Provides
    @Singleton
    fun appDatabaseProvider() = appDatabase

    @Provides
    @Singleton
    fun userDaoProvider() = userDao
}
