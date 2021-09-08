package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForceUpdateResponse {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("msgEn")
    @Expose
    var msgEn: String? = null

    @SerializedName("msgAr")
    @Expose
    var msgAr: String? = null

    @SerializedName("android_version")
    @Expose
    var androidVersion: String? = null

    @SerializedName("mandatory")
    @Expose
    var mandatory: Boolean? = null
}