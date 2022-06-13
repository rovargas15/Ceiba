/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:53 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.exception

import com.squareup.moshi.JsonDataException
import java.net.ConnectException
import java.net.SocketTimeoutException

class CommonErrors {

    fun manageException(throwable: Throwable): DomainException {
        return manageJavaErrors(throwable)
    }

    private fun manageJavaErrors(throwable: Throwable): DomainException {
        return when (throwable) {
            is SocketTimeoutException -> TimeOutException
            is ConnectException -> InternalErrorException
            else -> manageParsingExceptions(throwable)
        }
    }

    private fun manageParsingExceptions(throwable: Throwable): DomainException {
        return when (throwable) {
            is JsonDataException -> ParseException
            else -> UnknownError
        }
    }
}
