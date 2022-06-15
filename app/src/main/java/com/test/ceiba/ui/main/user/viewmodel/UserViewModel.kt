/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 2:52 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.ceiba.domain.model.User
import com.test.ceiba.domain.uc.GetAllUserUC
import com.test.ceiba.domain.uc.GetUserByQueryUC
import com.test.ceiba.ui.main.user.intent.UserEvent
import com.test.ceiba.ui.main.user.intent.UserEvent.GetUserALl
import com.test.ceiba.ui.main.user.intent.UserEvent.GetUserByQuery
import com.test.ceiba.ui.main.user.intent.UserEvent.Reload
import com.test.ceiba.ui.main.user.state.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getAllUserUC: GetAllUserUC,
    private val getUserByQueryUC: GetUserByQueryUC,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableLiveData<UserState>(UserState.Loader)
    val viewState: LiveData<UserState>
        get() = _viewState

    fun process(event: UserEvent) {
        when (event) {
            is GetUserALl -> {
                getUser()
            }
            is GetUserByQuery -> {
                if (event.query.isNotEmpty()) {
                    getUserByQuery(event.query)
                } else {
                    getUser()
                }
            }
            is Reload -> {
                getUser()
            }
        }
    }

    private fun getUserByQuery(query: String) {
        getUserByQueryUC.invoke(query).map { result: Result<List<User>> ->
            result.fold(
                onSuccess = { users: List<User> ->
                    _viewState.postValue(UserState.Success(users))
                },
                onFailure = {
                    // Timber.tag(this::class.simpleName).e(it)
                    _viewState.postValue(UserState.Error)
                }
            )
        }.onStart {
            _viewState.postValue(UserState.Loader)
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }

    private fun getUser() {
        getAllUserUC.invoke().map { result: Result<List<User>> ->
            result.fold(
                onSuccess = { users: List<User> ->
                    _viewState.postValue(UserState.Success(users))
                },
                onFailure = {
                    // Timber.tag(this::class.simpleName).e(it)
                    _viewState.postValue(UserState.Error)
                }
            )
        }.onStart {
            _viewState.postValue(UserState.Loader)
        }.flowOn(coroutineDispatcher).launchIn(viewModelScope)
    }
}
