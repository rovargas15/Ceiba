/*
 * *
 *  * Created by Rafael Vargas on 6/10/22, 2:39 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.ui.main.user.intent

sealed class UserEvent {
    object GetUserALl : UserEvent()
    class GetUserByQuery(val query: String) : UserEvent()
    object Reload : UserEvent()
}
