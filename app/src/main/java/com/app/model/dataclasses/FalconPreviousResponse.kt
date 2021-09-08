package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FalconPreviousResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("msgEn")
    @Expose
    var msgEn: String? = null

    @SerializedName("msgAr")
    @Expose
    var msgAr: String? = null

    @SerializedName("responce")
    @Expose
    var responce: List<FalconPreviousListResponse>? = null
}