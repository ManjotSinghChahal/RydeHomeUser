package com.example.rydehomeuser.ui.activities.searchPlacesActivity


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.utils.GlobalHelper.getAutocompletes
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.search_places.*
import kotlin.concurrent.thread
import android.app.Activity
import android.content.Intent
import com.example.rydehomeuser.data.model.getSavedAddress.GetSavedAddress
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTINGS
import com.example.rydehomeuser.utils.Constants.PLACE_NAME
import com.example.rydehomeuser.utils.Constants.SEARCH_PLACES
import com.google.android.gms.location.places.AutocompleteFilter


class SearchPlacesActivity : AppCompatActivity() , SearchPlacesAdpt.PlaceSel, HomeContract.SearchPlacesView{


    var mGeoDataClientNew: GeoDataClient? = null
    var recyclerviewSearchPlaces : RecyclerView? = null
    var presenter: HomeContract.HomePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_places)


        val filter : AutocompleteFilter= AutocompleteFilter.Builder()
            .setCountry("IN")
            .build()


        recyclerviewSearchPlaces = findViewById(R.id.recyclerview_searchPlaces) as RecyclerView
        recyclerviewSearchPlaces?.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        mGeoDataClientNew = Places.getGeoDataClient(this, null)

        presenter = HomePresenterImp(this)
        presenter!!.getSavedAddress(SEARCH_PLACES)


        relBack_searchPlaces.setOnClickListener {
            onBackPressed()
        }

        relCross_searchPlaces.setOnClickListener {
            edit_searchPlaces.setText("")
            relCross_searchPlaces.visibility = View.GONE
        }

        relWorkAdd_savedPlaces.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(PLACE_NAME, workAdd_savedPlaces.text.toString().trim())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        relHomeAdd_savedPlaces.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(PLACE_NAME, homeAdd_savedPlaces.text.toString().trim())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        relOther1Add_savedPlaces.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(PLACE_NAME, other1Add_savedPlaces.text.toString().trim())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        relOther2Add_savedPlaces.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra(PLACE_NAME, other2Add_savedPlaces.text.toString().trim())
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

      //  https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318&markers=color:red%7Clabel:C%7C40.718217,-73.998284&key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE



        edit_searchPlaces.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                try {

                    thread {

                        runOnUiThread({

                            if (s.toString().equals(""))
                            {
                                recyclerview_searchPlaces.visibility = View.GONE
                                relCross_searchPlaces.visibility = View.GONE
                            }
                            else
                            {
                                recyclerview_searchPlaces.visibility = View.VISIBLE
                                relCross_searchPlaces.visibility = View.VISIBLE
                            }
                        })


                        var filterData: ArrayList<AutocompletePrediction>? = ArrayList()
                        filterData = getAutocompletes(s.toString(), mGeoDataClientNew,filter)
                        if (filterData!=null && filterData!!.size > 0)
                        {
                            updateRecycleview(filterData)
                        }


                    }

                }catch (ex : Exception){}



                    }
        })

    }

    fun updateRecycleview(filterData: ArrayList<AutocompletePrediction>) {
        runOnUiThread {


                if (filterData.size > 0)
                {
                    recyclerviewSearchPlaces!!.adapter = SearchPlacesAdpt(this, filterData, this)
                }

            }



    }


    override fun OnPlaceSel(place: String) {
      Log.e("printPlaces",place)

        val returnIntent = Intent()
        returnIntent.putExtra(PLACE_NAME, place)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    override fun onGetSavedAddressSuccess(getSavedAddress: GetSavedAddress) {

        try {

            if (getSavedAddress.code.equals(200))
            {

                if (!getSavedAddress.body.home.address.isEmpty())
                {
                    homeAdd_savedPlaces.text = getSavedAddress.body.home.address
                    relHomeAdd_savedPlaces.visibility = View.VISIBLE
                }

                if (!getSavedAddress.body.work.address.isEmpty())
                {
                    workAdd_savedPlaces.text = getSavedAddress.body.work.address
                    relWorkAdd_savedPlaces.visibility = View.VISIBLE
                }

                if (getSavedAddress.body.others.size == 1)
                {
                    other1Add_savedPlaces.text = getSavedAddress.body.others.get(0).address
                    relOther1Add_savedPlaces.visibility = View.VISIBLE
                }
                else if (getSavedAddress.body.others.size == 2)
                {
                    other1Add_savedPlaces.text = getSavedAddress.body.others.get(0).address
                    other2Add_savedPlaces.text = getSavedAddress.body.others.get(1).address
                    relOther1Add_savedPlaces.visibility = View.VISIBLE
                    relOther2Add_savedPlaces.visibility = View.VISIBLE
                }

            }
        }catch (ex : Exception){}

    }

    override fun onFailure(error: String) {

        try {

        }catch (ex : Exception){}

    }

}
