package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FalconListResponse {
    @SerializedName("falconId")
    @Expose
    var falconId: String? = null

    @SerializedName("falconName")
    @Expose
    var falconName: String? = null

    @SerializedName("falconNameEn")
    @Expose
    var falconNameEn: String? = null

    @SerializedName("falconType")
    @Expose
    var falconType: String? = null

    @SerializedName("falconAge")
    @Expose
    var falconAge: Int? = null

    @SerializedName("falconBirth")
    @Expose
    var falconBirth: String? = null

    @SerializedName("falconDescription")
    @Expose
    var falconDescription: String? = null

    @SerializedName("falconDescriptionEn")
    @Expose
    var falconDescriptionEn: String? = null

    @SerializedName("falconImageMobiles")
    @Expose
    var falconImageMobiles: List<FalconImageResponse>? = null

    @SerializedName("falconStatus")
    @Expose
    var falconStatus: Int? = null

    @SerializedName("limitAucctionTime")
    @Expose
    var limitAucctionTime: String? = null

    @SerializedName("startingPrice")
    @Expose
    var startingPrice: Int? = null

    @SerializedName("minimalBet")
    @Expose
    var minimalBet: Int? = null

    @SerializedName("falconMaxPrice")
    @Expose
    var falconMaxPrice: Int? = null


}