package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserTokenModel {

    @SerializedName("userId")
    @Expose
    var userId: String?= null

    @SerializedName("userQid")
    @Expose
    var userQid: String? = null

    @SerializedName("userNationality")
    @Expose
    var userNationality: String? = null

    @SerializedName("userNationalityCode")
    @Expose
    var userNationalityCode: String? = null

    @SerializedName("holderName")
    @Expose
    var holderName: String? = null

    @SerializedName("userName")
    @Expose
    var userName: String? = null

    @SerializedName("userNameAr")
    @Expose
    var userNameAr: String? = null

    @SerializedName("userLastOtp")
    @Expose
    var userLastOtp: String? = null

    @SerializedName("phoneNumber")
    @Expose
    var phoneNumber: String? = null

    @SerializedName("userEmail")
    @Expose
    var userEmail: String? = null

    @SerializedName("userType")
    @Expose
    var userType: Int? = null

    @SerializedName("active")
    @Expose
    var active: Boolean? = null

    @SerializedName("token")
    @Expose
    var token: String? = null

    @SerializedName("passwordHash")
    @Expose
    var passwordHash: String? = null

    @SerializedName("phoneNumberConfirmed")
    @Expose
    var phoneNumberConfirmed: Boolean? = null

    @SerializedName("isValidated")
    @Expose
    var isValidated: Boolean? = null

    @SerializedName("isPayed")
    @Expose
    var isPayed: Boolean? = null

    @SerializedName("isInHold")
    @Expose
    var isInHold: Boolean? = null

    @SerializedName("isForAction")
    @Expose
    var isForAction: Boolean? = null

    @SerializedName("phoneCode")
    @Expose
    var phoneCode: String? = null

    @SerializedName("birthDate")
    @Expose
    var birthDate: String? = null

    @SerializedName("goldenPass")
    @Expose
    var goldenPass: String? = null

    @SerializedName("userRoles")
    @Expose
    var userRoles: List<UserRoles>? = null

    @SerializedName("userPaymentLink")
    @Expose
    var userPaymentLink: String? = null

}