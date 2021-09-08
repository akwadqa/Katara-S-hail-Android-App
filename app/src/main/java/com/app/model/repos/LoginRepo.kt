package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.UserParamModel
import com.app.model.api.constants.ApiCodes
import com.app.model.dataclasses.OTPParamModel


class LoginRepo : GenericApiRequest<Any>() {

    suspend fun hitSendOTP(loginCredentials: UserParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitSendOTP(loginCredentials)
        }
    }

    suspend fun hitVerifyOTPApi(loginCredentials: OTPParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitVerifyOTPApi(loginCredentials)
        }
    }

}