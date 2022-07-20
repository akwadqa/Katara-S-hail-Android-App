package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.AccountStatusParamModel


class HomeRepo : GenericApiRequest<Any>() {

    suspend fun getNewsList() : Any? {
        return apiRequest {
            ApiManager.apiClient.getNewsApi()
        }
    }

    suspend fun getBannerList() : Any? {
        return apiRequest {
            ApiManager.apiClient.getBannerApi()
        }
    }

    suspend fun getFalconCategory() : Any? {
        return apiRequest {
            ApiManager.apiClient.getFalconCategoryApi()
        }
    }

    suspend fun getFalconList(falconCategoryId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getFalconApi(falconCategoryId)
        }
    }

    suspend fun requestAccountUpgrade(userId: String) : Any? {
        return apiRequest {
            ApiManager.apiClient.requestAccountUpgrade(AccountStatusParamModel(key = userId, value = "upgrade"))
        }
    }

    suspend fun requestDeleteAccount(userId: String) : Any? {
        return apiRequest {
            ApiManager.apiClient.requestDeleteAccount(AccountStatusParamModel(key = userId, value = "Z3Af%=JC95FNe23Q2ma+-5TK3WG--XU+"))
        }
    }

}