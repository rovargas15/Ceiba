/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 9:32 AM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.test.ceiba.ui.main.navigation.Route.USER_ID
import com.test.ceiba.ui.main.post.intent.PostEvent
import com.test.ceiba.ui.main.post.intent.PostEvent.OnBackPressed
import com.test.ceiba.ui.main.post.screen.postScreen
import com.test.ceiba.ui.main.post.viewmodel.PostViewModel
import com.test.ceiba.ui.main.user.intent.UserEvent
import com.test.ceiba.ui.main.user.screen.UserListScreen
import com.test.ceiba.ui.main.user.viewmodel.UserViewModel
import com.test.ceiba.utils.hiltViewModel

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Graph.MAIN_GRAPH) {
        mainGraph(
            navController = navController
        )
    }
}

@SuppressLint("RememberReturnType")
fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation(startDestination = Route.USER, route = Graph.MAIN_GRAPH) {
        composable(route = Route.USER) {
            val userViewModel: UserViewModel = hiltViewModel<UserViewModel>().apply {
                process(UserEvent.GetUserALl)
            }
            UserListScreen(
                livedata = userViewModel.viewState,
                onEvent = { event ->
                    userViewModel.process(event)
                },
                onSelectUser = { user ->
                    navController.navigate("${Route.POST}${user.id}")
                }
            )
        }
    }

    composable(
        route = "${Route.POST}{$USER_ID}",
        arguments = listOf(
            navArgument(USER_ID) { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getInt(USER_ID)
        requireNotNull(userId)
        val postViewModel: PostViewModel = hiltViewModel<PostViewModel>().apply {
            remember {
                process(PostEvent.GetPosts(userId))
            }
        }

        postScreen(
            livedata = postViewModel.viewState,
            onEvent = { event ->
                when (event) {
                    is OnBackPressed -> navController.popBackStack()
                    else -> postViewModel.process(event)
                }
            },
        )
    }
}

object Graph {
    const val MAIN_GRAPH = "main_graph"
}

object Route {
    const val USER = "user"
    const val USER_ID = "userId"
    const val POST = "post/"
}
