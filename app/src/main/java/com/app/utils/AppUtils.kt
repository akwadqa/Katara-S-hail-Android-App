package com.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import com.app.view.activities.SplashActivity

object AppUtils {

    /**
     * check that device has screen navigation
     * @param activity [Activity]
     * @return hasSoftwareKeys [Boolean]
     */
    fun hasScreenNavigation(activity: Activity): Boolean {
        val hasSoftwareKeys: Boolean
        val d = activity.windowManager.defaultDisplay
        val realDisplayMetrics = DisplayMetrics()
        d.getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels
        val displayMetrics = DisplayMetrics()
        d.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels
        hasSoftwareKeys = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0

        return hasSoftwareKeys
    }

    fun isInternetAvailable(context: Context? = com.app.BaseApplication.instance.applicationContext): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            nwInfo.isConnected
        }
    }


    //validation methods
    fun isNumericString(userInput: String): Boolean {
        return userInput.matches(Regex("^[0-9]*$"))
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return phoneNumber.matches(Regex("^[0-9]{10}$"))
    }

    fun isEmailValid(emailId: String): Boolean {
        val EMAIL_PATTERN = ("[A-Z0-9a-z.-_+]+[A-Z0-9a-z]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,50}")
        return if (emailId.length < 3 || emailId.length > 265) false else {
            emailId.matches(Regex(EMAIL_PATTERN))
        }
    }


    //This will check weather enter password contains one lower case, one upper case, one digit and one special symbol with atleast 8 characters
    fun isPasswordValid(password: String): Boolean {
        val regex = ("^(?=.*[0-9])"
                + "(?=.*[a-zA-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$")
        return password.matches(Regex(regex))
    }

    fun logout(context: Activity)
    {
        val languageToLoad = SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)
        SharedPreferencesManager.clearAllPreferences(context)
        SharedPreferencesManager.put(AppConstants.LANGUAGE,languageToLoad)
        val intent = Intent(context, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        context.finish()
    }

}