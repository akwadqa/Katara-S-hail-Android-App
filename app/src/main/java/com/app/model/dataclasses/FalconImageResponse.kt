package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FalconImageResponse {

    @SerializedName("docOrder")
    @Expose
    var docOrder: Int? = 0

    @SerializedName("docType")
    @Expose
    var docType: String? = null

    @SerializedName("docImageURL")
    @Expose
    var docImageURL: String? = null

    @SerializedName("docVideoURL")
    @Expose
    var docVideoURL: String? = null

    @SerializedName("falconId")
    @Expose
    var falconId: String? = null
}