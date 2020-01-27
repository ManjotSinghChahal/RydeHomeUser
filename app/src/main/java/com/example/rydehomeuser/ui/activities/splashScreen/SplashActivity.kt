package com.example.rydehomedriver.ui.activities.splashScreen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.rydehomedriver.utils.GrantPermissions
import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.LocationService
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.android.gms.common.util.IOUtils.toByteArray
import android.provider.SyncStateContract.Helpers.update
import android.content.pm.PackageInfo
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : Activity(), SplashView {

    private val PermissionsRequestCode = 101
    lateinit var presenter: SplashPresenter
    lateinit var managePermissions: GrantPermissions
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 2000 //2 seconds
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
       /* requestWindowFeature(FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)


        presenter = SplashPresenter(this, SplashInteractor())




        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        // Initialize a new instance of ManagePermissions class
        managePermissions = GrantPermissions(this, list, PermissionsRequestCode,this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
        else
            presenter.onTimeOut()
    }


    override fun onHandleTimeout() {

        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {



        when (requestCode) {
            PermissionsRequestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)

                presenter.onTimeOut()
                startService(Intent(this, LocationService::class.java))
                if (isPermissionsGranted) {
                    //  toast("Permissions granted.")
                } else {
                    //  toast("Permissions denied.")
                }
                return
            }
        }

    }

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {



            if (SharedPrefUtil?.getInstance()?.isLogin()!!)
            {
                val intent = Intent(applicationContext, Home::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
            else
            {
                val intent = Intent(applicationContext, SignUp::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }



        }
    }

    public override fun onDestroy() {



        super.onDestroy()
    }

    override fun onPause() {
        if (mDelayHandler != null)
            mDelayHandler!!.removeCallbacks(mRunnable)
        super.onPause()
    }

    override fun onRestart() {
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()

        if (checkPermissionForLocation(this)) {
            startService(Intent(this, LocationService::class.java))
        }
    }


    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }


}

