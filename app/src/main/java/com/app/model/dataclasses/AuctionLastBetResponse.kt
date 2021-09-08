package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuctionLastBetResponse {
    @SerializedName("aucctionId")
    @Expose
    var aucctionId: String? = null

    @SerializedName("falconId")
    @Expose
    var falconId: String? = null

    @SerializedName("falcon")
    @Expose
    var falcon: FalconListResponse? = null

    @SerializedName("user")
    @Expose
    var user: UserTokenModel? = null

    @SerializedName("entryDate")
    @Expose
    var entryDate: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Int? = 0

}