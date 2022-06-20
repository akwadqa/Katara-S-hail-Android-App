package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName
data class GeneralResponseModel(
    @SerializedName(AppConstants.ERROR) val error: Boolean,
    @SerializedName("msgEn") val msgEn: String,
    @SerializedName("msgAr") val msgAr: String,
    @SerializedName("responce") val response: Boolean,
)
