package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class RegisterImageParamModel(

    @SerializedName("docPhotoFileContent")
    var docPhotoFileContent: String? = null,

    @SerializedName("docQid1")
    var docQid1: String? = null,

    @SerializedName("docQid1FileContent")
    var docQid1FileContent: String? = null,

    @SerializedName("docQid2")
    var docQid2: String? = null,

    @SerializedName("docQid2FileContent")
    var docQid2FileContent: String? = null,


)
