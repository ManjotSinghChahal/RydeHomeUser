package com.example.rydehomeuser.ui.activities.searchPlaces


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.mapActivity.MapActivity
import com.example.rydehomeuser.ui.activities.mapActivity.PlacesAdapter
import com.example.rydehomeuser.utils.GlobalHelper
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceBufferResponse
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_search_places.*

class SearchPlaces : AppCompatActivity() , OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    lateinit var placesAdapter: PlacesAdapter
    lateinit var mGeoDataClient: GeoDataClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_places)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.search_activity) as SupportMapFragment
        mapFragment.getMapAsync(this)




        mGeoDataClient = Places.getGeoDataClient(this, null)
        placesAdapter = PlacesAdapter(this, android.R.layout.simple_list_item_1, mGeoDataClient, null,
            LatLngBounds(LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG), LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)))
        edt_location_search.setAdapter(placesAdapter)

        edt_location_search.setOnItemClickListener({ parent, view, position, id ->

            val item = placesAdapter.getItem(position)
            val placeId = item?.getPlaceId()
            val primaryText = item?.getPrimaryText(null)

            Log.i("Autocomplete", "Autocomplete item selected: " + primaryText)


            val placeResult = mGeoDataClient.getPlaceById(placeId)
            placeResult.addOnCompleteListener(object : OnCompleteListener<PlaceBufferResponse> {
                override fun onComplete(task: Task<PlaceBufferResponse>) {
                    val places = task.getResult();
                    val place = places!!.get(0)

                   /* MapActivity.searchPlaces_lat = place.latLng.latitude.toString()
                    MapActivity.searchPlaces_lat = place.latLng.longitude.toString()
                    MapActivity.searchPlaces_address = place.address.toString()
*/
                   finish()

                    places.release()
                }

            })


        })


    }


    override fun onMapReady(googleMap: GoogleMap?) {
        this.mMap = googleMap

        if (!GlobalHelper.CURRENT_LAT.equals(0.0) && !GlobalHelper.CURRENT_LNG.equals(0.0)) {
            val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
            mMap?.addMarker(
                MarkerOptions()
                    .position(position)
                    .title("")
            )
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
        }

    }

    /*interface PLacesSearch
    {
        fun OnPlacesSearch(lat : String, lng : String)
    }*/

    }
