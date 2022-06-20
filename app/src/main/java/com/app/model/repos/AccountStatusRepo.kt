package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.*


class AccountStatusRepo : GenericApiRequest<Any>() {


    suspend fun hitRefreshUser(loginCredentials: AccountStatusParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitRefreshUser(loginCredentials)
        }
    }
}