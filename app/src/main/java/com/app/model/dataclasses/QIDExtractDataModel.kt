package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QIDExtractDataModel : Serializable{

    @SerializedName("qid")
    @Expose
    var qid: String? = null

    @SerializedName("nameAr")
    @Expose
    var nameAr: String? = null

    @SerializedName("nameEn")
    @Expose
    var nameEn: String? = null

    @SerializedName("nationalityCode")
    @Expose
    var nationalityCode: Int? = null

    @SerializedName("birthDatDate")
    @Expose
    var birthDatDate: String? = null

    @SerializedName("qidExpiryDate")
    @Expose
    var qidExpiryDate: String? = null

    @SerializedName("gender")
    @Expose
    var gender: Int? = null

    @SerializedName("genderstr")
    @Expose
    var genderstr: String? = null
}