package com.example.rydehomeuser.ui.activities.home.fragment.homeFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.carRequests.CarRequests
import com.example.rydehomeuser.data.model.driverList.DriverList
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.data.saveData.notificationData.NotificationData
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.mapActivity.MapActivity
import com.example.rydehomeuser.ui.baseclasses.App
import com.example.rydehomeuser.utils.*
import com.example.rydehomeuser.utils.Constants.HOMEFRAGMENT_SCREEN
import com.example.rydehomeuser.utils.Constants.NOTIFICATION_DATA
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.GsonBuilder
import com.google.maps.android.heatmaps.HeatmapTileProvider
import io.socket.client.Socket
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.ride_info_bottomsheet.*
import kotlinx.android.synthetic.main.ride_info_bottomsheet.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.ArrayList


class HomeFragment : androidx.fragment.app.Fragment(), OnMapReadyCallback, HomeContract.HomeFragmentView, SocketManager.Observer {



    private var mMap: GoogleMap? = null
    var mapView : View? = null
    var presenter: HomeContract.HomePresenter? = null

    var mSocket: Socket? = null
    var mSocketManager: SocketManager? = null
    private val REQUEST_PERMISSION_LOCATION = 10
    var arrayListMark : ArrayList<HashMap<String,LatLng>> = ArrayList()
    val array : ArrayList<Marker> = ArrayList()
    var addCarOnMap : Boolean = true
    val markerAnimationHelper : MarkerAnimationHelper = MarkerAnimationHelper()
    var CURRENT_LAT : String = "0.0"
    var CURRENT_LNG : String = "0.0"
    var mainHandler = Handler(Looper.getMainLooper())
    var linBottom_rideStatus: LinearLayout? = null


    var arrayListOfDriverListing : ArrayList<DriverList> = arrayListOf()
    var arrayListOfHeatMap : ArrayList<LatLng> = arrayListOf()
    private var mProvider: HeatmapTileProvider? = null
    private var mOverlay: TileOverlay? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        initialize(view)
        bottomSheetRideStatus(view)


        return view
    }


    fun initialize(view : View)
    {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment!!.getView()

        arrayListOfDriverListing.clear()

        GlobalHelper.setToolbar(getString(R.string.home), homeMenuIconVis = true, drawerSwipeable = true)

        presenter = HomePresenterImp(this)
      //  presenter!!.getUserRideStatus(HOMEFRAGMENT_SCREEN)

        Handler().postDelayed({
            if (GlobalHelper.CURRENT_LAT!=0.0 && GlobalHelper.CURRENT_LNG!=0.0)
            presenter!!.heatMap(GlobalHelper.CURRENT_LAT.toString(),GlobalHelper.CURRENT_LNG.toString(),HOMEFRAGMENT_SCREEN)
        },2000)

        val app = activity?.application as App
        mSocket = app.getSocket()
        mSocketManager = App.instance!!.getSocketManager()

        //-------------broadcast to update latlng----------------------
        val filterLoc = IntentFilter()
        filterLoc.addAction("service.to.activity.transfer")


        LocalBroadcastManager.getInstance(activity as AppCompatActivity).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val latitude = intent.getStringExtra("lat")
                    val longitude = intent.getStringExtra("lng")

                    if (latitude != null && longitude != null) {
                        CURRENT_LAT = latitude
                        CURRENT_LNG = longitude


                      /*  if (!hitBroadcastOnce) {
                            hitBroadcastOnce = true*/

                           // getDriverListing(latitude, longitude)
                     //   }

                    }
                }
            }, IntentFilter("ACTION_LOCATION_BROADCAST")
        )


        getDriverListing()



        view.rel_enterLoc_home.setOnClickListener {
            startActivity(Intent(activity, MapActivity::class.java))
        }

    }

    fun bottomSheetRideStatus(view : View)
    {
        linBottom_rideStatus = view.findViewById<View>(R.id.bottom_sheet__rideStatus) as LinearLayout
    }



    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {

        this.mMap = googleMap

        if (GlobalHelper.CURRENT_LAT!=0.0 && GlobalHelper.CURRENT_LNG!=0.0) {
            val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
           /* mMap?.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("")
            )*/
            mMap!!.isMyLocationEnabled = true
            val locationButton= (mapView!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
            val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.setMargins(0,0,30,30)

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
        }
        else
        {
            Handler().postDelayed(
                {
                    if (GlobalHelper.CURRENT_LAT!=0.0 && GlobalHelper.CURRENT_LNG!=0.0) {
                        val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
                       /* mMap?.addMarker(
                            MarkerOptions()
                                .position(position)
                                .title("")
                        )*/
                        mMap!!.isMyLocationEnabled = true
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
                    }
                },
                3000 // value in milliseconds
            )
        }

//        mMap!!.isMyLocationEnabled = true
//
//        //----------------change location enabled button-----------------
//        val locationButton= (mapView!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
//        val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
//        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
//        rlp.setMargins(0,70,40,0)

    }

    override fun onUserRideStatusSuccess(userRideStatus: UserRideStatus) {

        try {
            if (userRideStatus.code.equals(200))
            {
             if (userRideStatus.body.size>0 && !userRideStatus.body.get(0).status.equals("pending"))
               {
                 startActivity(Intent(activity, MapActivity::class.java))
               }
             else if (userRideStatus.body.size>0 && userRideStatus.body.get(0).status.equals("pending"))
               {
                   if (userRideStatus!!.body.size>0)
                     {
                         linBottom_rideStatus!!.visibility = View.VISIBLE
                         setRideStatus(userRideStatus!!)
                     }
               }
            }
        }catch (ex: Exception){}
    }

    override fun onHeatMApSuccess(carRequests: CarRequests) {
        try {
            if (carRequests.code.equals(200))
            {
                arrayListOfHeatMap.clear()

                for (i in 0..carRequests.body.size-1)
                {
                    arrayListOfHeatMap.add(LatLng(carRequests.body.get(i).from_lat.toDouble(),carRequests.body.get(i).from_long.toDouble()))
                }

                addHeatMap(arrayListOfHeatMap)

            }
        }catch (ex: Exception){}    }

    override fun onFailure(error: String) {

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
                    activity as AppCompatActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                activity?.startService(Intent(activity as AppCompatActivity, LocationService::class.java))

            } else {
                Toast.makeText(activity as AppCompatActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun getDriverListing() {

        mainHandler.post(object : Runnable {
            override fun run() {


                if (!CURRENT_LAT.equals("0.0") && !CURRENT_LNG.equals("0.0"))
                {
                    val jsonObject = JSONObject()
                    jsonObject.put("user_id", SharedPrefUtil.getInstance()?.getUserId())
                    jsonObject.put("latitude", CURRENT_LAT)
                    jsonObject.put("longitude", CURRENT_LNG)
                    mSocketManager!!.onGetDriverListing(jsonObject)
                }
                mainHandler.postDelayed(this, 3000)
            }
        })


    }



    override fun onDriverList(event: String, args: JSONArray) {

        Log.e("rcrdcx","1233")
        activity?.runOnUiThread {

            if (event.equals(mSocketManager!!.GET_LIST)) {

                try {
                    val fileData = args.toString()
                    Log.e("rcrdcx",fileData.toString())
                    val gson = GsonBuilder().create()
                    val getDriverList = gson.fromJson(fileData, Array<DriverList>::class.java).toList()


                    //-----to show heatmap---------------------------------------
                    /*try {
                        if (arrayListOfDriverListing!=null && arrayListOfDriverListing.size==0)
                        {
                            arrayListOfDriverListing.addAll(getDriverList)
                            arrayHeatMap(arrayListOfDriverListing)
                        }
                    }catch (ex : Exception){}*/



                    // mMap!!.clear()
                    for (i in 0..getDriverList.size - 1) {
                        val latlng =
                            LatLng(getDriverList.get(i).latitude.toDouble(), getDriverList.get(i).longitude.toDouble())

                        val hashMap : HashMap<String,LatLng> = HashMap()
                        hashMap.put("latLng",latlng)
                        arrayListMark.add(hashMap)

                        showOrAnimateMarker(LatLng(getDriverList.get(i).latitude.toDouble(),getDriverList.get(i).longitude.toDouble()),getDriverList.get(i).email,i)

                        Handler().postDelayed({
                            addCarOnMap = false
                        },2000)

                    }
                } catch (ex: Exception) {
                }

            } else {

            }
        }

    }


    fun showOrAnimateMarker(latLng: LatLng,id : String,pos : Int)
    {
            //-----------adding marker on map-------
            if (addCarOnMap)
            {
                var  marker = mMap!!.addMarker(getDriverMarkerOptions(latLng))
                marker.tag = id

                array.add(marker)

            }
            //---------animating marker on map-----------
            else
            {
                for (p in 0..array.size-1)
                {
                    if (id.equals(array.get(p).tag))
                    {
                        markerAnimationHelper.animateMarkerToGB(array.get(p)!!, latLng, LatLngInterpolator.Spherical())
                    }
                }
            }


    }

    fun getDriverMarkerOptions(position: LatLng): MarkerOptions {

        val options = getMarkerOptions(R.drawable.car_icon1, position)
        options.flat(true)
        return options
    }

    private fun getMarkerOptions(resource: Int, position: LatLng): MarkerOptions {

        val  b : Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_icon1)
        val smallMarker : Bitmap = Bitmap.createScaledBitmap(b, 50, 100, false)

        return MarkerOptions()
           // .icon(BitmapDescriptorFactory.fromResource(resource))
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            .position(position)
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissionForLocation(activity as AppCompatActivity)) {
            activity?.startService(Intent(activity as AppCompatActivity, LocationService::class.java))
        }
        mSocketManager?.onRegister(this)

        presenter!!.getUserRideStatus(HOMEFRAGMENT_SCREEN)
    }

    override fun onPause() {
        super.onPause()

        try {
            activity?.stopService(Intent(activity as AppCompatActivity, LocationService::class.java))
            mSocketManager!!.disconnectGetListing()
            mSocketManager!!.unRegister(this)
            mainHandler.removeCallbacksAndMessages(null)
        }catch (ex : Exception){}

    }


    fun arrayHeatMap(arrayListOfDriverListing: ArrayList<DriverList>)
    {

        arrayListOfHeatMap.clear()
        for(i in 0..arrayListOfDriverListing.size-1)
        {
            var count : Int = 0
            for (j in 0..arrayListOfDriverListing.size-1)
            {
                if ((GlobalHelper.getDistanceBetweenLatLng(arrayListOfDriverListing.get(i).latitude.toDouble(),
                        arrayListOfDriverListing.get(i).longitude.toDouble(),
                        arrayListOfDriverListing.get(j).latitude.toDouble(),
                        arrayListOfDriverListing.get(j).longitude.toDouble()))<1)
                {
                    count++
                    if (count==5)
                    {
                        arrayListOfHeatMap.add(LatLng(arrayListOfDriverListing.get(i).latitude.toDouble(),
                            arrayListOfDriverListing.get(i).longitude.toDouble()))
                    }

                }
            }
        }

        addHeatMap(arrayListOfHeatMap)
    }

    private fun addHeatMap(list: ArrayList<LatLng>) {

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = HeatmapTileProvider.Builder()
            .data(list)
          //  .radius(50)
            .build()
        mProvider!!.setRadius(70)
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap!!.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }

    fun setRideStatus(userRideStatusModel: UserRideStatus)
    {
        linBottom_rideStatus!!.sourceAdd_rideStatus.text = userRideStatusModel.body.get(0).from_address
        linBottom_rideStatus!!.destAdd_rideStatus.text = userRideStatusModel.body.get(0).to_address
    }





}
