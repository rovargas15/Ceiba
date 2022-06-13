/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 10:44 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.user.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.LiveData
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.ceiba.R
import com.test.ceiba.domain.model.User
import com.test.ceiba.ui.main.user.intent.UserEvent
import com.test.ceiba.ui.main.user.intent.UserEvent.Reload
import com.test.ceiba.ui.main.user.state.UserState
import com.test.ceiba.ui.theme.LocalDimensions
import com.test.ceiba.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(
    livedata: LiveData<UserState>,
    onSelectUser: (User) -> Unit,
    onEvent: (UserEvent) -> Unit
) {
    val (value, onValueChange) = rememberSaveable { mutableStateOf(String()) }
    TopBar(
        livedata = livedata,
        query = value,
        onQueryChange = onValueChange,
        onEvent = onEvent,
        onSelectUser = onSelectUser
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TopBar(
    livedata: LiveData<UserState>,
    query: String,
    onQueryChange: (String) -> Unit,
    onEvent: (UserEvent) -> Unit,
    onSelectUser: (User) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .fillMaxWidth()
            ) {
                val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        onQueryChange(it)
                        onEvent(UserEvent.GetUserByQuery(query = it))
                    },
                    modifier = Modifier
                        .padding(LocalDimensions.current.paddingMedium)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .testTag("searchView"),
                    placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        localSoftwareKeyboardController?.hide()
                        onEvent(UserEvent.GetUserByQuery(query = query))
                    }),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Outlined.Search,
                            null,
                            modifier = Modifier.padding(LocalDimensions.current.paddingSmall)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Outlined.Clear,
                            null,
                            modifier = Modifier
                                .padding(LocalDimensions.current.paddingSmall)
                                .clickable {
                                    if (query.isNotEmpty()) {
                                        onQueryChange(String())
                                        onEvent(UserEvent.GetUserALl)
                                    }
                                }
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        ManagerUserState(
            livedata = livedata,
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent,
            onSelectUser = onSelectUser,
        )
    }
}

@Composable
fun ManagerUserState(
    livedata: LiveData<UserState>,
    modifier: Modifier,
    onEvent: (UserEvent) -> Unit,
    onSelectUser: (User) -> Unit
) {

    when (val value = livedata.observeAsState().value) {
        is UserState.Success -> {
            if (value.users.isEmpty()) {
                ContentEmptyList(modifier)
            } else {
                CreateList(
                    modifier = modifier,
                    users = value.users,
                    onSelectUser = onSelectUser
                )
            }
        }
        is UserState.Error -> {
            ContentError(modifier = modifier, onEvent = onEvent)
        }
        is UserState.Loader -> {
            ContentLoader(modifier = modifier)
        }
        else -> Unit
    }
}

@Composable
fun CreateList(
    modifier: Modifier,
    users: List<User>,
    onSelectUser: (User) -> Unit
) {
    LazyVerticalGrid(
        columns = Adaptive(minSize = LocalDimensions.current.heightCard),
        horizontalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        modifier = modifier.padding(
            all = LocalDimensions.current.paddingMedium,
        ).testTag("listUser")
    ) {
        items(users.size) { index ->
            ContentItemUser(users[index], onSelectUser)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItemUser(
    user: User,
    onSelectUser: (User) -> Unit
) {
    val scope = rememberCoroutineScope()
    OutlinedCard(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(LocalDimensions.current.paddingLarge)),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall),
        ) {

            Text(
                text = user.name,
                style = Typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall)) {
                Icon(
                    Filled.Phone,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = user.phone,
                    style = Typography.bodyMedium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall)) {
                Icon(
                    Filled.Email,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = user.email,
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    onClick = {
                        scope.launch {
                            onSelectUser(user)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_view_post),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
fun ContentEmptyList(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize().testTag("ContentEmptyList"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.message_empty_list),
            style = Typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun ContentLoader(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize().testTag("ContentLoader"),
        contentAlignment = Alignment.Center
    ) {
        CreateAnimation(raw = RawRes(R.raw.loader))
    }
}

@Composable
fun CreateAnimation(
    raw: RawRes,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(spec = raw)
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier.size(LocalDimensions.current.imageSmall)
    )
}

@Composable
fun ContentError(
    modifier: Modifier,
    onEvent: (UserEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().testTag("ContentError"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            LocalDimensions.current.paddingSmall,
            alignment = Alignment.CenterVertically
        ),
    ) {
        CreateAnimation(raw = RawRes(R.raw.error))
        Text(
            text = stringResource(id = R.string.search_message_error),
            style = Typography.bodyMedium,
        )

        TextButton(
            onClick = {
                scope.launch {
                    onEvent(Reload)
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.btn_retry),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
