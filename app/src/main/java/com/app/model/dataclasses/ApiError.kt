package com.app.model.dataclasses

open class ApiError {
    var status: Boolean = false
    var message: String? = null
    var result: Any? = null
    var statusCode:Int?=null
}