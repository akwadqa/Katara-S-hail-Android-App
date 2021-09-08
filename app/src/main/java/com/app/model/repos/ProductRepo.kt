package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest


class ProductRepo : GenericApiRequest<Any>() {

    suspend fun getShopList() : Any? {
        return apiRequest {
            ApiManager.apiClient.getShopList()
        }
    }

    suspend fun getProductList() : Any? {
        return apiRequest {
            ApiManager.apiClient.getProductList()
        }
    }

    suspend fun getShopDetails(shopId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getShopDetailsApi(shopId)
        }
    }



}