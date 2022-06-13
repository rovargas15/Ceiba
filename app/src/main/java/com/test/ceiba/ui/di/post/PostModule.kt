/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 12:01 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.di.post

import com.test.ceiba.data.remote.enpoint.PostApi
import com.test.ceiba.data.remote.mapper.mapFromPost
import com.test.ceiba.data.repository.PostByUserIdRepositoryImpl
import com.test.ceiba.domain.repository.DomainExceptionRepository
import com.test.ceiba.domain.repository.PostByUserIdRepository
import com.test.ceiba.domain.uc.GetPostByUserIdUC
import com.test.ceiba.domain.uc.GetUserByIdUC
import com.test.ceiba.ui.main.post.viewmodel.PostViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object PostModule {

    @Provides
    fun postViewModelProvider(
        getPostByUserIdUC: GetPostByUserIdUC,
        getUserByIdUC: GetUserByIdUC,
        coroutineDispatcher: CoroutineDispatcher
    ): PostViewModel = PostViewModel(getPostByUserIdUC, getUserByIdUC, coroutineDispatcher)

    @Provides
    @ViewModelScoped
    fun getPostByUserIdUCProvider(
        postByUserIdRepository: PostByUserIdRepository
    ): GetPostByUserIdUC = GetPostByUserIdUC(postByUserIdRepository)

    @Provides
    @ViewModelScoped
    fun postByUserIdRepositoryProvider(
        postApi: PostApi,
        domainExceptionRepository: DomainExceptionRepository
    ): PostByUserIdRepository =
        PostByUserIdRepositoryImpl(postApi, mapFromPost, domainExceptionRepository)

    @Provides
    @ViewModelScoped
    fun postApiProvide(retrofit: Retrofit): PostApi =
        retrofit.create(PostApi::class.java)
}
