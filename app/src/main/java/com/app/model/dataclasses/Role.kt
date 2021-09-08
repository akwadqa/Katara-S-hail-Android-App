package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Role {
    @SerializedName("roleId")
    @Expose
    var roleId: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("userRoles")
    @Expose
    var userRoles: List<Role>? = null
}