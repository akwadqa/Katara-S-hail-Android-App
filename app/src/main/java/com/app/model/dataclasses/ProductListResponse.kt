package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductListResponse : Serializable {

    @SerializedName("productId")
    @Expose
    var productId: String? = null

    @SerializedName("productName")
    @Expose
    var productName: String? = null

    @SerializedName("productCategoryEn")
    @Expose
    var productCategoryEn: Any? = null

    @SerializedName("productCategoryAr")
    @Expose
    var productCategoryAr: Any? = null

    @SerializedName("productDescription")
    @Expose
    var productDescription: String? = null

    @SerializedName("productNameAr")
    @Expose
    var productNameAr: String? = null

    @SerializedName("productDescriptionAr")
    @Expose
    var productDescriptionAr: String? = null

    @SerializedName("productPrice")
    @Expose
    var productPrice: Int? = null

    @SerializedName("docImage")
    @Expose
    var docImage: String? = null

    @SerializedName("docImageContent")
    @Expose
    var docImageContent: Any? = null

    @SerializedName("shopId")
    @Expose
    var shopId: String? = null

    @SerializedName("shop")
    @Expose
    var shop: ShopListResponse? = null

    @SerializedName("bestSelection")
    @Expose
    var bestSelection: Boolean? = null
}