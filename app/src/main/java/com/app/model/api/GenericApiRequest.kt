package com.app.model.api

import retrofit2.Response

abstract class GenericApiRequest<T> {
    suspend fun apiRequest(
        call: suspend () -> Response<Any>
    ): Any? {
        val response: Response<Any> = call.invoke()
        return if (response.body() != null)
            response.body()
        else
            response.code()
    }
}