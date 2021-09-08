package com.app.model.repos

import com.app.model.api.ApiManager
import com.app.model.api.GenericApiRequest
import com.app.model.dataclasses.BetParamModel


class AuctionRepo : GenericApiRequest<Any>() {

    suspend fun getPreviousAuctionList(): Any? {
        return apiRequest {
            ApiManager.apiClient.getPreviousAuctionList()
        }
    }

    suspend fun getAuction10List(falconId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getAuction10List(falconId)
        }
    }

    suspend fun getLastBet(): Any? {
        return apiRequest {
            ApiManager.apiClient.getLastBet()
        }
    }

    suspend fun getAuctionDetails(falconId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getAuctionDetails(falconId)
        }
    }

    suspend fun getAuctionPreviousDetails(falconId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getAuctionPreviousDetails(falconId)
        }
    }


    suspend fun getAuctionMaxPrice(falconId: String?): Any? {
        return apiRequest {
            ApiManager.apiClient.getAuctionMaxPrice(falconId)
        }
    }

    suspend fun hitMakeBet(betModel : BetParamModel): Any? {
        return apiRequest {
            ApiManager.apiClient.hitMakeBetApi(betModel)
        }
    }

}