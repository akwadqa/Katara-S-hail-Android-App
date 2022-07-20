package com.app.view.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.BuildConfig
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.model.api.API_VIEWMODEL_DATA
import com.app.model.dataclasses.AccountStatusParamModel
import com.app.model.dataclasses.UserTokenModel
import com.app.model.repos.AccountStatusRepo
import com.app.utils.*
import com.app.view.fragments.AccountFragment
import com.app.view.fragments.HomeFragment
import com.app.view.fragments.ProductFragment
import com.app.view.fragments.TicketFragment
import com.app.viewmodel.HomeViewModel
import com.google.gson.Gson
import java.util.*

class MainActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private var mLastHomeClick: Long = 0
    private var mLastTicketClick: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setObservers()
        setData()
        val isFrom = intent.getBooleanExtra("fromAccount",false)
        if(isFrom) {
            binding.tvHome.performClick()
        }
        else {
            when {
                SharedPreferencesManager.getString(AppConstants.USER_ROLE) == "GEST" -> if (SharedPreferencesManager.getInt(
                        AppConstants.isGuest) == 1
                ) binding.tvAccount.performClick() else binding.tvTicket.performClick()
                else -> binding.tvHome.performClick()
            }
        }
    }

    private fun setObservers() {
        homeViewModel.getResponseObserver().observe(this, this)
    }

    private fun setData() {
        if(SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE) == "en")
            SharedPreferencesManager.put(AppConstants.IS_ARABIC,false)
        else
            SharedPreferencesManager.put(AppConstants.IS_ARABIC,true)
        if (SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
        {
            if(SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR).isEmpty())
                binding.tvName.text =
                    SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)
            else
                binding.tvName.text =
                    SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME_AR)
        }
        else
            binding.tvName.text = SharedPreferencesManager.getString(AppConstants.USER_FIRST_NAME)

        if(binding.tvName.text.trim().isEmpty())
            binding.tvName.text = resources.getString(R.string.guest)

        if(SharedPreferencesManager.getString(AppConstants.USER_PHONE).toString().isNotEmpty())
            binding.tvNumber.text = SharedPreferencesManager.getString(AppConstants.PHONECODE)+ " - " + SharedPreferencesManager.getString(AppConstants.USER_PHONE)

        if(SharedPreferencesManager.getString(AppConstants.USER_ROLE) == "ADMIN")
            binding.tvLogout.visibility = View.GONE
        else
            binding.tvLogout.visibility = View.VISIBLE
        binding.tvVersion.text = resources.getString(R.string.version) + " " + BuildConfig.VERSION_NAME
        if(!SharedPreferencesManager.getBoolean(AppConstants.IS_FOR_ACTION)){
            binding.tvUpgrade.visibility = View.VISIBLE
            binding.tvUpgrade.setOnClickListener(this)
        }
    }

    private fun setListeners() {
        binding.tvHome.setOnClickListener(this)
        binding.tvTicket.setOnClickListener(this)
        binding.tvAccount.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
        binding.tvLogout.setOnClickListener(this)
        binding.tvChangeLanguage.setOnClickListener(this)
        binding.ivIntsa.setOnClickListener(this)
        binding.ivFb.setOnClickListener(this)
        binding.ivTwitter.setOnClickListener(this)
        binding.tvEmail.setOnClickListener(this)
        binding.tvContact.setOnClickListener(this)
        binding.tvLocation.setOnClickListener(this)
        binding.tvExhibition.setOnClickListener(this)
        binding.tvDelete.setOnClickListener(this)
    }

    fun openDrawer()
    {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    fun openProductFragment()
    {
        setCurrentFragment(ProductFragment())
    }

    fun openHomeFragment()
    {
        setCurrentFragment(HomeFragment())
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentHolder.id,fragment)
                .commitAllowingStateLoss()
        }

    fun hitTicketTab() {
        binding.tvTicket.performClick()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.tvHome.id -> {
                if (SystemClock.elapsedRealtime() - mLastHomeClick < 2000){
                    return
                }
                mLastHomeClick = SystemClock.elapsedRealtime();
                setDefaultUI()
                binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_active, 0, 0)
                binding.tvHome.setTextColor(resources.getColor(R.color.primary))
                binding.ivHome.visibility = View.VISIBLE
                setCurrentFragment(HomeFragment())
            }
            binding.tvTicket.id -> {
                if (SystemClock.elapsedRealtime() - mLastTicketClick < 2000){
                    return
                }
                mLastTicketClick = SystemClock.elapsedRealtime();
                if(SharedPreferencesManager.getString(AppConstants.USER_ROLE) != "ADMIN") {
                    setDefaultUI()
                    binding.tvTicket.setCompoundDrawablesWithIntrinsicBounds(0,
                        R.drawable.ticket_active,
                        0,
                        0)
                    binding.tvTicket.setTextColor(resources.getColor(R.color.primary))
                    binding.ivTicket.visibility = View.VISIBLE
                    setCurrentFragment(TicketFragment())
                }
                else
                    openLogInDialog()
            }
            binding.tvAccount.id -> {
                if(SharedPreferencesManager.getString(AppConstants.USER_ROLE) != "ADMIN")
                {
                    setDefaultUI()
                    binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account_active, 0, 0)
                    binding.tvAccount.setTextColor(resources.getColor(R.color.primary))
                    binding.ivAccount.visibility = View.VISIBLE
                    setCurrentFragment(AccountFragment())
                }
                else
                    openLogInDialog()
            }
            binding.ivBack.id -> {
                openDrawer()

            }
            binding.tvLogout.id -> {
                openLogoutDialog()
            }
            binding.tvChangeLanguage.id -> {
                if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                    setLanguage("en")
                else
                    setLanguage("ar")
            }
            binding.ivIntsa.id -> {
                openBrowser("https://www.instagram.com/shail_qa")
            }
            binding.ivFb.id -> {
                openBrowser("https://www.facebook.com/shailexb")
            }
            binding.ivTwitter.id -> {
                openBrowser("https://twitter.com/shail_qa")
            }
            binding.tvLocation.id -> {
                openBrowser("https://www.google.com/maps/dir/25.3187324,51.5240111/'25.361013794807405,51.524905863820955'/@25.3383661,51.5056895,13.75z/data=!4m8!4m7!1m1!4e1!1m3!2m2!1d51.5249059!2d25.3610138!3e3")
            }
            binding.tvContact.id -> {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:" + "+97455792820") //change the number
                startActivity(callIntent)
            }
            binding.tvEmail.id -> {
                val intent = Intent(Intent.ACTION_SEND)
                val recipients = arrayOf("s-hail@katara.net")
                intent.putExtra(Intent.EXTRA_EMAIL, recipients)
                intent.type = "text/html"
                startActivity(Intent.createChooser(intent, resources.getString(R.string.send_email)))
            }
            binding.tvExhibition.id -> {
                openBrowser("https://shail.katara.net/SignInUp3")
            }
            binding.tvUpgrade.id -> {
                homeViewModel.requestAccountUpgrade(userId = SharedPreferencesManager.getString(AppConstants.USER_ID))
            }
            binding.tvDelete.id -> {
                homeViewModel.requestDeleteAccount(userId = SharedPreferencesManager.getString(AppConstants.USER_ID))
            }
        }
    }

    override fun onResponseSuccess(apiCode: Int) {
        super.onResponseSuccess(apiCode)
        when (apiCode) {
            ApiCodes.REQUEST_ACCOUNT_UPGRADE -> {
                val message: String = if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                    homeViewModel.upgradeResponseData.value!!.msgAr
                else
                    homeViewModel.upgradeResponseData.value!!.msgEn
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            ApiCodes.REQUEST_DELETE_ACCOUNT -> {
                if(homeViewModel.deleteResponseData.value!!.response){
                    val languageToLoad = SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)
                    SharedPreferencesManager.clearAllPreferences(this)
                    SharedPreferencesManager.put(AppConstants.LANGUAGE,languageToLoad)
                    val intent = Intent(this, SplashActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                val message: String = if(SharedPreferencesManager.getBoolean(AppConstants.IS_ARABIC))
                    homeViewModel.deleteResponseData.value!!.msgAr
                else
                    homeViewModel.deleteResponseData.value!!.msgEn
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDefaultUI() {
        binding.tvHome.setTextColor(resources.getColor(R.color.grey))
        binding.tvTicket.setTextColor(resources.getColor(R.color.grey))
        binding.tvAccount.setTextColor(resources.getColor(R.color.grey))
        binding.tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home, 0, 0)
        binding.tvTicket.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ticket, 0, 0)
        binding.tvAccount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.account, 0, 0)
        binding.ivHome.visibility = View.INVISIBLE
        binding.ivTicket.visibility = View.INVISIBLE
        binding.ivAccount.visibility = View.INVISIBLE
    }

    private fun openLogInDialog() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(resources.getString(R.string.login_register))
        builder1.setMessage(resources.getString(R.string.sign_msg))
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            resources.getString(R.string.ok)
        ) { dialog , which ->
            dialog.dismiss()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder1.setNegativeButton(resources.getString(R.string.cancel))
        { dialog , which->
            dialog.dismiss()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    fun openLogoutDialog() {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(resources.getString(R.string.logout))
        builder1.setMessage(resources.getString(R.string.logout_msg))
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            resources.getString(R.string.ok)
        ) { dialog , which ->
            dialog.dismiss()
            val languageToLoad = SharedPreferencesManager.getLanguageString(AppConstants.LANGUAGE)
            SharedPreferencesManager.clearAllPreferences(this)
            SharedPreferencesManager.put(AppConstants.LANGUAGE,languageToLoad)
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        builder1.setNegativeButton(resources.getString(R.string.cancel))
        { dialog , which->
            dialog.dismiss()
        }
        val alert11 = builder1.create()
        alert11.show()
    }

    private fun setLanguage(value: String) {
        SharedPreferencesManager.put(AppConstants.LANGUAGE,value)
        startActivity(intent)
        finish()
    }

    private fun openBrowser(link : String)
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

}