package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.BookTicketMainParamModel


class BookTicketRepo : GenericApiRequest<Any>() {

    suspend fun hitBookTicketApi(loginCredentials: BookTicketMainParamModel) : Any? {
        return apiRequest {
            ApiManager.apiClient.hitBookTicketApi(loginCredentials)
        }
    }

    suspend fun getTicketConfigApi() : Any? {
        return apiRequest {
            ApiManager.apiClient.getTicketConfigApi()
        }
    }

    suspend fun getTicketDataApi() : Any? {
        return apiRequest {
            ApiManager.apiClient.getTicketDataApi()
        }
    }


}