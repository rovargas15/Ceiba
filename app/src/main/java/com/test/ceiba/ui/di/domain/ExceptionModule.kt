/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:54 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.domain

import com.test.ceiba.data.exception.HttpErrors
import com.test.ceiba.data.repository.DomainExceptionRepositoryImpl
import com.test.ceiba.domain.exception.CommonErrors
import com.test.ceiba.domain.repository.DomainExceptionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ExceptionModule {

    @Provides
    @ViewModelScoped
    fun commonErrorsProvider() = CommonErrors()

    @Provides
    @ViewModelScoped
    fun httpErrorsProvider() = HttpErrors()

    @Provides
    @ViewModelScoped
    fun domainExceptionRepositoryProvider(
        commonErrors: CommonErrors,
        httpErrors: HttpErrors
    ): DomainExceptionRepository =
        DomainExceptionRepositoryImpl(commonErrors, httpErrors)
}
