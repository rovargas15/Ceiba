/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 9:32 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.post.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.ceiba.domain.uc.GetPostByUserIdUC
import com.test.ceiba.domain.uc.GetUserByIdUC
import com.test.ceiba.ui.main.models.UserPostResult
import com.test.ceiba.ui.main.post.intent.PostEvent
import com.test.ceiba.ui.main.post.intent.PostEvent.GetPosts
import com.test.ceiba.ui.main.post.intent.PostEvent.Reload
import com.test.ceiba.ui.main.post.state.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostByUserIdUC: GetPostByUserIdUC,
    private val getUserByIdUC: GetUserByIdUC,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _viewState = MutableLiveData<PostState>(PostState.Loader)
    val viewState: LiveData<PostState>
        get() = _viewState
    private var userId: Int = 0

    fun process(event: PostEvent) {
        when (event) {
            is Reload -> {
                getPostByUserId(userId)
            }
            is GetPosts -> {
                userId = event.userId
                getPostByUserId(userId)
            }
            else -> Unit
        }
    }

    private fun getPostByUserId(id: Int) {
        userId = id
        val flowUser = getUserByIdUC.invoke(userId)
        val flowPost = getPostByUserIdUC.invoke(userId)

        flowUser.zip(flowPost) { resultUser, resultPots ->
            resultUser.onFailure {
                Timber.tag(this::class.simpleName).e(it)
                _viewState.postValue(PostState.Error)
            }

            resultPots.onFailure {
                Timber.tag(this::class.simpleName).e(it)
                _viewState.postValue(PostState.Error)
            }

            val user = resultUser.getOrNull()
            val posts = resultPots.getOrNull()
            if (user != null && posts != null) {
                UserPostResult(user = user, posts = posts)
            } else {
                null
            }
        }.map { result ->
            result?.let {
                _viewState.postValue(PostState.Success(it))
            }
        }.onStart {
            _viewState.postValue(PostState.Loader)
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }
}
