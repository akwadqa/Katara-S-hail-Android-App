package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.*


class RegisterRepo : GenericApiRequest<Any>() {

    suspend fun checkQId(qid: String) : Any? {
        return apiRequest {
            ApiManager.apiClient.checkQID(qid)
        }
    }

    suspend fun hitExtractQID(loginCredentials: QIDExtractParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitExtractQID(loginCredentials)
        }
    }

    suspend fun hitLogin(loginCredentials: LoginParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitLogin(loginCredentials)
        }
    }

    suspend fun hitRegisterApi( loginCredentials: RegisterParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitRegisterApi(loginCredentials)
        }
    }

    suspend fun hitRegisterApiForUnknowUser( loginCredentials: RegisterParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitRegisterApiForUnknowUser(loginCredentials)
        }
    }

    suspend fun hitRegisterApiForTicketUser( loginCredentials: RegisterParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitRegisterApiForTicketUser(loginCredentials)
        }
    }

    suspend fun hitRegisterApiForNoneQatariUser( loginCredentials: RegisterParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitRegisterApiForNoneQatariUser(loginCredentials)
        }
    }

    suspend fun getIsdList(): Any?{
        return apiRequest {
            ApiManager.apiClient.getIsdList()
        }
    }

    suspend fun getNationalityList(): Any?{
        return apiRequest {
            ApiManager.apiClient.getNationalityList()
        }
    }

    suspend fun sendMobileOtpApi(number: String, phoneCode: String) : Any? {
        val phone = phoneCode+number
        return apiRequest {
            ApiManager.apiClient.sendMobileOtpApi(phone)
        }
    }

    suspend fun hitVerifyOTPApi(loginCredentials: OTPParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitVerifyOTPApi(loginCredentials)
        }
    }

    suspend fun checkForceUpdate() : Any? {
        return apiRequest {
            ApiManager.apiUpdateClient.checkForceUpdate()
        }
    }
}