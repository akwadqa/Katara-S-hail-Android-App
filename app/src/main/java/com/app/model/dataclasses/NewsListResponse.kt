package com.app.model.dataclasses

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.io.Serializable

class NewsListResponse : Serializable{
    @SerializedName("newsId")
    @Expose
    var newsId: String? = null

    @SerializedName("newsTitleAr")
    @Expose
    var newsTitleAr: String? = null

    @SerializedName("newsTitleEn")
    @Expose
    var newsTitleEn: String? = null

    @SerializedName("newsHeaderAr")
    @Expose
    var newsHeaderAr: String? = null

    @SerializedName("newsHeaderEn")
    @Expose
    var newsHeaderEn: String? = null

    @SerializedName("newsDetailAr")
    @Expose
    var newsDetailAr: String? = null

    @SerializedName("newsDetailEn")
    @Expose
    var newsDetailEn: String? = null

    @SerializedName("newsExternalLink")
    @Expose
    var newsExternalLink: String? = null

    @SerializedName("newsImageLink")
    @Expose
    var newsImageLink: String? = null

    @SerializedName("activeNews")
    @Expose
    var activeNews: Boolean? = null

    @SerializedName("newsDate")
    @Expose
    var newsDate: String? = null
}