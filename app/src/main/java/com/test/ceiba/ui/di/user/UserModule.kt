/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:38 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.user

import com.test.ceiba.data.cache.db.UserDao
import com.test.ceiba.data.cache.mapper.mapFromUser
import com.test.ceiba.data.cache.mapper.mapFromUserEntity
import com.test.ceiba.data.cache.mapper.mapUserResponseToUser
import com.test.ceiba.data.remote.enpoint.UserApi
import com.test.ceiba.data.repository.UserCacheRepositoryImpl
import com.test.ceiba.data.repository.UserRemoteRepositoryImpl
import com.test.ceiba.domain.repository.DomainExceptionRepository
import com.test.ceiba.domain.repository.UserCacheRepository
import com.test.ceiba.domain.repository.UserRemoteRepository
import com.test.ceiba.domain.uc.GetAllUserUC
import com.test.ceiba.domain.uc.GetUserByQueryUC
import com.test.ceiba.ui.main.user.viewmodel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {

    @Provides
    fun userViewModelProvider(
        getAllUserUC: GetAllUserUC,
        getUserByQueryUC: GetUserByQueryUC,
        coroutineDispatcher: CoroutineDispatcher
    ) = UserViewModel(getAllUserUC, getUserByQueryUC, coroutineDispatcher)

    @Provides
    @ViewModelScoped
    fun getUserUC(
        userRemoteRepository: UserRemoteRepository,
        userCacheRepository: UserCacheRepository
    ): GetAllUserUC = GetAllUserUC(userRemoteRepository, userCacheRepository)

    @Provides
    @ViewModelScoped
    fun getUserByQueryUC(
        userCacheRepository: UserCacheRepository
    ): GetUserByQueryUC = GetUserByQueryUC(userCacheRepository)

    @Provides
    @ViewModelScoped
    fun userCacheRepositoryImplProvider(
        userDao: UserDao
    ): UserCacheRepository =
        UserCacheRepositoryImpl(userDao, mapFromUser, mapFromUserEntity)

    @Provides
    @ViewModelScoped
    fun userRemoterRepositoryImplProvider(
        userApi: UserApi,
        domainExceptionRepository: DomainExceptionRepository
    ): UserRemoteRepository =
        UserRemoteRepositoryImpl(userApi, mapUserResponseToUser, domainExceptionRepository)

    @Provides
    @ViewModelScoped
    fun userApiProvide(retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)
}
