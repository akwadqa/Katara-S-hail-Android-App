package com.app.interfaces

import androidx.lifecycle.Observer
import com.app.model.api.ApiResponseData
import com.app.model.dataclasses.ApiError

interface LiveDataObserver<T>: Observer<ApiResponseData> {
    fun onResponseSuccess(apiCode : Int)
    fun onTokenExpired()
    fun onException()
    override fun onChanged(apiResponse: ApiResponseData) {
        when(apiResponse){
            is ApiResponseData.API_SUCCEED ->{onResponseSuccess(apiResponse.apiCode)}
            is ApiResponseData.API_TOKEN_EXPIRED ->{onTokenExpired()}
            is ApiResponseData.API_EXCEPTION ->{onException()}
        }
    }
}