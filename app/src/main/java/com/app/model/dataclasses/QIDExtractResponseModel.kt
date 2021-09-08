package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QIDExtractResponseModel : Serializable {

    @SerializedName(AppConstants.ERROR)
    var error: Boolean? = false

    @SerializedName("msgEn")
    var msgEn: String? = null

    @SerializedName("msgAr")
    var msgAr: String? = null

    @SerializedName("moiError")
    var moiError: String? = null

    @SerializedName("qidDetails")
    @Expose
    var qidDetails: QIDExtractModel? = null
}
