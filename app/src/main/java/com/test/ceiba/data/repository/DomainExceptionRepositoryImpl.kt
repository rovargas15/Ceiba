/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:53 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.data.repository

import com.test.ceiba.data.exception.HttpErrors
import com.test.ceiba.domain.exception.CommonErrors
import com.test.ceiba.domain.exception.DomainException
import com.test.ceiba.domain.repository.DomainExceptionRepository
import retrofit2.HttpException

class DomainExceptionRepositoryImpl(
    private val commonErrors: CommonErrors,
    private val httpErrors: HttpErrors
) : DomainExceptionRepository {

    override fun manageError(error: Throwable): DomainException {
        return if (error is HttpException) {
            httpErrors.getHttpError(error)
        } else {
            commonErrors.manageException(error)
        }
    }
}
