/*
 * *
 *  * Created by Rafael Vargas on 6/9/22, 10:53 PM
 *  * Copyright (c) 2022 . All rights reserved.
 *
 */

package com.test.ceiba.domain.exception

open class DomainException(override val message: String = "") : Throwable(message)
object NotFoundException : DomainException()
object BadRequestException : DomainException()
object InternalErrorException : DomainException()
object UnknownError : DomainException()
object Unauthorized : DomainException()
object TimeOutException : DomainException()
object ParseException : DomainException()
data class HttpErrorCode(val code: Int, override val message: String) : DomainException()
