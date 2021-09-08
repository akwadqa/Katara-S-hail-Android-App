package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FalconCategoryResponse {
    @SerializedName("falconCategoryId")
    @Expose
    var falconCategoryId: String? = null

    @SerializedName("falconCategoryAr")
    @Expose
    var falconCategoryAr: String? = null

    @SerializedName("falconCategoryEn")
    @Expose
    var falconCategoryEn: String? = null

    @SerializedName("falconCategoryCode")
    @Expose
    var falconCategoryCode: String? = null


}