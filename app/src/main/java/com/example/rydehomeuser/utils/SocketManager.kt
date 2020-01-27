package com.example.rydehomeuser.utils

import android.app.Activity
import android.os.Handler
import android.util.Log
import com.example.rydehomeuser.utils.GlobalHelper.CHAT_SERVER_URL


import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URISyntaxException
import java.util.*

class SocketManager {

    // sockets events
    val CONNECT_USER = "connect_user"
    val CONNECT_LISTENER = "user_online"
    val LISTENER_NEAR_BY_USERS = "send"
    val UPDATE_LOCATION = "update_location"
    val GET_DRIVER_LISTING_LISTENER = "list"
    val GET_DRIVER_LISTING_METHOD = "driver_listing"


    val USER_DISCONNECT = "user_disconnect"

    val ERROR_MESSAGE = "error_message"
    val GET_LIST = "list"

    val NEARBY_USERS_LIST = "nearByUsersList"
    val LISTENER_NEARBY_USERS = "users"




    private var mSocket: Socket? = null
    private val mActivity: Activity? = null
    private var observerList: MutableList<Observer>? = null
    // private var observerBlockUser: MutableList<ObserveBlock>? = null

    private var activeUser = 0
    private val filePath = ""
    private var forDeliveryOnly = false





    private val onUserDisconnect = Emitter.Listener { Log.i("Socket", "ON USERDUSCONNECT") }
    private val onErrorMessage = Emitter.Listener {
        Log.i("Socket", "CONNECTION ERROR MESSAGE")
        for (observer in observerList!!) {
            //            observer.onError(CONNECT_USER, args);
        }

    }








    fun onGetDriverListing(jsonObject: JSONObject?) {
        Log.e("SocketData",jsonObject.toString())
        if (jsonObject != null) {


                        mSocket!!.off(GET_DRIVER_LISTING_LISTENER, onDriverGetListing)
                        mSocket!!.on(GET_DRIVER_LISTING_LISTENER, onDriverGetListing)
                        mSocket!!.emit(GET_DRIVER_LISTING_METHOD, jsonObject)

            }

            Log.i("Socket", "location_update_fetched")
        }





    private val onDriverGetListing = Emitter.Listener { args ->


        try {
            val data = args[0] as JSONArray
            Log.i("SocketDataPrint",data.toString())

            for (observer in observerList!!) {
                observer.onDriverList(GET_LIST, data)
            }

        } catch (ex: Exception) {
            Log.i("Socket",ex.message)
        }
    }



    private val onNearByUsers = Emitter.Listener { args ->


        try {
            val data = args[0] as JSONObject
            Log.i("nearbyUsers",data.toString())

           /* for (observer in observerList!!) {
                observer.onNearbyUsersList(GET_LIST, data)
            }
*/
        } catch (ex: Exception) {
            Log.i("nearbyUsers",ex.message)
            ex.localizedMessage
        }
    }




    private val onConnectListener = Emitter.Listener { args ->
        try {
            //            JSONObject data = (JSONObject) args[0];
            Log.i("Socket", "onConnectionListener : I am online now." /*+ data.getString("error_message")*/)
            //                SharedPrefUtil.getInstance().saveSocketId(data.getString("socket_id"));
        } catch (ex: Exception) { }
    }




    fun init() {

        initializeSocket()
    }



    private fun initializeSocket() {
        if (mSocket == null) {
            mSocket = getSocket()
        }
        if (observerList == null || observerList!!.size == 0) {
            observerList = ArrayList()
        }
        disconnectAll()
        mSocket!!.on(Socket.EVENT_CONNECT, onConnect)
        mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect)
        mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
      //  mSocket!!.on(ERROR_MESSAGE, onErrorMessage)
        mSocket!!.connect()

    }

    private val onConnect = Emitter.Listener { args ->
        Log.i("Socket", "CONNECTED")
        if (isConnected()) {
            try {
                Log.i("Socket", "  want to go online")
                val jsonObject = JSONObject()
                jsonObject.put("type", "1")
                jsonObject.put("driver_id", SharedPrefUtil.getInstance()!!.getUserId())
                mSocket!!.off(CONNECT_LISTENER)
                mSocket!!.on(CONNECT_LISTENER, onConnectListener)
                //                eventUserDisconnect();
                mSocket!!.emit(CONNECT_USER, jsonObject)


            } catch (e: JSONException) {
                e.printStackTrace()
                // Log.e("gttgtgtgtg",e.message)
            }

        } else {
            initializeSocket()
        }
    }

    private val onDisconnect = Emitter.Listener { Log.i("Socket", "DISCONNECTED") }
    private val onConnectError = Emitter.Listener {
        Log.i("Socket", "CONNECTION ERROR")

    }

    fun getmSocket(): Socket? {
        return mSocket
    }

    fun isConnected(): Boolean {
        //        mSocket.
        return mSocket != null && mSocket!!.connected()
    }



    fun disconnectAll() {
        if (mSocket != null) {

            mSocket!!.off(Socket.EVENT_CONNECT, onConnect)
            mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect)
            mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
            mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError)
            mSocket!!.off(GET_DRIVER_LISTING_LISTENER, onDriverGetListing)

            mSocket!!.off()
            mSocket!!.disconnect()
        }
    }

    fun disconnectGetListing()
    {
        if (mSocket != null) {
            mSocket!!.off(GET_DRIVER_LISTING_LISTENER, onDriverGetListing)
        }
    }


    fun getSocket(): Socket? {
        run {
            try {
                mSocket = IO.socket(CHAT_SERVER_URL)
            } catch (e: URISyntaxException) {
                throw RuntimeException(e)
            }
        }
        return mSocket
    }

    fun onRegister(observer: Observer) {
        if (observerList != null && !observerList!!.contains(observer)) {
            observerList!!.add(observer)
        } else {
            observerList = ArrayList()
            observerList!!.add(observer)
        }
    }

    fun unRegister(observer: Observer) {

        try {
            if (observerList != null) {
                for (i in observerList!!.indices) {
                    val model = observerList!![i]
                    if (model === observer) {
                        observerList!!.remove(model)
                    }
                }
            }
        }catch (ex : Exception)
        {
        }

    }













    private fun convertFileToBytes(filePath: String): ByteArray {
        val file = File(filePath)
        val size = file.length().toInt()
        val chunks = ByteArray(size)
        Log.i("Chunks", " : " + file.length() + "  :  " + file.length() / 1000)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(chunks, 0, chunks.size)
            buf.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return chunks
    }

    protected fun getExtension(selectedImage: String): String {
        return selectedImage.substring(selectedImage.lastIndexOf(".") + 1)
    }





    interface Observer {


     //   fun onChatDetail(event: String, args: JSONArray)
        fun onDriverList(event: String, args: JSONArray)
       /* fun onTotalPassed(event: String, args: JSONObject)
        fun onNearbyUsersList(event: String, args: JSONObject)
        fun onResponseArray(event : String, args : JSONArray)
        fun onResponseObject(event : String, args : JSONObject)*/
    }



}