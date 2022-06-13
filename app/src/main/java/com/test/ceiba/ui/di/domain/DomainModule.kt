/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 11:15 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.domain

import com.test.ceiba.domain.repository.UserCacheRepository
import com.test.ceiba.domain.uc.GetUserByIdUC
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun getUserByIdUCProvider(
        userCacheRepository: UserCacheRepository
    ): GetUserByIdUC = GetUserByIdUC(userCacheRepository)
}
