package com.app.model.dataclasses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TicketSlotsResponse {
    @SerializedName("timeSlotId")
    @Expose
    var timeSlotId: String? = null

    @SerializedName("slotOrder")
    @Expose
    var slotOrder: Int? = null

    @SerializedName("displayNameAr")
    @Expose
    var displayNameAr: String? = null

    @SerializedName("displayNameEn")
    @Expose
    var displayNameEn: String? = null

    @SerializedName("slotCapacity")
    @Expose
    var slotCapacity: Int? = null

    @SerializedName("timeStart")
    @Expose
    var timeStart: String? = null

    @SerializedName("timeEnd")
    @Expose
    var timeEnd: String? = null

    @SerializedName("bookingDateId")
    @Expose
    var bookingDateId: String? = null

}