package com.app.model.dataclasses

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class TicketDateTimeResponse {

    @SerializedName("bookingDateId")
    @Expose
    var bookingDateId: String? = null

    @SerializedName("order")
    @Expose
    var order: Int? = null

    @SerializedName("displayNameAr")
    @Expose
    var displayNameAr: String? = null

    @SerializedName("displayNameEn")
    @Expose
    var displayNameEn: String? = null

    @SerializedName("physicalDate")
    @Expose
    var physicalDate: String? = null

    @SerializedName("timeSlots")
    @Expose
    var timeSlots: List<TicketSlotsResponse>? = null
}