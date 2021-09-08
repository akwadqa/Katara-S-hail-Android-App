package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest


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



}