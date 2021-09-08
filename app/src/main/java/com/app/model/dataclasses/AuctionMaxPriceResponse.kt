package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuctionMaxPriceResponse {
    @SerializedName("memberNameAr")
    @Expose
    var memberNameAr: String? = null

    @SerializedName("memberNameEn")
    @Expose
    var memberNameEn: String? = null


    @SerializedName("maxPrice")
    @Expose
    var maxPrice: Int? = 0

}