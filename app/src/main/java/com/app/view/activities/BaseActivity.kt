package com.app.view.activities

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.app.R
import com.app.databinding.ActivityBaseBinding
import com.app.interfaces.LiveDataObserver
import com.app.model.api.ApiResponseData
import com.app.utils.*
import com.app.view.dialogs.ProgressDialog
import com.app.view.fragments.BaseFragment
import com.app.viewmodel.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

abstract class BaseActivity : AppCompatActivity(), LiveDataObserver<ApiResponseData> {
    private lateinit var baseActivityBinding: ActivityBaseBinding
    private var baseViewModel: BaseViewModel? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStatusBarTransparent()
        baseActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        //setLayout()
    }

    override fun attachBaseContext(newBase: Context?) {
        val languageToLoad = SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)
        val locale = Locale(languageToLoad)
        Locale.setDefault(locale)
        val res = newBase!!.resources
        val config = res.configuration
        val curLocale = getLocale(config)
        if (curLocale != locale) {
            Locale.setDefault(locale)
            val conf = Configuration(config)
            conf.setLocale(locale)
            res.updateConfiguration(conf, res.displayMetrics);
        }

        val context: Context = ContextWrapper.changeLang(newBase,locale)
        super.attachBaseContext(context)
    }

    fun getLocale(config: Configuration): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.locales[0]
        } else {
            //noinspection deprecation
            return config.locale;
        }
    }

    /**
     *
     * @param viewModel setting up the any [androidx.lifecycle.ViewModel] extending [BaseViewModel]
     * The main purpose to access any functionality for [BaseViewModel] like showing and hiding [LoadingDialog]
     *
     */
    fun setBaseViewModel(viewModel: BaseViewModel) {
        this.baseViewModel = viewModel
        baseViewModel?.loadingState?.observe(this, Observer { loadingState ->
            if (loadingState != null) {
                when (loadingState) {
                    is LoadingState.LOADING -> showProgressDialog(
                        loadingState.type,
                        loadingState.msg
                    )
                    is LoadingState.LOADED -> hideProgressDialog(
                        loadingState.type,
                        loadingState.msg
                    )
                }
            }
        })
    }

    private fun setStatusBarTransparent() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        window.statusBarColor = Color.TRANSPARENT
    }


    /**
     * Method is used to set the layout in the Base Activity.
     * Layout params of the inserted child is match parent
     */
    /* private fun setLayout() {
         if (resourceId != -1) {
             removeLayout()
             val layoutParams = RelativeLayout.LayoutParams(
                 RelativeLayout.LayoutParams.MATCH_PARENT
                 , RelativeLayout.LayoutParams.MATCH_PARENT
             )
             val layoutInflater =
                 getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
             val view = layoutInflater.inflate(resourceId, null)
             baseActivityBinding.rlBaseContainer.addView(view, layoutParams)
         }
     }*/

    /**
     * hides keyboard onClick anywhere besides edit text
     *
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken,
                0
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Method is used by the sub class for passing the id of the layout ot be inflated in the relative layout
     *
     * @return id of the resource to be inflated
     */
    //protected abstract val resourceId: Int

    fun addFragment(container: Int, fragment: BaseFragment?, tag: String?) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager.beginTransaction()
            .add(container, fragment!!, tag)
            .commit()
    }

    fun addFragmentWithBackstack(
        container: Int,
        fragment: BaseFragment?,
        tag: String?,
    ) {
        supportFragmentManager.beginTransaction()
            .add(container, fragment!!, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragment(container: Int, fragment: BaseFragment?, tag: String?) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) supportFragmentManager.beginTransaction()
            .replace(container, fragment!!, tag)
            .commit()
    }

    fun showFragment(container: Int, fragment: BaseFragment?, tag: String?) {
        val fm: FragmentManager = supportFragmentManager
        fm.beginTransaction()
//            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
            .show(fragment!!)
            .commit()
    }

    fun replaceFragmentWithBackstack(
        container: Int,
        fragment: BaseFragment?,
        tag: String?,
    ) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment!!, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun replaceFragmentWithBackstackWithStateLoss(
        container: Int,
        fragment: BaseFragment?,
        tag: String?,
    ) {
        supportFragmentManager.beginTransaction()
            .replace(container, fragment!!, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()
    }

    /**
     * This method is used to remove the view already present as a child in relative layout.
     */
    private fun removeLayout() {
        if (baseActivityBinding.rlBaseContainer.childCount >= 1) baseActivityBinding.rlBaseContainer.removeAllViews()
    }

    /**
     * hiding the progress dialog
     *
     * @param type type of dialog or kind of sheemer effect
     * @param msg
     */
    open fun showProgressDialog(type: Int, msg: String) {
        if(mProgressDialog!=null && mProgressDialog!!.isShowing){
            mProgressDialog!!.hide()
        }
        if (!isDestroyed) {
            when (type) {
                LoaderType.NORMAL -> {
                    mProgressDialog = ProgressDialog(this as Context)
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.show()
                }
            }

        }


    }

    /**
     * hiding the progress dialog
     *
     * @param type type of dialog or kind of sheemer effect
     * @param msg
     */
    open fun hideProgressDialog(type: Int, msg: String) {
        when (type) {
            LoaderType.NORMAL -> {
                mProgressDialog?.let {
                    if (it.isShowing)
                        it.dismiss()
                }
            }
        }
    }

    fun showToastShort(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showToastLong(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    /**
     * function to show the snack bar
     *
     * @param text message text
     */
    fun showSnackBar(text: String) {
        mSnackBar?.dismiss()
        mSnackBar = Snackbar.make(baseActivityBinding.rlBaseContainer, text, Snackbar.LENGTH_LONG)
        val sbView: View = mSnackBar!!.view
        //if (AppUtils.hasScreenNavigation(this)) sbView.layoutParams = getSnackBarLayoutParams(sbView)
        val bgColor: Int
        val textColor: Int
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_DARK_THEME)) {
            bgColor = R.color.primary
            textColor = R.color.primary
        } else {
            bgColor = R.color.primary
            textColor = R.color.primary
        }
        sbView.setBackgroundColor(ContextCompat.getColor(this, bgColor))
        val sbText: TextView = sbView.findViewById(R.id.snackbar_text)
        sbText.setTextColor(ContextCompat.getColor(this, textColor))
        sbText.setBackgroundResource(bgColor)
        mSnackBar!!.show()

    }

    /**
     * get snackbar layout params
     *
     * @param snackBarView of message
     * @return [FrameLayout.LayoutParams]
     */
    /*open fun getSnackBarLayoutParams(snackBarView: View): FrameLayout.LayoutParams? {
        val params =
            snackBarView.layoutParams as FrameLayout.LayoutParams
        params.setMargins(
            params.leftMargin, params.topMargin, params.rightMargin,
            params.bottomMargin + bottomNavigationHeight()
        )
        return params
    }*/

    /**
     * function to get height of bottom navigation
     *
     * @return height
     */
    /*open fun bottomNavigationHeight(): Int {
        val resourceId =
            resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }*/

    fun popFragment() {
        supportFragmentManager.popBackStackImmediate()
    }

    /**
     * Will be called when there is api success
     *
     * @param statusCode code to identify weather api succeed or fail
     * @param apiCode code to identify the api which is called based on [com.app.core.util.ApiCodes]
     * @param msg any error or success message
     */
    override fun onResponseSuccess(apiCode: Int) {
        hideProgressDialog(LoaderType.NORMAL, "")
        when (apiCode) {
//            ApiCodes.LOGIN -> {
//                baseViewModel?.let {
//                    finishAffinity()
//                }

        }

    }

    override fun onTokenExpired() {
        AppUtils.logout(this)
    }

    override fun onException() {
        showToastLong(resources.getString(R.string.something_went_wrong))
    }



    /**
     *  In order to show keyboard
     *
     */
    open fun showSoftKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(
            InputMethodManager.SHOW_IMPLICIT,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    open fun hideKeyboard() {
        try {
            val inputManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun isOnline(): Boolean {
        val conMgr =
            applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo: NetworkInfo? = null
        if (conMgr != null) {
            netInfo = conMgr.activeNetworkInfo
        }
        if (!(netInfo == null || !netInfo.isConnected || !netInfo.isAvailable)) {
            return true
        }
        return false
    }

}
