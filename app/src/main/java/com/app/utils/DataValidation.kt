package com.app.utils

import com.app.R

/**
 * This is common validation validation model used to check validation type throught the application
 *
 * @property message error message that is to show
 * @property type identify the type of erro if there are mutiple error meassage on the same screen
 */
data class DataValidation(
    val message: String = com.app.BaseApplication.instance.applicationContext
        .getString(R.string.something_went_wrong),
    var value: Boolean = true
)
