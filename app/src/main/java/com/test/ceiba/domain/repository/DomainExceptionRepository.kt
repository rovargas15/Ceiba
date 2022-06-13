/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:54 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.repository

import com.test.ceiba.domain.exception.DomainException

interface DomainExceptionRepository {

    fun manageError(error: Throwable): DomainException
}
