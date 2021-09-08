package com.app.model.dataclasses

import com.google.gson.annotations.SerializedName

data class QIDExtractParamModel(

    @SerializedName("qid")
    var qid: String? = null,

    @SerializedName("exp")
    var exp: String? = null,

    )
