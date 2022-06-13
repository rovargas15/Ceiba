/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 2:39 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.post.intent

sealed class PostEvent {
    data class GetPosts(val userId: Int) : PostEvent()
    object Reload : PostEvent()
    object OnBackPressed : PostEvent()
}
