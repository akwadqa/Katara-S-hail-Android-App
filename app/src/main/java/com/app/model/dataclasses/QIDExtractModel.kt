package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QIDExtractModel : Serializable {

    @SerializedName("d")
    @Expose
    var data: QIDExtractDataModel? = null
}