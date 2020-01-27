package com.example.rydehomeuser.utils.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.passerby.data.injectorApi.InjectorApi
import com.app.passerby.data.injectorApi.InterfaceApi
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.cancelRide.CancelRide
import com.example.rydehomeuser.data.saveData.notificationData.NotificationData
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.mapActivity.MapActivity
import com.example.rydehomeuser.ui.baseclasses.App
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.NOTIFICATION_DATA
import com.example.rydehomeuser.utils.GlobalHelper.visibleScreen
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val channelId = ""
    private val notificationId = 0


    override fun onNewToken(s: String) {
        super.onNewToken(s)
        SharedPrefUtil.getInstance()!!.saveDeviceToken(s)
        Log.e(TAG, "Refreshed_token: " + s!!)
    }


    //   cqlsystesting19@gmail.com    firebase noti


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)


        //  if back end send json in notification then automatically displayed not in on MessageReceived
        // if back end send json in data then onMessageReceived get called

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.e("notificationFetch", "notification: got it!!!! ")
        Log.e("notificationFetch", "" + remoteMessage!!.data)
       // Log.e("notificationFetch",remoteMessage.data.toString())
        Log.e("notificationFetch->",remoteMessage.from)


        if (visibleScreen.equals(MAP_ACTIVITY))
            Log.e("printVisisblity","11111")
        else
            Log.e("printVisisblity","2222")


        try {

            val jsonOutter = JSONObject(remoteMessage.data)
            if (jsonOutter.getString("type").equals("Pickup_confirmation"))
            {
                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                val jsonObject = JSONObject(remoteMessage.data["value"])
                notiData.driver_id =jsonObject.getString("driver_id")
                notiData.source_lat =jsonObject.getString("from_lat")
                notiData.source_long =jsonObject.getString("from_long")
                notiData.dest_lat = jsonObject.getString("to_lat")
                notiData.dest_long = jsonObject.getString("to_long")
                notiData.ride_id =jsonObject.getString("ride_id")

                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                    sendNotification(notiData)

            }
            else if (jsonOutter.getString("type").equals("Pickup_rejected"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                sendNotification(notiData)
            }

            else if (jsonOutter.getString("type").equals("split_fare_request"))
            {
                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                val jsonObject = JSONObject(remoteMessage.data["value"])
                notiData.ride_id =jsonObject.getString("ride_id")
                notiData.total_amount =jsonObject.getString("total_amount")
                notiData.share_amount =jsonObject.getString("share_amount")
                notiData.fareSplit_name =jsonObject.getString("name")
                notiData.fareSplit_photo =jsonObject.getString("photo")

                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                    sendNotification(notiData)
            }
            else if (jsonOutter.getString("type").equals("split_fare_request_approval"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                val jsonObject = JSONObject(remoteMessage.data["value"])
                notiData.message =  "${jsonOutter.getString("message")} by ${jsonObject.getString("name")}"

                sendNotification(notiData)
            }
            else if (jsonOutter.getString("type").equals("planned_request_confirmed"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")
               // notiData.message = jsonOutter.getString("message")

              //  sendNotification(notiData)
            }
            else if (jsonOutter.getString("type").equals("ride_completed_by_driver"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")


                val jsonObject = JSONObject(remoteMessage.data["value"])
                notiData.ride_id =jsonObject.getString("ride_id")

                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                    sendNotification(notiData)
            }

            else if (jsonOutter.getString("type").equals("ride_started_for_pickup"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")


                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                    sendNotification(notiData)
            }

            else if (jsonOutter.getString("type").equals("destination_change_confirm"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")


                if (visibleScreen.equals(MAP_ACTIVITY))
                    sendBroadCastUpdateList(notiData)
                else
                    sendNotification(notiData)
            }

            else if (jsonOutter.getString("type").equals("ride_started_by_driver"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                sendNotification(notiData)
            }
            else if (jsonOutter.getString("type").equals("ride_reached_by_driver"))
            {

                val notiData = NotificationData()
                notiData.type = jsonOutter.getString("type")
                notiData.title = jsonOutter.getString("title")
                notiData.message = jsonOutter.getString("message")

                sendNotification(notiData)
            }




        } catch (ex: Exception) {
            Log.e("notification","this in notification " + ex.localizedMessage)
        //    sendNotification(getString(R.string.app_name)), remoteMessage.data["message"], 101, "")
        }



    }


    private fun sendNotification(notiData: NotificationData) {
        val intent = Intent(this, MapActivity::class.java)
        intent.putExtra(NOTIFICATION_DATA,notiData)

        val NOTIFICATION_CHANNEL_ID = "channel_id"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = java.lang.Long.toString(System.currentTimeMillis())
        val bitmap: Bitmap? = null
        //        ToastUtils.shortToast("sendNotification");
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            //                Bitmap bitmap;
            //                .setLargeIcon(bitmap)
            .setContentTitle(notiData.title).setContentText(notiData.message).setAutoCancel(true).setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         //   var mChannel: NotificationChannel? = null
            var   mChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            mChannel.description = notiData.message
            mChannel.enableLights(true)
          //  mChannel.setShowBadge(true)
            mChannel.lightColor = ContextCompat.getColor(this, R.color.colorPrimary)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(mChannel)
        } else {
            notificationBuilder.setVibrate(longArrayOf(100, 250))
                .setLights(ContextCompat.getColor(this, R.color.colorPrimary), 500, 5000)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
        notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
      //  notificationManager.notify(notificationId, notificationBuilder.build())
        notificationManager.notify(notiData.message.hashCode(), notificationBuilder.build())  // passed unique code ot avoid notif to override

    }


    private fun sendEmptyNotification(title: String,body : String)
    {
        val intent = Intent(this, Home::class.java)
        intent.putExtra("valNotiEmpty","hello")


        val NOTIFICATION_CHANNEL_ID = "channel_id"
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.action = java.lang.Long.toString(System.currentTimeMillis())
        val bitmap: Bitmap? = null
        //        ToastUtils.shortToast("sendNotification");
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            //                Bitmap bitmap;
            //                .setLargeIcon(bitmap)
            .setContentTitle(title).setContentText(body).setAutoCancel(true).setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //   var mChannel: NotificationChannel? = null
            var   mChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            mChannel.description = ""
            mChannel.enableLights(true)
            //  mChannel.setShowBadge(true)
            mChannel.lightColor = ContextCompat.getColor(this, R.color.colorPrimary)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager?.createNotificationChannel(mChannel)
        } else {
            notificationBuilder.setVibrate(longArrayOf(100, 250))
                .setLights(ContextCompat.getColor(this, R.color.colorPrimary), 500, 5000)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
        notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    /*
     *To get a Bitmap image from the URL received
     */

    fun getBitmapfromUrl(imageUrl: String): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return null
        }

    }

    //    private Bitmap getCircleBitmap(Bitmap bitmap) {
    //        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
    //                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    //        final Canvas canvas = new Canvas(output);
    //
    //        final int color = Color.RED;
    //        final Paint paint = new Paint();
    //        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    //        final RectF rectF = new RectF(rect);
    //
    //        paint.setAntiAlias(true);
    //        canvas.drawARGB(0, 0, 0, 0);
    //        paint.setColor(color);
    //        canvas.drawOval(rectF, paint);
    //
    //        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    //        canvas.drawBitmap(bitmap, rect, rect, paint);
    //
    //        bitmap.recycle();
    //
    //        return output;
    //    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val color = Color.RED
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        val cx = (bitmap.width / 2).toFloat()
        val cy = (bitmap.height / 2).toFloat()
        val radius = if (cx < cy) cx else cy
        canvas.drawCircle(cx, cy, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        bitmap.recycle()
        return output
    }

    companion object {

        val NOTIFY_MESSAGE = "msg_key"
        val USER_IMAGE = "user_avatar"
        val USER_NAME = "my_key"
        val TITLE = "title"
        val BOOKING_ID = "booking_id"
        val NOTIFY_KEY = "notify_key"
        val ACCEPT_REJECT = "Accept Reject"
        val BOOKING_COMPLETED = "Booking Complete"

        private// TODO need to add other icon
        val notificationIcon: Int
            get() {
                val whiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                return if (whiteIcon) R.drawable.ic_launcher_background else R.drawable.ic_launcher_background
            }
    }


    //    private Bitmap getCircleBitmap(Bitmap bitmap) {
    //        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
    //        final Canvas canvas = new Canvas(output);
    //        final int color = Color.RED;
    //        final Paint paint = new Paint();
    //        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    //        final RectF rectF = new RectF(rect);
    //
    //        paint.setAntiAlias(true);
    //        canvas.drawARGB(0, 0, 0, 0);
    //        paint.setColor(color);
    //        canvas.drawOval(rectF, paint);
    //
    //        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    //        canvas.drawBitmap(bitmap, rect, rect, paint);
    //
    //        bitmap.recycle();
    //
    //        return output;
    //    }


    fun sendBroadCastUpdateList(notiData: NotificationData)
    {
        val intent = Intent("NOTIFICATION_BROADCAST")
        intent.putExtra(NOTIFICATION_DATA,notiData)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

  }
