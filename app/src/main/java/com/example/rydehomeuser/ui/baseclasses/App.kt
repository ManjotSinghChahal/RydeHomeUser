package com.example.rydehomeuser.ui.baseclasses

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.rydehomeuser.utils.GlobalHelper.CHAT_SERVER_URL
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.example.rydehomeuser.utils.SocketManager
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class App : Application(), AppLifecycleHandler.AppLifecycleDelegates
{
    private var mSocket: Socket? = null
    private var lifecycleHandler: AppLifecycleHandler? = null
    private var mSocketManager: SocketManager? = null

    companion object {

        var instance: App? = null
        //   private set

        fun hasNetwork(): Boolean {
            return instance!!.checkIfHasNetwork()
        }

        fun tokenExpired() {
            instance!!.onTokenExpired()
        }
    }

    override fun onCreate() {
        super.onCreate()


        instance = this
        initSharedHelper()
        lifecycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifecycleHandler!!)
        try {
            mSocket = IO.socket(CHAT_SERVER_URL)

        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }


        mSocketManager = getSocketManager()
        connectSocket()


    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }


    override fun onAppBackgrounded() {
        mSocketManager!!.disconnectAll()
    }

    override fun onAppForegrounded() {
        if (SharedPrefUtil.getInstance()?.isLogin()!!) {
        if (!mSocketManager!!.isConnected() || mSocketManager!!.getmSocket() == null) {
            mSocketManager!!.init()
        }
       }
    }


    fun connectSocket()
    {
        if (!mSocketManager!!.isConnected() || mSocketManager!!.getmSocket() == null) {
            mSocketManager!!.init()
        }
    }
    private fun initSharedHelper() {
           SharedPrefUtil.init(this)
    }

    fun checkIfHasNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun onTokenExpired() {
        //        ToastUtils.shortToast(getString(R.string.session_expire));
        /*  val intent = Intent(this, LoginActivity::class.java)
          intent.putExtra(TOKEN_EXPIRED, true)
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
          startActivity(intent)*/
    }
    fun getSocket(): Socket? {
        return null
    }


    public fun getSocketManager(): SocketManager {
        if (mSocketManager == null) {
            mSocketManager = SocketManager()
            return mSocketManager as SocketManager
        } else {
            return mSocketManager as SocketManager
        }
    }


}