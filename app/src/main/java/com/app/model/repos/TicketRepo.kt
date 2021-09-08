package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.UserParamModel
import com.app.model.api.constants.ApiCodes
import com.app.model.dataclasses.OTPParamModel


class TicketRepo : GenericApiRequest<Any>() {

    suspend fun getTicketList(userId : String) : Any? {
        return apiRequest {
            ApiManager.apiClient.getTicketListApi(userId)
        }
    }


}