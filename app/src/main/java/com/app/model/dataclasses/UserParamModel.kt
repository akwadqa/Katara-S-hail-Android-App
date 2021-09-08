package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class UserParamModel(

    @SerializedName(AppConstants.VALUE)
    var value: String? = null,
    )
