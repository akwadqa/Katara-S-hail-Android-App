package com.app.view.fragments

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.app.R
import com.app.model.api.ApiResponseData
import com.app.model.dataclasses.ApiError
import com.app.utils.AppUtils
import com.app.utils.LoadingState
import com.app.view.activities.BaseActivity
import com.app.viewmodel.BaseViewModel


abstract class BaseFragment : Fragment(), com.app.interfaces.LiveDataObserver<ApiResponseData> {
    private var fragmentBaseViewModel: BaseViewModel? = null
    abstract fun initialiseFragmentBaseViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialiseFragmentBaseViewModel()
        initialiseLiveData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseLiveData() {
        fragmentBaseViewModel?.loadingState?.observe(this@BaseFragment.viewLifecycleOwner, Observer { loadingState ->
            if (loadingState != null) {
                when (loadingState) {
                    is LoadingState.LOADING -> showProgressDialog(loadingState.type,loadingState.msg)
                    is LoadingState.LOADED -> hideProgressDialog(loadingState.type,loadingState.msg)
                }
            }
        })
    }


    /**
     *
     * @param viewModel setting up the any [androidx.lifecycle.ViewModel] extending [BaseViewModel]
     * The main purpose to access any functionality for [BaseViewModel] like showing and hiding [LoadingDialog]
     *
     */
    protected fun setFragmentBaseViewModel(viewModel: BaseViewModel){
        this.fragmentBaseViewModel = viewModel
    }


    fun showSnackBar(message: String) {
        activity?.let {
            if (it is BaseActivity) {
                it.showSnackBar(message)
            }
        }
    }

    fun showProgressDialog(type: Int, msg: String) {
        (activity as BaseActivity?)?.showProgressDialog(type, msg)
    }

    fun hideProgressDialog(type: Int, msg: String) {
        (activity as BaseActivity?)?.hideProgressDialog(type, msg)
    }


    /**
     * Will be called when there is api success
     *
     * @param statusCode code to identify weather api succeed or fail
     * @param apiCode code to identify the api which is called based on [com.app.core.util.ApiCodes]
     * @param msg any error or success message
     */
    override fun onResponseSuccess( apiCode: Int) {
//        fragmentBaseViewModel?.setLoadingState(LoadingState.LOADED)
    }

    override fun onTokenExpired() {
        if (isAdded() && activity != null) {
            AppUtils.logout(requireActivity())
        }
    }

    override fun onException() {
        showToastLong(resources.getString(R.string.something_went_wrong))
    }

    open fun isOnline(): Boolean {
        val conMgr =
            requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        var netInfo: NetworkInfo? = null
        if (conMgr != null) {
            netInfo = conMgr.activeNetworkInfo
        }
        if (!(netInfo == null || !netInfo.isConnected || !netInfo.isAvailable)) {
            return true
        }
        return false
    }

    fun showToastLong(message: String) {
        Toast.makeText(this@BaseFragment.requireActivity(), message, Toast.LENGTH_LONG).show()
    }

}