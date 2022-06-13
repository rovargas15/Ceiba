/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 10:44 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.post.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells.Adaptive
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.LiveData
import com.airbnb.lottie.compose.LottieCompositionSpec.RawRes
import com.test.ceiba.R
import com.test.ceiba.domain.model.Post
import com.test.ceiba.domain.model.User
import com.test.ceiba.ui.main.post.intent.PostEvent
import com.test.ceiba.ui.main.post.intent.PostEvent.Reload
import com.test.ceiba.ui.main.post.state.PostState
import com.test.ceiba.ui.main.user.screen.ContentLoader
import com.test.ceiba.ui.main.user.screen.CreateAnimation
import com.test.ceiba.ui.theme.LocalDimensions
import com.test.ceiba.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun postScreen(
    livedata: LiveData<PostState>,
    onEvent: (PostEvent) -> Unit
) {
    TopBar(
        livedata = livedata,
        onEvent = onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    livedata: LiveData<PostState>,
    onEvent: (PostEvent) -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                modifier = Modifier.background(
                    color = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(PostEvent.OnBackPressed) }) {
                        Icon(imageVector = Filled.ArrowBack, contentDescription = "")
                    }
                },
            )
        },
    ) { padding: PaddingValues ->
        ManagerPostState(
            livedata = livedata,
            modifier = Modifier.padding(padding),
            onEvent = onEvent
        )
    }
}

@Composable
fun ManagerPostState(
    livedata: LiveData<PostState>,
    modifier: Modifier,
    onEvent: (PostEvent) -> Unit
) {
    when (val value = livedata.observeAsState().value) {
        is PostState.Success -> {
            HeaderPost(
                user = value.userPostResult.user,
                modifier = modifier
            ) {
                ContentListPost(posts = value.userPostResult.posts)
            }
        }
        is PostState.Error -> {
            ContentError(modifier = modifier, onEvent = onEvent)
        }
        is PostState.Loader -> {
            ContentLoader(modifier = modifier)
        }
        else -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderPost(
    user: User,
    modifier: Modifier,
    contentList: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                all = LocalDimensions.current.paddingMedium,
            )
    ) {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth().testTag("ContentUserPost")
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(LocalDimensions.current.paddingLarge)),
                verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall)
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
            }
        }

        contentList()
    }
}

@Composable
fun ContentListPost(posts: List<Post>) {
    LazyVerticalGrid(
        columns = Adaptive(minSize = LocalDimensions.current.MinSizeCard),
        horizontalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        verticalArrangement = Arrangement.spacedBy(space = LocalDimensions.current.paddingMedium),
        modifier = Modifier.padding(top = LocalDimensions.current.paddingMedium).testTag("ContentListPost")
    ) {
        items(posts.size) { index ->
            ContentItemPost(post = posts[index])
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentItemPost(post: Post) {
    OutlinedCard(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(PaddingValues(LocalDimensions.current.paddingLarge)),
            verticalArrangement = Arrangement.spacedBy(LocalDimensions.current.paddingSmall),
        ) {

            Text(
                text = post.title,
                style = Typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = post.body,
                style = Typography.bodyMedium
            )
        }
    }
}

@Composable
fun ContentError(
    modifier: Modifier,
    onEvent: (PostEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().testTag("ContentErrorPost"),
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
