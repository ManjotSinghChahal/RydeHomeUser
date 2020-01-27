package com.example.rydehomeuser.ui.activities.mapActivity

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.*
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.*
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.carRequests.CarRequests
import com.example.rydehomeuser.data.model.confirmPickup.ConfirmPickup
import com.example.rydehomeuser.data.model.driverList.DriverList
import com.example.rydehomeuser.data.model.getCarTypes.GetCarTypes
import com.example.rydehomeuser.data.model.logout.Logout
import com.example.rydehomeuser.data.model.pickupNotes.PickupNotes
import com.example.rydehomeuser.data.model.pickup_change.PickupChange
import com.example.rydehomeuser.data.model.requestCab.RequestCab
import com.example.rydehomeuser.data.model.requestCabSchedule.RequestCabSchedule
import com.example.rydehomeuser.data.model.sos.Sos
import com.example.rydehomeuser.data.saveData.locationData.LocationData
import com.example.rydehomeuser.data.saveData.notificationData.NotificationData
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.bottomSheet.SchedulePickUp
import com.example.rydehomeuser.ui.dialogFragment.BookingCancel
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.NOTIFICATION_DATA
import com.example.rydehomeuser.utils.GlobalHelper.visibleScreen
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.confirm_pickup_bottomsheet.*
import kotlinx.android.synthetic.main.connect_nearbydrivers_bottomsheet.*
import kotlinx.android.synthetic.main.driver_details_bottomsheet.*
import kotlinx.android.synthetic.main.stop_info_bottomsheet.*
import kotlinx.android.synthetic.main.map_activity.*
import kotlinx.android.synthetic.main.request_cab_bottomsheet.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder

import com.example.rydehomeuser.data.model.splitRequest.SplitRequest
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.ui.activities.contactListActivity.ContactListActivity
import com.example.rydehomeuser.ui.activities.getCards.GetCards
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.fragment.paymentPoints.PaymentPoints
import com.example.rydehomeuser.ui.activities.searchPlacesActivity.SearchPlacesActivity
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.baseclasses.App
import com.example.rydehomeuser.ui.dialogFragment.PhoneDialog
import com.example.rydehomeuser.ui.dialogFragment.Rating
import com.example.rydehomeuser.ui.dialogFragment.SplitFareDialog
import com.example.rydehomeuser.utils.*
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTING
import com.example.rydehomeuser.utils.Constants.AMOUNT
import com.example.rydehomeuser.utils.Constants.CHANGE_DESTINATION_TYPE
import com.example.rydehomeuser.utils.Constants.FRIEND_IDS
import com.example.rydehomeuser.utils.Constants.PLACE_NAME
import com.example.rydehomeuser.utils.Constants.REQUEST_TYPE
import com.example.rydehomeuser.utils.Constants.RIDE_ID
import com.example.rydehomeuser.utils.GlobalHelper.TOTAL_DISTANCE
import com.example.rydehomeuser.utils.GlobalHelper.TOTAL_DURATION
import com.example.rydehomeuser.utils.GlobalHelper.pound
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.gson.GsonBuilder
import com.google.maps.android.heatmaps.HeatmapTileProvider
import io.socket.client.Socket
import kotlinx.android.synthetic.main.confirm_pickup_bottomsheet.view.*
import kotlinx.android.synthetic.main.connect_nearbydrivers_bottomsheet.lin_connectingNearbyDrivers
import kotlinx.android.synthetic.main.driver_details_bottomsheet.view.*
import kotlinx.android.synthetic.main.request_cab_bottomsheet.view.*
import kotlinx.android.synthetic.main.ride_info_bottomsheet.*
import kotlinx.android.synthetic.main.ride_info_bottomsheet.view.*
import kotlinx.android.synthetic.main.stop_info_bottomsheet.view.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import org.json.JSONArray
import java.util.*
import kotlin.collections.HashMap


class MapActivity : AppCompatActivity(), OnMapReadyCallback, HomeContract.MapView, ChooseCabAdapter.SelCarType,
    SplitFareDialog.SplitFareInterface, SchedulePickUp.ScheduleCabInterface, SocketManager.Observer {



    private val REQUEST_PERMISSION_LOCATION = 10

    lateinit var bottomSheetConnectingNearbyDrivers: BottomSheetBehavior<LinearLayout>

    private var mMap: GoogleMap? = null
    var recyclerview_requestCab: RecyclerView? = null

    var presenter: HomeContract.HomePresenter? = null
    var locData: LocationData = LocationData()


    lateinit var placesAdapter: PlacesAdapter
    lateinit var placesAdapter1: PlacesAdapter
    lateinit var placesAdapter2: PlacesAdapter
    lateinit var mGeoDataClient: GeoDataClient
    val BOUNDS_INDIA = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))

    var linBottom_driverDetails: LinearLayout? = null
    var linBottom_requestCab: LinearLayout? = null
    var linBottom_stop: LinearLayout? = null
    var linBottom_confirmPickup: LinearLayout? = null
    var linBottom_rideStatus: LinearLayout? = null

    var bottomSheetConfirmPickup: BottomSheetBehavior<LinearLayout>? = null

    var userRideStatusModel: UserRideStatus? = null

    var mSocket: Socket? = null
    var mSocketManager: SocketManager? = null

    val markerAnimationHelper: MarkerAnimationHelper = MarkerAnimationHelper()


    val CONTACT_TYPE: Int = 3
    val CARD_TYPE: Int = 4

    var arrayListMark: ArrayList<HashMap<String, LatLng>> = ArrayList()
    val arrayOfCarMarkers: ArrayList<Marker> = ArrayList()
    var arrayOfDriverId: ArrayList<String> = ArrayList()
    var addCarOnMap: Boolean = true
    var showOnlyCurrentDriver: Boolean = false
    var showAllDriver: Boolean = false
    var currentDriverId: String = ""
    val CHANGE_ADDRESS: Int = 6
    val CHANGE_CONFIRM_ADDRESS: Int = 7

    private var mProvider: HeatmapTileProvider? = null
    private var mOverlay: TileOverlay? = null

    private var snapUrl: URL? = null
    private val SNAP_TO_ROAD_API = "https://roads.googleapis.com/v1/snapToRoads?path="
    var snapLatPass: Double = 0.0
    var snapLngPass: Double = 0.0
    var snapLatGet: Double = 0.0
    var snapLngGet: Double = 0.0

    val SOURCE_ADDRESS: Int = 11
    val DESTINATION_ADDRESS: Int = 12
    val STOP1_ADDRESS: Int = 13
    var CURRENT_LAT: String = "0.0"
    var CURRENT_LNG: String = "0.0"
    val mainHandler = Handler(Looper.getMainLooper())
    var arrayListOfDriverListing: ArrayList<DriverList> = arrayListOf()
    var arrayListOfHeatMap: ArrayList<LatLng> = arrayListOf()
    //   val arrayLatLng : ArrayList<LatLng> = arrayListOf()
    //  var count : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)



        bottomSheetStop()
        bottomSheetRideStatus()
        bottomSheetConfirmPickup()
        bottomSheetRequestCab()
        bottomSheetDriverDetails()

        initialize()
        clickListener()









        /* arrayLatLng.add(LatLng(30.70917,76.69568))
         arrayLatLng.add(LatLng(30.70948,76.69575))
         arrayLatLng.add(LatLng(30.70948,76.69575))
         arrayLatLng.add(LatLng(30.70959,76.69567))
         arrayLatLng.add(LatLng(30.70996,76.69537))
         arrayLatLng.add(LatLng(30.71025,76.69514))
         arrayLatLng.add(LatLng(30.71086,76.69465))
         arrayLatLng.add(LatLng(30.71099,76.69453))
         arrayLatLng.add(LatLng(30.71164,76.69396))
         arrayLatLng.add(LatLng(30.71318,76.69267))
         arrayLatLng.add(LatLng(30.71352,76.69238))
         arrayLatLng.add(LatLng(30.71386,76.69208))
         arrayLatLng.add(LatLng(30.71409,76.69189))
         arrayLatLng.add(LatLng(30.71423,76.69178))
         arrayLatLng.add(LatLng(30.71469,76.69139))
         arrayLatLng.add(LatLng(30.71485,76.69125))
         arrayLatLng.add(LatLng(30.71494,76.69117))
         arrayLatLng.add(LatLng(30.71497,76.69114))
         arrayLatLng.add(LatLng(30.71501,76.69112))
         arrayLatLng.add(LatLng(30.71521,76.69096))
         arrayLatLng.add(LatLng(30.71663,76.68971))
         arrayLatLng.add(LatLng(30.71734,76.68915))
         arrayLatLng.add(LatLng(30.71812,76.68855))
         arrayLatLng.add(LatLng(30.71813,76.68854))
         arrayLatLng.add(LatLng(30.71864,76.68817))
         arrayLatLng.add(LatLng(30.7188,76.68806))
         arrayLatLng.add(LatLng(30.71965,76.68742))
         arrayLatLng.add(LatLng(30.7197,76.68738))
         arrayLatLng.add(LatLng(30.72034,76.68686))
         arrayLatLng.add(LatLng(30.72067,76.68657))
         arrayLatLng.add(LatLng(30.72071,76.68653))
         arrayLatLng.add(LatLng(30.72184,76.68545))
         arrayLatLng.add(LatLng(30.72262,76.68464))
 */

/*

        lat/lng: (30.72299,76.68421), lat/lng: (30.72354,76.68364), lat/lng: (30.72395,76.68323),
        lat/lng: (30.72494,76.68225), lat/lng: (30.72553,76.68165), lat/lng: (30.72645,76.68076),
        lat/lng: (30.72705,76.6802), lat/lng: (30.73028,76.67704), lat/lng: (30.73039,76.67693),
        lat/lng: (30.73043,76.6769), lat/lng: (30.73058,76.67675), lat/lng: (30.7306,76.67673),
        lat/lng: (30.73085,76.67646), lat/lng: (30.73113,76.67618), lat/lng: (30.73147,76.67

*/


        // RetrieveSnapRoad().execute()


    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap


        if (GlobalHelper.CURRENT_LAT != 0.0 && GlobalHelper.CURRENT_LNG != 0.0) {
            val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
            mMap!!.isMyLocationEnabled = true
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))

        } else {
            Handler().postDelayed(
                {
                    if (GlobalHelper.CURRENT_LAT != 0.0 && GlobalHelper.CURRENT_LNG != 0.0) {
                        val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
                        mMap!!.isMyLocationEnabled = true
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))

                    }
                },
                7000 // value in milliseconds
            )
        }


    }

    fun initialize() {
/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootmapAct.addOnLayoutChangeListener( { view: View, i: Int, i1: Int, i2: Int, i3: Int, i4: Int, i5: Int, i6: Int, i7: Int ->
                startRevealAnimation(rootmapAct)
            })
        }*/


        visibleScreen = MAP_ACTIVITY
        arrayListOfDriverListing.clear()
        cvv_num = ""


        val app = application as App
        mSocket = app.getSocket()
        mSocketManager = App.instance!!.getSocketManager()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_activity) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomSheetConnectingNearbyDrivers = BottomSheetBehavior.from(bottom_sheet__connectingNearby)



        recyclerview_requestCab = findViewById(R.id.recyclerview_requestCab) as RecyclerView
        recyclerview_requestCab!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        presenter = HomePresenterImp(this)
        presenter!!.getUserRideStatus(MAP_ACTIVITY)

        if (GlobalHelper.CURRENT_LAT!=0.0 && GlobalHelper.CURRENT_LNG!=0.0)
            presenter!!.heatMap(GlobalHelper.CURRENT_LAT.toString(),GlobalHelper.CURRENT_LNG.toString(),MAP_ACTIVITY)


        //------------------------autocomplete---------------------------------
        mGeoDataClient = Places.getGeoDataClient(this, null)

        //  placesAdapter = PlacesAdapter(this, android.R.layout.simple_list_item_1, mGeoDataClient, null, BOUNDS_INDIA)
        //  edt_locationFrom_mapAct.setAdapter(placesAdapter)

        //   placesAdapter1 = PlacesAdapter(this, android.R.layout.simple_list_item_1, mGeoDataClient, null, BOUNDS_INDIA)
        //   edt_locatioTo_mapAct.setAdapter(placesAdapter1)

        //  placesAdapter2 = PlacesAdapter(this, android.R.layout.simple_list_item_1, mGeoDataClient, null, BOUNDS_INDIA)
        //  edt_locatioStop_mapAct.setAdapter(placesAdapter2)


        //-------------------bundle-----------------------------

        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(NOTIFICATION_DATA)) {
            val notifiData: NotificationData = bundle.getParcelable(NOTIFICATION_DATA)
            rel_enterLocations.visibility = View.GONE

            if (notifiData.type.equals("Pickup_confirmation"))
            // showConfirm(notifiData)
            else if (notifiData.type.equals("split_fare_request"))
                showSplitFareDialog(notifiData)
            else if (notifiData.type.equals("Pickup_rejected")) {
                moveToHome()
            }

        }
        //--------------------bundle end--------------------------


        //--------------------to show pickup dialog when request comes and user is on home page-------
        val filter = IntentFilter()
        filter.addAction("service.to.activity.transfer")


        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {

                    rel_enterLocations.visibility = View.GONE
                    val notifiData = intent.getParcelableExtra(NOTIFICATION_DATA) as NotificationData

                    if (notifiData.type.equals("Pickup_confirmation"))
                    // showConfirm(notifiData)
                    else if (notifiData.type.equals("split_fare_request"))
                        showSplitFareDialog(notifiData)
                    else if (notifiData.type.equals("Pickup_rejected")) {
                        moveToHome()
                    }

                    presenter!!.getUserRideStatus(MAP_ACTIVITY)

                }
            }, IntentFilter("NOTIFICATION_BROADCAST")
        )


        if (!GlobalHelper.CURRENT_LAT.equals(0.0) && !GlobalHelper.CURRENT_LNG.equals(0.0)) {


            Handler().postDelayed({

                edt_locationFrom_mapAct.setText(
                    GlobalHelper.getCompleteAddressFromLatLng(
                        GlobalHelper.CURRENT_LAT,
                        GlobalHelper.CURRENT_LNG,
                        this
                    )
                )
                //      edt_locatioTo_mapAct.requestFocus()

                locData.source_lat = GlobalHelper.CURRENT_LAT.toString()
                locData.source_lng = GlobalHelper.CURRENT_LNG.toString()

                locData.source_loc =
                    GlobalHelper.getCompleteAddressFromLatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG, this)


                calculatePrice(locData.source_lat,locData.source_lng)

            }, 1000)

        }


        //------------------------location update broadcast receiver------------------------

        //-------------broadcast to update latlng----------------------
        if (checkPermissionForLocation(this)) {
            startService(Intent(this, LocationService::class.java))
        }


        val filterLoc = IntentFilter()
        filterLoc.addAction("service.to.activity.transfer")


        LocalBroadcastManager.getInstance(this).registerReceiver(
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val latitude = intent.getStringExtra("lat")
                    val longitude = intent.getStringExtra("lng")

                    if (latitude != null && longitude != null) {
                        CURRENT_LAT = latitude
                        CURRENT_LNG = longitude
                    }
                }
            }, IntentFilter("ACTION_LOCATION_BROADCAST")
        )

        getDriverListing()

    }


    fun clickListener() {


        img_cross_from.setOnClickListener {
            edt_locationFrom_mapAct.setText("")
            locData.source_lat = ""
            locData.source_lng = ""
            locData.source_loc = ""
            img_cross_from.visibility = View.GONE
        }

        img_cross_addStop2.setOnClickListener {

            edt_locatioStop_mapAct.setText("")
            rel_locationstop2.visibility = View.GONE
            img_cross_addStop1.visibility = View.GONE
            img_addStop.visibility = View.VISIBLE
            view2_mapActivity.visibility = View.GONE
        }

/*        edt_locationFrom_mapAct.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt_locationFrom_mapAct.text.toString().trim().length > 0)
                    img_cross_from.visibility = View.VISIBLE
                else
                    img_cross_from.visibility = View.GONE
            }

        })*/


/*        edt_locationFrom_mapAct.setOnItemClickListener { parent, view, position, id ->

            val item = placesAdapter.getItem(position)
            val placeId = item?.getPlaceId()
            val primaryText = item?.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: " + primaryText)


            val placeResult = mGeoDataClient.getPlaceById(placeId)
            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.getResult();
                    val place = places!!.get(0)

                    locData.source_lat = place.latLng.latitude.toString()
                    locData.source_lng = place.latLng.longitude.toString()




                    places.release()

                    GlobalHelper.hideSoftKeyBoard(this@MapActivity, rootmapAct)
                }

            })

            locData.source_loc = primaryText.toString()
        }*/



        edt_locationFrom_mapAct.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), SOURCE_ADDRESS)
        }

        edt_locatioTo_mapAct.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), DESTINATION_ADDRESS)
        }


/*        edt_locatioTo_mapAct.setOnItemClickListener { parent, view, position, id ->

            val item = placesAdapter1.getItem(position)
            val placeId = item?.getPlaceId()
            val primaryText = item?.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: " + primaryText)


            val placeResult = mGeoDataClient.getPlaceById(placeId)
            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.getResult();
                    val place = places!!.get(0)


                    locData.dest_lat = place.latLng.latitude.toString()
                    locData.dest_lng = place.latLng.longitude.toString()


                    places.release()

                    GlobalHelper.hideSoftKeyBoard(this@MapActivity, rootmapAct)
                }

            })

            locData.dest_loc = primaryText.toString()

        }*/

        edt_locatioStop_mapAct.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), STOP1_ADDRESS)
        }

/*        edt_locatioStop_mapAct.setOnItemClickListener { parent, view, position, id ->

            val item = placesAdapter2.getItem(position)
            val placeId = item?.getPlaceId()
            val primaryText = item?.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: " + primaryText)


            val placeResult = mGeoDataClient.getPlaceById(placeId)
            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.getResult()
                    val place = places!!.get(0)

                    locData.stop_lat = place.latLng.latitude.toString()
                    locData.stop_lng = place.latLng.longitude.toString()


                    places.release()

                    GlobalHelper.hideSoftKeyBoard(this@MapActivity, rootmapAct)
                }

            })

            locData.stop_loc = primaryText.toString()
        }*/





        img_addStop.setOnClickListener {
            img_addStop.visibility = View.GONE
            img_cross_addStop1.visibility = View.GONE
            rel_locationstop2.visibility = View.VISIBLE
            view2_mapActivity.visibility = View.VISIBLE
            //  edt_locatioTo_mapAct.text = getString(R.string.add_stop)


        }

        back_arrow_mapActivity.setOnClickListener {
            onBackPressed()
        }



        img_search_confirmPickup.setOnClickListener {
            //  startActivity(Intent(this, SearchPlaces::class.java))
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), CHANGE_CONFIRM_ADDRESS)


        }




        lin_connectingNearbyDrivers.setOnClickListener {
            bottomSheetConnectingNearbyDrivers.isHideable = true
            bottomSheetConnectingNearbyDrivers.state = BottomSheetBehavior.STATE_HIDDEN

        }

        //------------redudant----------------------
        bottom_sheet_layout.setOnClickListener {

        }


    }

    companion object {

        /* var searchPlaces_lat: String = ""
         var searchPlaces_lng: String = ""
         var searchPlaces_address: String = ""*/

        var ride_id: String = ""
        var amount: String = ""
        var cvv_num: String = ""

        private val PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place"
        private val TYPE_AUTOCOMPLETE = "/autocomplete"
        private val OUT_JSON = "/json"
        private val PLACES_DETAIL_API_BASE = "https://maps.googleapis.com/maps/api/place"

        fun autocomplete(input: String): ArrayList<*>? {
            var resultList: ArrayList<Any>? = null
            Log.d("input", input)
            var conn: HttpURLConnection? = null
            val jsonResults = StringBuilder()
            try {
                val sb = StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON)
                sb.append("?key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")

                /*
         * sb.append("&components=country:in");
         */
                sb.append("&types=(cities)")
                sb.append("&input=" + URLEncoder.encode(input, "utf-8"))


                val url = URL(sb.toString())
                conn = url.openConnection() as HttpURLConnection
                val `in` = InputStreamReader(conn.inputStream)
                // Load the results into a StringBuilder
                var read: Int
                val buff = CharArray(1024)
                //  while ((read = `in`.read(buff)) != -1) {
                while (`in`.read(buff).also { read = it } != -1) {
                    jsonResults.append(buff, 0, read)
                }
            } catch (e: MalformedURLException) {
                //  Log.e(LOG_TAG, "Error processing Places API URL", e)
                return resultList
            } catch (e: IOException) {
                //   Log.e(LOG_TAG, "Error connecting to Places API", e)
                return resultList
            } finally {

                Log.d("final block", "1")
                if (conn != null) {
                    Log.d("final block", "2")
                    conn.disconnect()
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                val jsonObj = JSONObject(jsonResults.toString())
                val predsJsonArray = jsonObj.getJSONArray("predictions")

                // Extract the Place descriptions from the results
                resultList = ArrayList(predsJsonArray.length())
                for (i in 0 until predsJsonArray.length()) {
                    println(predsJsonArray.getJSONObject(i).getString("description"))
                    println("============================================================")
                    resultList!!.add(predsJsonArray.getJSONObject(i).getString("description"))

                    Log.e("result_here", resultList[i].toString() + "")
                }
            } catch (e: JSONException) {
                Log.e("printHome", "Cannot process JSON results", e)
            }

            return resultList
        }
    }


    override fun onGetCarTypesSuccess(getCarTypes: GetCarTypes) {

        try {

            if (getCarTypes.code.equals(200)) {
                linBottom_requestCab!!.visibility = View.VISIBLE
                recyclerview_requestCab!!.adapter = ChooseCabAdapter(this, getCarTypes, this)
            }
        } catch (ex: Exception) {
        }

    }

    override fun onHeatMApSuccess(carRequests: CarRequests) {

        try {

            if (carRequests.code.equals(200)) {

                arrayListOfHeatMap.clear()
                for (i in 0..carRequests.body.size-1)
                {
                    arrayListOfHeatMap.add(LatLng(carRequests.body.get(i).from_lat.toDouble(),carRequests.body.get(i).from_long.toDouble()))
                }


                addHeatMap(arrayListOfHeatMap)
            }
        } catch (ex: Exception) {
        }    }

    override fun onFailure(error: String) {
        try {

            if (error.equals("Invalid authorization_key"))
                presenter!!.logout(MAP_ACTIVITY)
            else
                GlobalHelper.snackBar(rootmapAct, error)

        } catch (ex: Exception) {
        }
    }

    override fun onRequestCabSuccess(requestCab: RequestCab) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootmapAct, requestCab.message)
            if (requestCab.code.equals(200)) {
                linBottom_requestCab!!.visibility = View.GONE
                rel_enterLocations.visibility = View.GONE
                presenter!!.getUserRideStatus(MAP_ACTIVITY)
            }

        } catch (ex: Exception) {

        }
    }

    override fun onPickupNotesSuccess(pickupNotes: PickupNotes) {
        try {
            //  GlobalHelper.snackBar(rootmapAct, pickupNotes.message)
        } catch (ex: Exception) {
        }
    }

    override fun onSelCarTypeSuccess(
        seater: String,
        vehicle_id: String,
        price: String,
        amount: String,
        distance: String
    ) {


        locData.vehilce_id = vehicle_id
        locData.est_price = price
        locData.amount = amount
        locData.distance = distance
        locData.duration = TOTAL_DURATION.toString()


        seatCapacity_requestCab.text = seater


    }

    override fun onConfirmPickupSuccess(confirmPickup: ConfirmPickup) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootmapAct, confirmPickup.message)
            linBottom_confirmPickup!!.visibility = View.GONE
            presenter!!.getUserRideStatus(MAP_ACTIVITY)


        } catch (ex: Exception) {
        }
    }

    override fun onPickupChangeSuccess(pickupChange: PickupChange) {
        try {

            presenter!!.getUserRideStatus(MAP_ACTIVITY)
        } catch (ex: Exception) {
        }
    }


    override fun onStop() {
        super.onStop()
        visibleScreen = ""
    }


    fun showConfirm(userRideStatusModel: UserRideStatus) {
        pickupLoc_ConfirmRide.text = userRideStatusModel.body.get(0).from_address
    }


    override fun onResume() {
        super.onResume()

        visibleScreen = MAP_ACTIVITY
        mSocketManager?.onRegister(this)

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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startService(Intent(this, LocationService::class.java))

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        try {
            stopService(Intent(this, LocationService::class.java))
            mSocketManager!!.disconnectGetListing()
            mSocketManager!!.unRegister(this)
            mainHandler.removeCallbacksAndMessages(null)
        } catch (ex: Exception) {
        }

    }

    override fun onRestart() {
        super.onRestart()
        visibleScreen = MAP_ACTIVITY

        presenter!!.getUserRideStatus(MAP_ACTIVITY)


        /*if (!searchPlaces_address.equals("")) {
            pickupLoc_ConfirmRide.text = searchPlaces_address
        }*/
    }


    fun bottomSheetRequestCab() {

        linBottom_requestCab = findViewById<View>(R.id.bottom_sheet__requestCab) as LinearLayout
        val bottomSheetRequestCab = BottomSheetBehavior.from(bottom_sheet__requestCab)
        bottomSheetRequestCab.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetRequestCab.setState(BottomSheetBehavior.STATE_EXPANDED)
        bottomSheetRequestCab.setState(BottomSheetBehavior.STATE_HIDDEN)
        bottomSheetRequestCab.setPeekHeight(140)
        bottomSheetRequestCab.setHideable(false)
        bottomSheetRequestCab.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
            }
        })

        linBottom_requestCab!!.rootRequestCab.setOnClickListener {

        }

        linBottom_requestCab!!.request_cab.setOnClickListener {

            locData.payment_mode = "1"
            locData.booking_date = ""
            locData.time = ""
            locData.ride_id = "0"

            if (locData.vehilce_id.isEmpty())
                GlobalHelper.snackBar(rootmapAct, getString(R.string.sel_car_type))
            else {
                GlobalHelper.showProgress(this)
                presenter!!.requestCab(REQUEST_TYPE, locData)
            }

        }

        linBottom_requestCab!!.rel_clock_requestCab.setOnClickListener {


            if (locData.vehilce_id.isEmpty())
                GlobalHelper.snackBar(rootmapAct, getString(R.string.sel_car_type))
            else {

                val schedulePickUp = SchedulePickUp(locData, this)
                schedulePickUp.show(supportFragmentManager, "")
            }


        }

    }

    fun bottomSheetDriverDetails() {
        linBottom_driverDetails = findViewById<View>(R.id.bottom_sheet__DriverDetails) as LinearLayout
        val bottomSheetDriverDetails = BottomSheetBehavior.from(bottom_sheet__DriverDetails)
        bottomSheetDriverDetails.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetDriverDetails.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
            }
        })



        linBottom_driverDetails!!.linLay_driverDetails.setOnClickListener {

        }

        linBottom_driverDetails!!.rel_selCard_driverDetails.setOnClickListener {
            val intent = Intent(this, GetCards::class.java)
            intent.putExtra(MAP_ACTIVITY, MAP_ACTIVITY)
            startActivityForResult(intent, CARD_TYPE)
        }

        linBottom_driverDetails!!.rel_changeDestination_driverDetails.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), CHANGE_ADDRESS)
        }

        linBottom_driverDetails!!.rel_splitFare_DriverDetails.setOnClickListener {

            if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {
                val intent = Intent(this, ContactListActivity::class.java)
                intent.putExtra(MAP_ACTIVITY, MAP_ACTIVITY)
                intent.putExtra(RIDE_ID, userRideStatusModel!!.body.get(0).ride_id)
                intent.putExtra(AMOUNT, userRideStatusModel!!.body.get(0).amount)
                startActivity(intent)
            }

        }

        linBottom_driverDetails!!.linShareTrip_driverDetails.setOnClickListener {
            if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {
                val intent = Intent(this, ContactListActivity::class.java)
                intent.putExtra(MAP_ACTIVITY, MAP_ACTIVITY)
                intent.putExtra(RIDE_ID, userRideStatusModel!!.body.get(0).ride_id)
                intent.putExtra(AMOUNT, userRideStatusModel!!.body.get(0).amount)
                startActivity(intent)
            }
        }

        linBottom_driverDetails!!.rel_shareTrip_driverDetails.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            intent.putExtra(ACCOUNT_SETTING, ACCOUNT_SETTING)
            startActivityForResult(intent, CONTACT_TYPE)
        }

        linBottom_driverDetails!!.next_pickupNotes_driverDetails.setOnClickListener {
            if (linBottom_driverDetails!!.pickUpNotes_driverDetails.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootmapAct, "Please enter Pick-Up notes")
            else {
                if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0)
                    presenter!!.pickupNotes(
                        userRideStatusModel!!.body.get(0).ride_id,
                        linBottom_driverDetails!!.pickUpNotes_driverDetails.text.toString().trim()
                    )

                linBottom_driverDetails!!.pickUpNotes_driverDetails.setText("")
            }
        }

        linBottom_driverDetails!!.linSOS_driverDetails.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Are you sure want to notify for Emergency?")
            //  builder.setMessage("Are you want to set the app background color to RED?")
            builder.setPositiveButton("YES") { dialog, which ->
                try {
                    GlobalHelper.showProgress(this)
                    presenter!!.sos(userRideStatusModel!!.body.get(0).ride_id)
                } catch (ex: Exception) {
                }

            }
            builder.setNegativeButton("No") { dialog, which ->
            }
            builder.show()
        }


        linBottom_driverDetails!!.cancel_driverDetails.setOnClickListener {

            if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {
                val bookingCancel = BookingCancel(userRideStatusModel)
                bookingCancel.show(supportFragmentManager, "")
            }

        }

        linBottom_driverDetails!!.linCard_driverDetails.setOnClickListener {
            val intent = Intent(this, GetCards::class.java)
            intent.putExtra(MAP_ACTIVITY, MAP_ACTIVITY)
            startActivityForResult(intent, CARD_TYPE)
        }

        linBottom_driverDetails!!.imgCall_driverDetails.setOnClickListener {
            if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {
                /* val intent: Intent = Intent(Intent.ACTION_DIAL)
                 intent.setData(Uri.parse("tel:${userRideStatusModel!!.body.get(0).driver.phone}"))
                 startActivity(intent)*/


                val fragmentManager = supportFragmentManager!!.beginTransaction()
                val phoneDialog = PhoneDialog(userRideStatusModel!!.body.get(0).driver.phone, this)
                phoneDialog?.show(fragmentManager, "")

            }
        }

    }

    fun bottomSheetStop() {
        linBottom_stop = findViewById<View>(R.id.bottom_sheet_layout) as LinearLayout
        val bottomSheetStopInfo = BottomSheetBehavior.from(bottom_sheet_layout)
        bottomSheetStopInfo.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetStopInfo.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
            }
        })


        linBottom_stop!!.done_stopInfoBottomsheet.setOnClickListener {

            //-----------------to show on request accepted------------------------------
            if (edt_locationFrom_mapAct.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootmapAct, getString(R.string.source_loc))
            else if (edt_locatioTo_mapAct.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootmapAct, getString(R.string.dest_loc))
            else {

                try {

                    GlobalHelper.TOTAL_DISTANCE = 0.0
                    GlobalHelper.drawPath(
                        locData.source_lat.toDouble(),
                        locData.source_lng.toDouble(),
                        locData.dest_lat.toDouble(),
                        locData.dest_lng.toDouble(),
                        mMap!!, this
                    )

                    Handler().postDelayed(
                        {
                            linBottom_stop!!.visibility = View.GONE
                            rel_enterLocations.visibility = View.GONE

                            presenter?.getCarTypes(locData)
                        },
                        1000 // value in milliseconds
                    )


                } catch (ex: Exception) {
                    GlobalHelper.snackBar(rootmapAct, "Something went wrong.Please try again later.")
                }


            }
        }


    }


    fun bottomSheetRideStatus() {
        linBottom_rideStatus = findViewById<View>(R.id.bottom_sheet__rideStatus) as LinearLayout
        val bottomSheetRideStatus = BottomSheetBehavior.from(bottom_sheet__rideStatus)
        bottomSheetRideStatus.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetRideStatus.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
            }
        })


    }

    fun bottomSheetConfirmPickup() {
        linBottom_confirmPickup = findViewById<View>(R.id.bottom_sheet__confirmPickup) as LinearLayout
        bottomSheetConfirmPickup = BottomSheetBehavior.from(bottom_sheet__confirmPickup)
        bottomSheetConfirmPickup!!.setState(BottomSheetBehavior.STATE_COLLAPSED)
        bottomSheetConfirmPickup!!.setState(BottomSheetBehavior.STATE_EXPANDED)
        bottomSheetConfirmPickup!!.setState(BottomSheetBehavior.STATE_HIDDEN)
        bottomSheetConfirmPickup!!.setPeekHeight(340)
        bottomSheetConfirmPickup!!.setHideable(false)
        bottomSheetConfirmPickup!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(bottomSheet: View, p1: Int) {
            }
        })



        linBottom_confirmPickup!!.confirm_pickup.setOnClickListener {


            GlobalHelper.showProgress(this)
            presenter!!.confirmPickup(
                userRideStatusModel!!.body.get(0).ride_id,
                userRideStatusModel!!.body.get(0).from_lat,
                userRideStatusModel!!.body.get(0).from_long
            )
        }

    }

    override fun OnSplitFareSuccess(status: String, ride_id: String) {
        presenter!!.splitRequest(ride_id, status)
    }

    override fun onSplitRequestSuccess(splitRequest: SplitRequest) {
        try {
            GlobalHelper.snackBar(rootmapAct, splitRequest.message)
        } catch (ex: Exception) {
        }
    }

    fun showSplitFareDialog(notifiData: NotificationData) {
        val splitFare = SplitFareDialog(this, notifiData)
        splitFare.show(supportFragmentManager, "SpiltFareDialog")

    }

    fun showPaymentDetail(userRideStatus: UserRideStatus) {

        val paymentPoints = PaymentPoints(userRideStatus)
        val ft = supportFragmentManager.beginTransaction()
        ft.add(paymentPoints!!, "")
        ft.commitAllowingStateLoss()


    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun startRevealAnimation(rootView: View) {

        val cx = rootView.getMeasuredWidth() / 2
        val cy = rootView.getMeasuredHeight() / 2

        val anim = ViewAnimationUtils.createCircularReveal(rootView, cx, cy, 50f, rootView.getWidth().toFloat())

        anim.duration = 500
        anim.interpolator = AccelerateInterpolator(2f) as TimeInterpolator?
        anim.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                bottom_sheet_layout.visibility = View.VISIBLE
                lin_placeSearch.visibility = View.VISIBLE


            }
        })

        anim.start()
    }

    override fun onRequestCabScheduleSuccess(requestCab: RequestCabSchedule) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootmapAct, requestCab.message)
            linBottom_requestCab!!.visibility = View.GONE

            finishAffinity()
            startActivity(Intent(this, Home::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        } catch (ex: Exception) {
        }
    }

    override fun onSosSuccess(sos: Sos) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootmapAct, sos.message)
        } catch (ex: Exception) {
        }
    }


    override fun OnScheduleCab(locDataNew: LocationData) {
        presenter!!.requestScheduleCab(locDataNew)
    }

    override fun onLogoutSuccess(logout: Logout) {
        try {
            if (logout.code.equals(200)) {
                SharedPrefUtil.getInstance()?.setLogin(false)
                SharedPrefUtil.getInstance()?.clear()

                val intent = Intent(this, SignUp::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        } catch (ex: Exception) {
        }
    }

    override fun onUserRideStatusSuccess(userRideStatus: UserRideStatus) {

        try {

            if (userRideStatus.code.equals(200)) {
                userRideStatusModel = userRideStatus



                if (userRideStatus.body.size > 0 && !userRideStatus.body.get(0).status.equals("pending")) {
                    layoutChanges()
                    currentDriverId = userRideStatusModel!!.body.get(0).driver.email
                    showAllDriver = false
                    linBottom_rideStatus!!.visibility = View.GONE
                    rel_enterLocations.visibility = View.GONE
                    linBottom_stop!!.visibility = View.GONE
                } else {
                    currentDriverId = ""
                    showAllDriver = true


                    if (userRideStatusModel!!.body.size > 0) {
                        rel_enterLocations.visibility = View.GONE
                        linBottom_stop!!.visibility = View.GONE
                        linBottom_rideStatus!!.visibility = View.VISIBLE

                        setRideStatus(userRideStatusModel!!)
                    } else {

                    }

                }


            }
        } catch (ex: Exception) {
        }

    }


    fun layoutChanges() {

        if (userRideStatusModel!!.body.get(0).status.equals("pending")) {

        } else if (userRideStatusModel!!.body.get(0).status.equals("notConfirmed")) {
            linBottom_confirmPickup!!.visibility = View.VISIBLE
            showConfirm(userRideStatusModel!!)
        } else if (userRideStatusModel!!.body.get(0).status.equals("confirmed")) {
            linBottom_driverDetails!!.visibility = View.VISIBLE
            setDriverDetails(userRideStatusModel!!)
        }
        else if (userRideStatusModel!!.body.get(0).status.equals("onTheWay")) {
            linBottom_driverDetails!!.visibility = View.VISIBLE
            setDriverDetails(userRideStatusModel!!)
        }
        else if (userRideStatusModel!!.body.get(0).status.equals("Reached")) {
            linBottom_driverDetails!!.visibility = View.VISIBLE
            setDriverDetails(userRideStatusModel!!)
        } else if (userRideStatusModel!!.body.get(0).status.equals("Completed")) {
            linBottom_driverDetails!!.visibility = View.GONE
            showPaymentDetail(userRideStatusModel!!)
        } else if (userRideStatusModel!!.body.get(0).status.equals("paymentDone")) {

            val ft = supportFragmentManager.beginTransaction()
            val rating = Rating(userRideStatusModel)
            rating.show(ft, "")
        }

        //-------------for drawing path for current driver----------------
        if (userRideStatusModel!!.body.get(0).status.equals("confirmed") ||
            userRideStatusModel!!.body.get(0).status.equals("pending") ||
            userRideStatusModel!!.body.get(0).status.equals("notConfirmed")
        ) {
            showOnlyCurrentDriver = true
            if (mMap != null) {
                mMap!!.clear()
                addCarOnMap = true

                if (userRideStatusModel!!.body.get(0).status.equals("confirmed")) {
                    GlobalHelper.drawPath(
                        GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG,
                        userRideStatusModel!!.body.get(0).from_lat.toDouble(),
                        userRideStatusModel!!.body.get(0).from_long.toDouble(),
                        mMap!!, this
                    )
                } else {
                    GlobalHelper.drawPath(
                        userRideStatusModel!!.body.get(0).from_lat.toDouble(),
                        userRideStatusModel!!.body.get(0).from_long.toDouble(),
                        userRideStatusModel!!.body.get(0).to_lat.toDouble(),
                        userRideStatusModel!!.body.get(0).to_long.toDouble(),
                        mMap!!, this
                    )
                }
            } else {
                Handler().postDelayed({
                    if (mMap != null) {
                        mMap!!.clear()
                        addCarOnMap = true

                        if (userRideStatusModel!!.body.get(0).status.equals("confirmed")) {
                            GlobalHelper.drawPath(
                                GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG,
                                userRideStatusModel!!.body.get(0).from_lat.toDouble(),
                                userRideStatusModel!!.body.get(0).from_long.toDouble(),
                                mMap!!, this
                            )
                        } else {
                            GlobalHelper.drawPath(
                                userRideStatusModel!!.body.get(0).from_lat.toDouble(),
                                userRideStatusModel!!.body.get(0).from_long.toDouble(),
                                userRideStatusModel!!.body.get(0).to_lat.toDouble(),
                                userRideStatusModel!!.body.get(0).to_long.toDouble(),
                                mMap!!, this
                            )
                        }
                    }
                }, 3000)
            }

        } else
            showOnlyCurrentDriver = false


    }


    fun setDriverDetails(userRideStatusModel: UserRideStatus) {

        linBottom_driverDetails!!.driverName_driverDetails.text =
            "${userRideStatusModel.body.get(0).driver.first_name} ${userRideStatusModel.body.get(0).driver.last_name}"
        linBottom_driverDetails!!.rideType_driverDetails.text = userRideStatusModel.body.get(0).vehicle_type_name
        linBottom_driverDetails!!.vehNumber_driverDetails.text = userRideStatusModel.body.get(0).vehicle_number
        linBottom_driverDetails!!.dest_driverDetails.text = userRideStatusModel.body.get(0).to_address
        linBottom_driverDetails!!.amount_driverDetails.text =
            "$pound ${GlobalHelper.uptoTwoDecimal(userRideStatusModel.body.get(0).amount)}"
        linBottom_driverDetails!!.txt_rating_driverDetails.text =
            GlobalHelper.uptoOneDecimal(userRideStatusModel.body.get(0).average_rating)
        linBottom_driverDetails!!.ratingBar_driverDetails.rating =
            userRideStatusModel.body.get(0).average_rating.toFloat()
        Glide.with(this).load(userRideStatusModel.body.get(0).driver.image).into(driverImage_driverDetails)

    }

    fun setRideStatus(userRideStatusModel: UserRideStatus) {
        linBottom_rideStatus!!.sourceAdd_rideStatus.text = userRideStatusModel.body.get(0).from_address
        linBottom_rideStatus!!.destAdd_rideStatus.text = userRideStatusModel.body.get(0).to_address
    }


    fun moveToHome() {
        GlobalHelper.snackBar(rootmapAct, "No Driver available")
        Handler().postDelayed(
            {
                finishAffinity()
                startActivity(Intent(this, Home::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            },
            3000 // value in milliseconds
        )

    }


    fun getDriverListing() {

        mainHandler.post(object : Runnable {
            override fun run() {

                if (!CURRENT_LAT.equals("0.0") && !CURRENT_LNG.equals("0.0")) {
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


        runOnUiThread {

            if (event.equals(mSocketManager!!.GET_LIST)) {

                try {
                    val fileData = args.toString()
                    val gson = GsonBuilder().create()
                    val getDriverList = gson.fromJson(fileData, Array<DriverList>::class.java).toList()

                    //-----to show heatmap---------------------------------------
                   /* try {
                        if (arrayListOfDriverListing != null && arrayListOfDriverListing.size == 0) {
                            arrayListOfDriverListing.addAll(getDriverList)
                            arrayHeatMap(arrayListOfDriverListing)
                        }
                    } catch (ex: Exception) {
                    }*/



                    for (i in 0..getDriverList.size - 1) {
                        val latlng =
                            LatLng(getDriverList.get(i).latitude.toDouble(), getDriverList.get(i).longitude.toDouble())

                        val hashMap: HashMap<String, LatLng> = HashMap()
                        hashMap.put("latLng", latlng)
                        arrayListMark.add(hashMap)


                        //  if (count<32)
                        showOrAnimateMarker(
                            LatLng(
                                getDriverList.get(i).latitude.toDouble(),
                                getDriverList.get(i).longitude.toDouble()
                            ), getDriverList.get(i).id, i
                        )

                        Handler().postDelayed({
                            addCarOnMap = false
                        }, 2000)

                    }
                } catch (ex: Exception) {
                }

            } else {

            }
        }

    }


    fun showOrAnimateMarker(latLng: LatLng, id: String, pos: Int) {

        if (showOnlyCurrentDriver) {

            //  RetrieveSnapRoad().execute()
            //   getsnappedLatLng()

            if (addCarOnMap) {

                if (mMap != null) {
                    if (currentDriverId.equals(id)) {

                        if (arrayOfDriverId.contains(id)) {
                        } else {
                            var marker = mMap!!.addMarker(getDriverMarkerOptions(latLng))
                            marker.tag = id
                            arrayOfCarMarkers.add(marker)
                            arrayOfDriverId.add(id)
                            addCarOnMap = false
                        }

                    }
                }
            } else {

                for (p in 0..arrayOfCarMarkers.size - 1) {


                    if (currentDriverId.equals(id)) {

                        if (id.equals(arrayOfCarMarkers.get(p).tag)) {

                            /* snapLatPass = latLng.latitude
                             snapLngPass = latLng.longitude

                             if (snapLatGet!= 0.0 && snapLngGet != 0.0)
                             {
                                 markerAnimationHelper.animateMarkerToGB(arrayOfCarMarkers.get(p)!!,
                                     latLng, LatLngInterpolator.Spherical())
                             }*/

                            markerAnimationHelper.animateMarkerToGB(
                                arrayOfCarMarkers.get(p)!!,
                                latLng,
                                LatLngInterpolator.Spherical()
                            )
                        }
                    }
                }
            }
        } else if (showAllDriver) {

            //    getsnappedLatLng()

            if (addCarOnMap) {
                if (arrayOfDriverId.contains(id)) {
                }   // to avoid second time to add same marker in list
                else {
                    var marker = mMap!!.addMarker(getDriverMarkerOptions(latLng))
                    marker.tag = id

                    arrayOfCarMarkers.add(marker)
                    arrayOfDriverId.add(id)
                }

            } else {
                for (p in 0..arrayOfCarMarkers.size - 1) {
                    if (id.equals(arrayOfCarMarkers.get(p).tag)) {
                        /*snapLatPass = latLng.latitude
                        snapLngPass = latLng.longitude

                        if (snapLatGet!= 0.0 && snapLngGet != 0.0)
                        {
                            markerAnimationHelper.animateMarkerToGB(array.get(p)!!, LatLng(30.709169374991795,76.69567892558628), LatLngInterpolator.Spherical())
                          //  markerAnimationHelper.animateMarkerToGB(array.get(p)!!, LatLng(snapLatGet,snapLngGet), LatLngInterpolator.Spherical())
                        }*/


                        /* markerAnimationHelper.animateMarkerToGB(arrayOfCarMarkers.get(p)!!, arrayLatLng.get(count), LatLngInterpolator.Spherical())
                         count++*/

                        markerAnimationHelper.animateMarkerToGB(
                            arrayOfCarMarkers.get(p)!!,
                            latLng,
                            LatLngInterpolator.Spherical()
                        )
                    }
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

        val b: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_icon1)
        val smallMarker: Bitmap = Bitmap.createScaledBitmap(b, 50, 100, false)

        return MarkerOptions()
            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
            // .icon(BitmapDescriptorFactory.fromResource(resource))
            .position(position)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    //----for share trip----------------------
                    if (requestCode == CONTACT_TYPE) {
                        val frnd_ids = data!!.getStringExtra(FRIEND_IDS)
                        presenter!!.shareTrip(frnd_ids)
                    } else if (requestCode == CARD_TYPE) {
                    } else if (requestCode == CHANGE_ADDRESS) {


                        try {

                            val address = data!!.getStringExtra(PLACE_NAME)
                            val latLng = GlobalHelper.getLatLngFromAddress(this, address)

                            val locData: LocationData = LocationData()

                            locData.dest_loc = address
                            locData.dest_lat = latLng!!.latitude.toString()
                            locData.dest_lng = latLng!!.longitude.toString()

                            locData.source_lat = GlobalHelper.CURRENT_LAT.toString()
                            locData.source_lng = GlobalHelper.CURRENT_LNG.toString()
                            locData.source_loc = GlobalHelper.getCompleteAddressFromLatLng(
                                GlobalHelper.CURRENT_LAT,
                                GlobalHelper.CURRENT_LNG,
                                this
                            )

                            locData.payment_mode = "1"
                            locData.booking_date = ""
                            locData.time = ""

                            GlobalHelper.TOTAL_DISTANCE = 0.0

                            GlobalHelper.drawPath(
                                GlobalHelper.CURRENT_LAT,
                                GlobalHelper.CURRENT_LNG,
                                latLng.latitude,
                                latLng.longitude,
                                mMap!!, this
                            )

                            Handler().postDelayed(
                                {
                                    if (GlobalHelper.TOTAL_DISTANCE == 0.0) {


                                        Handler().postDelayed({

                                            locData.distance = GlobalHelper.TOTAL_DISTANCE.toString()
                                            locData.ride_id = userRideStatusModel!!.body.get(0).ride_id
                                            locData.vehilce_id = userRideStatusModel!!.body.get(0).vehicle_type_id
                                            locData.amount = userRideStatusModel!!.body.get(0)
                                                .amount   // need to change from backend
                                            locData.est_price =
                                                userRideStatusModel!!.body.get(0).amount // need to change from backend

                                            presenter!!.requestCab(CHANGE_DESTINATION_TYPE, locData)

                                        }, 3000)
                                    } else {
                                        //    linBottom_driverDetails!!.dest_driverDetails.text = address

                                        try {
                                            val amt: Double =
                                                userRideStatusModel!!.body.get(0).estimated_price.toDouble() * GlobalHelper.TOTAL_DISTANCE
                                            locData.amount = amt.toString()
                                        } catch (ex: Exception) {
                                            locData.amount = userRideStatusModel!!.body.get(0).estimated_price
                                        }

                                        locData.distance = GlobalHelper.TOTAL_DISTANCE.toString()
                                        locData.ride_id = userRideStatusModel!!.body.get(0).ride_id
                                        locData.vehilce_id = userRideStatusModel!!.body.get(0).vehicle_type_id
                                        locData.amount =
                                            userRideStatusModel!!.body.get(0).amount  // need to change from backend
                                        locData.est_price = userRideStatusModel!!.body.get(0).estimated_price

                                        presenter!!.requestCab(CHANGE_DESTINATION_TYPE, locData)
                                    }


                                },
                                3000 // value in milliseconds
                            )


                        } catch (ex: Exception) {
                            GlobalHelper.snackBar(rootmapAct, "Something went wrong.Please try again later.")
                        }

                    } else if (requestCode == STOP1_ADDRESS) {
                        val address = data!!.getStringExtra(PLACE_NAME)
                        val latLng = GlobalHelper.getLatLngFromAddress(this, address)

                        locData.stop_lat = latLng!!.latitude.toString()
                        locData.stop_lng = latLng!!.longitude.toString()

                        locData.stop_loc = address
                        edt_locatioStop_mapAct.text = address
                    }
                    else if (requestCode == DESTINATION_ADDRESS) {
                        val address = data!!.getStringExtra(PLACE_NAME)

                        val latLng = GlobalHelper.getLatLngFromAddress(this, address)

                        locData.dest_lat = latLng!!.latitude.toString()
                        locData.dest_lng = latLng!!.longitude.toString()
                        locData.dest_loc = address
                        edt_locatioTo_mapAct.text = address

                    }
                    else if (requestCode == SOURCE_ADDRESS) {
                        val address = data!!.getStringExtra(PLACE_NAME)
                        val latLng = GlobalHelper.getLatLngFromAddress(this, address)

                        locData.source_lat = latLng!!.latitude.toString()
                        locData.source_lng = latLng!!.longitude.toString()

                        locData.source_loc = address
                        edt_locationFrom_mapAct.text = address

                        calculatePrice(locData.source_lat,locData.source_lng)

                    } else if (requestCode == CHANGE_CONFIRM_ADDRESS) {

                        try {


                            val address = data!!.getStringExtra(PLACE_NAME)
                            val latLng = GlobalHelper.getLatLngFromAddress(this, address)

                            TOTAL_DISTANCE = 0.0

                            if (latLng != null && userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {
                                GlobalHelper.getRoadDistance(
                                    latLng.latitude, latLng.longitude,
                                    userRideStatusModel!!.body.get(0).from_lat.toDouble(),
                                    userRideStatusModel!!.body.get(0).from_long.toDouble()
                                )
                            }


                            //  pickupLoc_ConfirmRide.text =address

                            if (userRideStatusModel != null && userRideStatusModel!!.body.size > 0) {

                                //---------------------------------------------------------
                                val timer = Timer()
                                timer.scheduleAtFixedRate(object : TimerTask() {
                                    override fun run() {


                                        runOnUiThread(object : Runnable {
                                            override fun run() {

                                                if (TOTAL_DISTANCE != 0.0) {
                                                    try {

                                                        val totalAmount =
                                                            userRideStatusModel!!.body.get(0).estimated_price.toDouble() * TOTAL_DISTANCE
                                                        amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())

                                                       presenter!!.pickupChange(
                                                            userRideStatusModel!!.body.get(0).ride_id,
                                                            latLng!!.latitude.toString(),
                                                            latLng!!.longitude.toString(),
                                                            TOTAL_DISTANCE.toString(),
                                                            totalAmount.toString(),address)
                                                        TOTAL_DISTANCE = 0.0
                                                    } catch (ex: Exception) {
                                                    }

                                                    timer.cancel()
                                                }


                                            }

                                        })
                                    }


                                }, 0, 2000)

                                //---------------------------------------------------------


                            }

                        } catch (ex: Exception) {
                        }


                    }


                } catch (ex: Exception) {
                }


            }

        }


    }


    private fun addHeatMap(list: ArrayList<LatLng>) {


        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = HeatmapTileProvider.Builder()
            .data(list)
            // .radius(50)  this max is 50
            .build()
        mProvider!!.setRadius(70)
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap!!.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))
    }


    fun getsnappedLatLng() {


        async {

            snapUrl =
                URL(SNAP_TO_ROAD_API + snapLatPass + "," + snapLngPass + "&interpolate=true&key=AIzaSyDfC5nxxgV7aqR2QQrpFm3ALGrEYSNkk48")
            Log.e("printUrl", snapUrl.toString())


            val result = URL(snapUrl.toString()).readText()
            uiThread {
                // When API call is done, create parser and convert into JsonObjec
                val parser: Parser = Parser()
                val stringBuilder: StringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject


                /*  val routes = json.array<JsonObject>("routes")

                  if (routes!!["legs"]["steps"].size>0) {


                      val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>

                      //-----------to get distance and duration
                      val getDuration = routes!!["legs"]["duration"][0] as JsonObject
                      val getDistance = routes!!["legs"]["distance"][0] as JsonObject
                      val duration = getDuration.get("text").toString()
                      val distance = getDistance.get("text").toString()


                  }*/

            }
        }


    }


    override fun onBackPressed() {
        //  super.onBackPressed()
        finishAffinity()
        startActivity(Intent(this, Home::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }


    fun arrayHeatMap(arrayListOfDriverListing: ArrayList<DriverList>) {

        arrayListOfHeatMap.clear()
        for (i in 0..arrayListOfDriverListing.size - 1) {
            var count: Int = 0
            for (j in 0..arrayListOfDriverListing.size - 1) {
                if ((GlobalHelper.getDistanceBetweenLatLng(
                        arrayListOfDriverListing.get(i).latitude.toDouble(),
                        arrayListOfDriverListing.get(i).longitude.toDouble(),
                        arrayListOfDriverListing.get(j).latitude.toDouble(),
                        arrayListOfDriverListing.get(j).longitude.toDouble()
                    )) < 1
                ) {
                    count++
                    if (count == 5) {
                        arrayListOfHeatMap.add(
                            LatLng(
                                arrayListOfDriverListing.get(i).latitude.toDouble(),
                                arrayListOfDriverListing.get(i).longitude.toDouble()
                            )
                        )
                    }

                }
            }
        }

      //  addHeatMap(arrayListOfHeatMap)
    }



    fun calculatePrice(srcLat : String, strLng : String)
    {
        if (arrayListOfHeatMap!=null && arrayListOfHeatMap.size>0)
        {
            try {

                for (i in 0..arrayListOfHeatMap.size-1)
                {
                 val dis =   GlobalHelper.getDistanceBetweenLatLng(srcLat.toDouble(),strLng.toDouble()
                        ,arrayListOfHeatMap.get(i).latitude,arrayListOfHeatMap.get(i).longitude)

                    if (dis<.5)
                        GlobalHelper.CURRENT_PRICE_SURGE = 1.5
                }

            }catch (ex : Exception){}

        }
    }





}

