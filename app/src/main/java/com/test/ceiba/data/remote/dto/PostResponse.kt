/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 11:54 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "body")
    val body: String
)
