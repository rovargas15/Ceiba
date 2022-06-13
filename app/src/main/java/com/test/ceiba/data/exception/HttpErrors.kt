/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:52 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.exception

import com.test.ceiba.domain.exception.BadRequestException
import com.test.ceiba.domain.exception.DomainException
import com.test.ceiba.domain.exception.HttpErrorCode
import com.test.ceiba.domain.exception.InternalErrorException
import com.test.ceiba.domain.exception.NotFoundException
import com.test.ceiba.domain.exception.Unauthorized
import com.test.ceiba.domain.exception.UnknownError
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

class HttpErrors {

    private val typeError = mapOf(
        HttpsURLConnection.HTTP_BAD_REQUEST to BadRequestException,
        HttpsURLConnection.HTTP_NOT_FOUND to NotFoundException,
        HttpsURLConnection.HTTP_INTERNAL_ERROR to InternalErrorException,
        HttpsURLConnection.HTTP_UNAUTHORIZED to Unauthorized
    )

    fun getHttpError(error: HttpException): DomainException {
        return if (typeError.containsKey(error.code())) {
            typeError.getValue(error.code())
        } else {
            HttpErrorCode(error.code(), getMessage(error).message)
        }
    }

    private fun getMessage(exception: HttpException): DomainException {
        return try {
            var jsonString = exception.response()?.errorBody()?.string()
            if (jsonString.isNullOrEmpty()) jsonString = "{}"
            DomainException(jsonString)
        } catch (exception_: Exception) {
            UnknownError
        }
    }
}
