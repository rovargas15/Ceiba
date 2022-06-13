/*
 * *
 *  * Created by Rafael Vargas on 6/11/22, 3:09 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.exception

import com.squareup.moshi.JsonDataException
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.ConnectException
import java.net.SocketTimeoutException

class CommonErrorsTest {

    private val commonErrors = CommonErrors()

    @Test
    fun giveThrowableWhenManageJavaErrorsThenReturnTimeOutException() {
        // Give
        val exception = SocketTimeoutException()

        // When
        val domainException = commonErrors.manageException(exception)

        // Then
        assertEquals(domainException, TimeOutException)
    }

    @Test
    fun giveThrowableWhenManageJavaErrorsThenReturnInternalErrorException() {
        // Give
        val exception = ConnectException()

        // When
        val domainException = commonErrors.manageException(exception)

        // Then
        assertEquals(domainException, InternalErrorException)
    }

    @Test
    fun giveThrowableWhenManageJavaErrorsThenReturnJsonDataException() {
        // Give
        val exception = JsonDataException()

        // When
        val domainException = commonErrors.manageException(exception)

        // Then
        assertEquals(domainException, ParseException)
    }

    @Test
    fun giveThrowableWhenManageJavaErrorsThenReturnUnknownError() {
        // Give
        val exception = IllegalArgumentException()

        // When
        val domainException = commonErrors.manageException(exception)

        // Then
        assertEquals(domainException, UnknownError)
    }
}
