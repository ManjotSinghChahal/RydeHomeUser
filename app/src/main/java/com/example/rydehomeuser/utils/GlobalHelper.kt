package com.example.rydehomeuser.utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import androidx.drawerlayout.widget.DrawerLayout
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.beust.klaxon.*
import com.example.rydehomeuser.R
import com.example.rydehomeuser.R.mipmap.*
import com.example.rydehomeuser.ui.activities.home.Home
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.JointType.ROUND
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Tasks.await
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_account_settings.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.async
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.net.URL
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.*
import java.util.concurrent.TimeoutException

object GlobalHelper {

    val euro by lazy { "\u20ac" }
    val pound by lazy { "\u00a3" }

          //    val BASE_URL = "http://192.168.1.107/ryde_home/Apis/"
             val BASE_URL = "http://202.164.42.226/dev/ryde_home/Apis/"
             val CHAT_SERVER_URL = "http://202.164.42.227:5202/"


    var auth_key = ""

    var CURRENT_LAT = 0.0
    var CURRENT_LNG = 0.0
    var PREV_LAT = 0.0
    var PREV_LNG = 0.0
    var TOTAL_DISTANCE = 0.0
    var TOTAL_DURATION = 0
    var PROMO_CODE = ""
    var REWARDS_POINTS = 0
    var CURRENT_PRICE_SURGE = 1.0

    private var blackPolyLine: Polyline? = null
    private var greyPolyLine: Polyline? = null
    private var NewListLatLng = ArrayList<LatLng>()

    var visibleScreen = ""

    var mProgress: ProgressDialog? = null

    fun changeTextColor(text: String, colorCode: String): String {
        return "<font color='$colorCode'>$text</font>"
    }

    @SuppressLint("WrongConstant")
    fun snackBar(view: View, message: String, duration: Int = Toast.LENGTH_SHORT) {
        val snack = Snackbar.make(view, message, duration).show()

    }

    fun showProgress(mContext: Context) {
        try {
            if (mProgress == null) {
                mProgress = ProgressDialog(mContext)
                mProgress!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            mProgress!!.show()
            mProgress!!.setContentView(R.layout.dialog_progress)
            mProgress!!.setCancelable(false)
        } catch (e: Exception) {
            e.printStackTrace()
            mProgress = null
        }

    }

    // hide the common progress which is used in all application.
    fun hideProgress() {
        try {
            if (mProgress != null) {
                mProgress!!.hide()
                mProgress!!.dismiss()
                mProgress = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mProgress = null
        }

    }

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

    }





    fun setToolbar(
        value: String,
        homeMenuIconVis: Boolean = false,
        homeBackIconVis: Boolean = false,
        relToggleVis: Boolean = false,
        homeCrossIconVis: Boolean = false,
        drawerSwipeable: Boolean = false,
        relHomeDone: Boolean = false,
        relHomeInfo: Boolean = false
    ) {
        Home.titleHome.text = value
        if (homeMenuIconVis) Home.homeMenuIcon.visibility = View.VISIBLE else Home.homeMenuIcon.visibility = View.GONE
        if (homeBackIconVis) Home.homeBackIcon.visibility = View.VISIBLE else Home.homeBackIcon.visibility = View.GONE
        if (relToggleVis) Home.relToggle.visibility = View.VISIBLE else Home.relToggle.visibility = View.GONE
        if (homeCrossIconVis) Home.homeCrossIcon.visibility = View.VISIBLE else Home.homeCrossIcon.visibility =
            View.GONE
        if (relHomeDone) Home.relDoneHome.visibility = View.VISIBLE else Home.relDoneHome.visibility = View.GONE
        if (relToggleVis) Home.relToggle.visibility = View.VISIBLE else Home.relToggle.visibility = View.GONE

        if (drawerSwipeable) Home.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED) else Home.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (relHomeInfo)  Home.relInfoHome.visibility = View.VISIBLE  else  Home.relInfoHome.visibility = View.GONE

    }

    /*@Throws(Exception::class)*/
    fun clearAllNotification(context : Context)
    {
        try {
            val notifManager : NotificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notifManager.cancelAll()
        }catch (ex : Exception){}


        /* throw Exception()*/
    }




    fun changeBackground(layout: RelativeLayout, color: Int) {
        val gd = GradientDrawable()
        gd.shape = GradientDrawable.RECTANGLE
        gd.setColor(Color.TRANSPARENT)
        gd.setStroke(4, color)
        gd.cornerRadius = 80.0f // border corner radius
        layout.rel_toggle.setBackground(gd)

    }

    fun uptoOneDecimal(value : String) : String
    {
        try {
            return   "%.1f".format(value.toDouble())
        }catch (ex : Exception){

        }

        return  ""
    }


    fun uptoTwoDecimal(value : String) : String
    {
        try {
           return   "%.2f".format(value.toDouble())
        }catch (ex : Exception){

        }

       return  ""
    }

    fun replaceFragment(context: Context, frag: Fragment, res: Int) {
        val transaction = (context as AppCompatActivity)?.supportFragmentManager!!.beginTransaction()
        /*.setCustomAnimations(
            R.anim.slide_in_left,
            R.anim.slide_in_right,
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )*/
        transaction.replace(res, frag)
        transaction.addToBackStack(null)
        transaction.commit()

    }


    fun replaceFramentAnim(context: Context, frag: Fragment, res: Int) {
        val transaction = (context as AppCompatActivity)?.supportFragmentManager!!.beginTransaction()
        /* .setCustomAnimations(R.anim.slide_in_top,R.anim.stay_still)*/
        /* .setCustomAnimations(
             R.anim.abc_slide_in_bottom,
             R.anim.abc_slide_out_bottom,
             R.anim.abc_slide_in_bottom,
             R.anim.abc_slide_out_bottom)*/
        transaction.replace(res, frag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getCompleteAddressFromLatLng(lat: Double, lng: Double, context: Context): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")

                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w("Current_loction_address", strReturnedAddress.toString())
            } else {
                Log.w("Current_loction_address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w("Current_loction_address", "Canont get Address!")
        }

        return strAdd
    }

    fun getLatLngFromAddress(context: Context, strAddress: String): LatLng? {

        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }

            val location = address[0]
            p1 = LatLng(location.getLatitude(), location.getLongitude())

        } catch (ex: IOException) {
         //  Log.e("exception:",ex.message)
            ex.printStackTrace()
        }

        return p1
    }


    fun drawPath(sour_lat: Double, sour_lng: Double, dest_lat: Double, dest_lng: Double, mMap: GoogleMap,
        context: Context
    ) {

        try {


        //---------------------------draw path----------------------------------
        // declare bounds object to fit whole route in screen
        val LatLongB = LatLngBounds.Builder()

        // Add markers
        val current = LatLng(sour_lat, sour_lng)
        val destination = LatLng(dest_lat, dest_lng)


        // Declare polyline object and set up color and width
        val options = PolylineOptions()
        val colorVal: Int = Color.parseColor("#3690E9")
        options.color(colorVal)
        options.width(15f)


        val url = getURL(LatLng(sour_lat, sour_lng), LatLng(dest_lat, dest_lng))

        async {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            uiThread {
                // When API call is done, create parser and convert into JsonObjec
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")

                if (routes!!["legs"]["steps"].size>0)
                {


                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                //-----------to get distance and duration
                val getDuration = routes!!["legs"]["duration"][0] as JsonObject
                val getDistance = routes!!["legs"]["distance"][0] as JsonObject
                val duration = getDuration.get("text").toString()
                val distance = getDistance.get("text").toString()

                    Log.e("printNowGH",distance)
                    Log.e("printDis1",duration)
                    //-----------to get total distance-----------------------------------
                    try {
                        if (distance.contains(","))
                        {
                            var amtDemo = distance.replace(",","")
                            var amt = amtDemo.replace("km","")
                            TOTAL_DISTANCE = amt.toDouble()
                        }
                        else if (distance.contains("km"))
                        {
                            var amt = distance.replace("km","")
                            TOTAL_DISTANCE = amt.toDouble()
                        }

                    }catch (ex : Exception){

                    }

                    //--------------------to get duration---------------------------
                    try {

                        TOTAL_DURATION =0

                        if (duration.contains("day") || duration.contains("days"))
                        {
                            val minArray =   duration.split(" ")

                            if (minArray.size==6)
                            {
                                TOTAL_DURATION = minArray.get(0).toInt()*24*60 + minArray.get(2).toInt()*60 + minArray.get(4).toInt()
                            }
                            else if (minArray.size==4)
                            {
                                if (duration.contains("hour") || duration.contains("hours"))
                                 TOTAL_DURATION = minArray.get(0).toInt()*24*60 + minArray.get(2).toInt()*60
                                else if (duration.contains("mins") )
                                    TOTAL_DURATION = minArray.get(0).toInt()*24*60 + minArray.get(2).toInt()
                            }
                            else if (minArray.size==2)
                            {
                                TOTAL_DURATION = minArray.get(0).toInt()*24*60
                            }


                        }
                        else  if (duration.contains("hour") || duration.contains("hours"))
                        {
                            val minArray =   duration.split(" ")
                            if (minArray.size==4)
                            {
                                TOTAL_DURATION = minArray.get(0).toInt()*60 + minArray.get(2).toInt()
                            }
                            else if (minArray.size==2)
                            {
                                TOTAL_DURATION = minArray.get(0).toInt()*60
                            }

                        }
                       else  if (duration.contains("mins"))
                        {
                         val minArray =   duration.split(" ")
                            if (minArray.size==2)
                                TOTAL_DURATION = minArray.get(0).toInt()
                        }

                    }catch (ex : Exception){}



                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!) }
                // Add  points to polyline and bounds

                var newLatLng: ArrayList<LatLng> = ArrayList()


                options.add(current)
                LatLongB.include(current)
                for (point in polypts) {
                    options.add(point)
                    LatLongB.include(point)
                    newLatLng!!.add(point)

                }
                    Log.e("printList",newLatLng.toString())
                NewListLatLng = newLatLng!!
                options.add(destination)
                LatLongB.include(destination)
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                //  mMap!!.addPolyline(options)
                // show map with route centered
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                addMarkerSource(mMap, LatLng(sour_lat, sour_lng), context, duration)
                addMarkerDest(mMap, LatLng(dest_lat, dest_lng), context, duration)



                options.width(15f)
                options.color(context.resources.getColor(R.color.path_color))
                options.startCap(SquareCap())
                options.endCap(SquareCap())
                options.jointType(ROUND)
                blackPolyLine = mMap.addPolyline(options)

               /* val greyOptions = PolylineOptions()
                greyOptions.width(15f)
                // greyOptions.color(Color.GRAY)
                greyOptions.color(context.resources.getColor(R.color.very_light_gray))
                greyOptions.startCap(SquareCap())
                greyOptions.endCap(SquareCap())
                greyOptions.jointType(ROUND)
                greyPolyLine = mMap.addPolyline(greyOptions)

                animatePolyLine()*/


                 /*   val greyOptions = PolylineOptions()
                    greyOptions.width(25f)
                    // greyOptions.color(Color.GRAY)
                    greyOptions.color(context.resources.getColor(R.color.red))
                    greyOptions.startCap(SquareCap())
                    greyOptions.endCap(SquareCap())
                    greyOptions.jointType(ROUND)
                    greyOptions.add(LatLng(30.71485,76.69125), LatLng(30.72447,76.72061))
                    greyPolyLine = mMap.addPolyline(greyOptions)*/



            }
        }
        }


        }catch (ex : IndexOutOfBoundsException){}
        //-----------------------------------------------------------------------

    }

    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "&destination=" + to.latitude + "," + to.longitude
        // val sensor = "sensor=false"
        val key = "&key=AIzaSyDKpkPtlJLZC0KR-p-cvb4QXG_5JtXFL40"
        val params = "$origin&$dest&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"

    }

    // https://maps.googleapis.com/maps/api/directions/json?origin=30.7046,76.7179&destination=30.7333,76.7794&key=AIzaSyDKpkPtlJLZC0KR-p-cvb4QXG_5JtXFL40

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }


    fun getMarkerBitmapFromViewSource(latLng: LatLng, context: Context, duration: String): Bitmap {

        val customMarkerView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.map_infowindow,
            null
        )
        val sAddress = customMarkerView.findViewById(R.id.sAddress_marker) as TextView
        val sTime = customMarkerView.findViewById(R.id.sTime_infoWindow) as TextView
        sTime.text = duration
        sAddress.text = getCompleteAddressFromLatLng(latLng.latitude, latLng.longitude, context)
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight())
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.getBackground()
        if (drawable != null)
            drawable!!.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    fun getMarkerBitmapFromViewDest(latLng: LatLng, context: Context, duration: String): Bitmap {

        val customMarkerView = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.dest_view,
            null
        )

        var durCount: String = ""
        var durUnit: String = ""
        val durArray = duration.split(" ")
        if (durArray.size == 2) {
            durCount = durArray.get(0)
            if (durArray.get(1).equals("mins"))
                durUnit = "MIN"
            else if (durArray.get(1).equals("hour"))
                durUnit = "Hr"
            else if (durArray.get(1).equals("hours"))
                durUnit = "Hrs"
        } else if (durArray.size == 4) {
            durCount = durArray.get(0)
            if (durArray.get(1).equals("hour"))
                durUnit = "Hr"
            else if (durArray.get(1).equals("hours"))
                durUnit = "Hrs"
        }

        val destTime_marker = customMarkerView.findViewById(R.id.destTime_marker) as TextView
        val destTime2_marker = customMarkerView.findViewById(R.id.destTime2_marker) as TextView
        destTime_marker.text = duration
     //   destTime_marker.text = durCount
     //   destTime2_marker.text = durUnit
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight())
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.getBackground()
        if (drawable != null)
            drawable!!.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    fun addMarkerSource(mMap: GoogleMap, latLng: LatLng, context: Context, duration: String) {
        mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("")
                .snippet("")
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromViewSource(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            ), context, duration
                        )
                    )
                )
        )

    }

    fun addMarkerDest(mMap: GoogleMap, latLng: LatLng, context: Context, duration: String) {
        mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latLng.latitude, latLng.longitude))
                .title("")
                .snippet("")
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        getMarkerBitmapFromViewDest(
                            LatLng(
                                latLng.latitude,
                                latLng.longitude
                            ), context, duration
                        )
                    )
                )
        )

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addOverlay(place: LatLng, context: Context, mMap: GoogleMap) {

        val groundOverlay = mMap.addGroundOverlay(
            GroundOverlayOptions()
                .position(place, 100f)
                .transparency(0.5f)
                .zIndex(3f)
                .image(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(context.getDrawable(R.drawable.map_overlay))))
        )

        startOverlayAnimation(groundOverlay)
    }

    private fun startOverlayAnimation(groundOverlay: GroundOverlay) {

        val animatorSet = AnimatorSet()

        val vAnimator = ValueAnimator.ofInt(0, 100)
        vAnimator.repeatCount = ValueAnimator.INFINITE
        vAnimator.repeatMode = ValueAnimator.RESTART
        vAnimator.interpolator = LinearInterpolator()
        vAnimator.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            groundOverlay.setDimensions(`val`.toFloat())
        }

        val tAnimator = ValueAnimator.ofFloat(0.toFloat(), 1.toFloat())
        tAnimator.repeatCount = ValueAnimator.INFINITE
        tAnimator.repeatMode = ValueAnimator.RESTART
        tAnimator.interpolator = LinearInterpolator()
        tAnimator.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Float
            groundOverlay.transparency = `val`
        }

        animatorSet.duration = 3000
        animatorSet.playTogether(vAnimator, tAnimator)
        animatorSet.start()
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap: Bitmap? = null

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun getAutocompletes(
        constraint: CharSequence,
        mGeoDataClient: GeoDataClient?,   filter : AutocompleteFilter
    ): ArrayList<AutocompletePrediction>? {
        Log.i("Print", "Starting autocomplete query for:" + constraint)

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.

        // LatLng(30.7046, 76.7179)
        val bounds = LatLngBounds(LatLng(CURRENT_LAT, CURRENT_LNG), LatLng(CURRENT_LAT, CURRENT_LNG))
        val results = mGeoDataClient!!.getAutocompletePredictions(
            constraint.toString(), bounds,
            null
        )

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            await(results, 60, SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }
        try {
            val autocompletePredictions = results.getResult()


            /* Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                     + " predictions.")*/

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose<AutocompletePrediction, AutocompletePrediction>(
                autocompletePredictions
            )
        } catch (e: RuntimeExecutionException) {
            // If the query did not complete successfully return null
            /*Toast.makeText(this, "Error contacting API: " + e.toString(),
                Toast.LENGTH_SHORT).show()*/
            Log.e("Exception",  e.message)
            return null
        }

    }

    private fun animatePolyLine() {

        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animator ->
            val latLngList = blackPolyLine!!.getPoints()
            val initialPointSize = latLngList.size
            val animatedValue = animator.animatedValue as Int
            val newPoints = animatedValue * NewListLatLng.size / 100

            if (initialPointSize < newPoints) {
                latLngList.addAll(NewListLatLng.subList(initialPointSize, newPoints))
                blackPolyLine!!.setPoints(latLngList)
            }
        }

        animator.addListener(polyLineAnimationListener)
        animator.start()

    }

    internal var polyLineAnimationListener: Animator.AnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animator: Animator) {

            // addMarker(listLatLng.get(latLngList.size - 1))
        }

        override fun onAnimationEnd(animator: Animator) {

            val blackLatLng = blackPolyLine!!.getPoints()
            val greyLatLng = greyPolyLine!!.getPoints()

            greyLatLng.clear()
            greyLatLng.addAll(blackLatLng)
            blackLatLng.clear()

            blackPolyLine!!.setPoints(blackLatLng)
            greyPolyLine!!.setPoints(greyLatLng)

            blackPolyLine!!.setZIndex(2f)

            animator.start()
        }

        override fun onAnimationCancel(animator: Animator) {

        }

        override fun onAnimationRepeat(animator: Animator) {


        }
    }


    fun countDownTimer()
    {
        val timer = object: CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
               // whereToGo.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
            }
        }
        timer.start()
    }

    fun getTimePicker(textView: TextView, context: Context) {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textView.text = SimpleDateFormat("HH:mm").format(cal.time)
            textView.textColor = ContextCompat.getColor(context,R.color.colorBlack)
        }

        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

      /*  textView.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }*/
    }

    fun getDatePicker(textview: TextView, context: Context) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            textview.text = "${dayOfMonth}-${monthOfYear}-${year}"
            textview.textColor = context.resources.getColor(R.color.colorBlack)
        }, year, month, day)

        dpd.show()
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000

    }

    fun getDateTimeFromTimestamp(timestamp: String): String {
        val stamp = Timestamp(timestamp.toLong() * 1000)
        val date = Date(stamp.getTime())

        val formateDate: String = SimpleDateFormat("dd/MM/yyyy").format(date)
        val formateTime: String = SimpleDateFormat("HH:mm").format(date)

        return "${formateDate} at ${formateTime}"
    }

    fun formatDate(date: String) : String {

        val fromUser: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val myFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {

            val reformattedStr: String = myFormat.format(fromUser.parse(date))
            return reformattedStr
        } catch (ex: Exception) { }

        return ""

    }



/*
    private fun startForeground(context : Context) {

        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context)
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context : Context): String{
        val channelId = "my_service"
        val channelName = "My Background Service"
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_HIGH)
        chan.lightColor = Color.BLUE
        chan.importance = NotificationManager.IMPORTANCE_NONE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
*/








/*        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun startRevealAnimation(rootView : View) {

        val cx = rootView.getMeasuredWidth() / 2
        val cy = rootView.getMeasuredHeight() / 2

          val anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 50f, rootView.getWidth().toFloat())

        anim.duration = 500
        anim.interpolator = AccelerateInterpolator(2f)
        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)


            }
        })

        anim.start()
    }*/


     fun getDistanceBetweenLatLng(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60.0 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    fun getRoadDistance(sour_lat: Double, sour_lng: Double, dest_lat: Double, dest_lng: Double) {

        try {


            //---------------------------draw path----------------------------------
            // declare bounds object to fit whole route in screen
            val LatLongB = LatLngBounds.Builder()

            // Add markers
            val current = LatLng(sour_lat, sour_lng)
            val destination = LatLng(dest_lat, dest_lng)


            // Declare polyline object and set up color and width
            val options = PolylineOptions()
            val colorVal: Int = Color.parseColor("#3690E9")
            options.color(colorVal)
            options.width(15f)


            val url = getURL(LatLng(sour_lat, sour_lng), LatLng(dest_lat, dest_lng))

            async {
                // Connect to URL, download content and convert into string asynchronously
                val result = URL(url).readText()
                uiThread {
                    // When API call is done, create parser and convert into JsonObjec
                    val parser: Parser = Parser()
                    val stringBuilder: StringBuilder = StringBuilder(result)
                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                    // get to the correct element in JsonObject
                    val routes = json.array<JsonObject>("routes")

                    if (routes!!["legs"]["steps"].size>0)
                    {


                        val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                        //-----------to get distance and duration
                        val getDuration = routes!!["legs"]["duration"][0] as JsonObject
                        val getDistance = routes!!["legs"]["distance"][0] as JsonObject
                        val duration = getDuration.get("text").toString()
                        val distance = getDistance.get("text").toString()

                        Log.e("printNowGH",distance)

                        //-----------to get total distance-----------------------------------
                        try {
                            if (distance.contains(","))
                            {
                                var amtDemo = distance.replace(",","")
                                var amt = amtDemo.replace("km","")
                                TOTAL_DISTANCE = amt.toDouble()
                            }
                            else if (distance.contains("km"))
                            {
                                var amt = distance.replace("km","")
                                TOTAL_DISTANCE = amt.toDouble()
                            }

                        }catch (ex : Exception){

                        }




                    }
                }
            }


        }catch (ex : IndexOutOfBoundsException){}
        //-----------------------------------------------------------------------

    }



    }



