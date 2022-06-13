/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:05 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.db

import android.content.Context
import androidx.room.Room
import com.test.ceiba.data.cache.db.AppDatabase
import com.test.ceiba.data.cache.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun userDaoProvider(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}

private const val DB_NAME = "userDB"
