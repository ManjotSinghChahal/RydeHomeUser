package com.example.rydehomeuser.ui.activities.savedPlaces

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.addAddress.AddAddress
import com.example.rydehomeuser.data.model.deleteSavedAddress.DeleteSavedAddress
import com.example.rydehomeuser.data.model.getSavedAddress.GetSavedAddress
import com.example.rydehomeuser.data.model.getShareTrip.GetShareTrip
import com.example.rydehomeuser.data.model.logout.Logout
import com.example.rydehomeuser.data.model.shareTrip.ShareTrip
import com.example.rydehomeuser.data.model.synContacts.SynContacts
import com.example.rydehomeuser.data.saveData.saveAddress.SaveAddress
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.searchPlacesActivity.SearchPlacesActivity
import com.example.rydehomeuser.utils.Constants.HOME_ADDRESS_TYPE
import com.example.rydehomeuser.utils.Constants.OTHER_ADDRESS_TYPE
import com.example.rydehomeuser.utils.Constants.PLACE_NAME
import com.example.rydehomeuser.utils.Constants.SAVED_PLACES
import com.example.rydehomeuser.utils.Constants.WORK_ADDRESS_TYPE
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.activity_saved_places.*


class SavedPlaces : AppCompatActivity(), HomeContract.AccountSettingView, SavedPlacesAdapter.SavedPlacesInterface {



    val HOME_ADDRESS : Int = 3
    val WORK_ADDRESS : Int = 4
    val OTHER_ADDRESS : Int = 5
    var presenter: HomeContract.HomePresenter? = null
    var recyclerviewSavedPlaces : RecyclerView? = null

    var homeAddId : String = ""
    var workAddId : String = ""
    var otherSavedAddressSize : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_places)



        initialize()
        clickListener()
    }

    fun initialize()
    {
        presenter = HomePresenterImp(this)
        presenter!!.getSavedAddress(SAVED_PLACES)


        recyclerviewSavedPlaces = findViewById(R.id.recyclerview_savedPlaces) as RecyclerView
        recyclerviewSavedPlaces?.layoutManager = LinearLayoutManager(this)

    }

    fun clickListener()
    {

        imgBack_savedPlaces.setOnClickListener {
            onBackPressed()
        }

        imgCross_homeAddress_savedPlaces.setOnClickListener {
            presenter!!.deleteSavedAddress(homeAddId)
            txtview_addHomeAddress_savedPlaces.text = getString(R.string.add_home)
            imgCross_homeAddress_savedPlaces.visibility = View.GONE
        }

        imgCross_workAddress_savedPlaces.setOnClickListener {
            presenter!!.deleteSavedAddress(workAddId)
            txtview_addWork_savedPlaces.text = getString(R.string.add_work)
            imgCross_workAddress_savedPlaces.visibility = View.GONE
        }



        lin_addHome_savedPlaces.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), HOME_ADDRESS)
        }

        lin_addWorkAddress_savedPlaces.setOnClickListener {
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), WORK_ADDRESS)
        }

        saveOtherPlaces.setOnClickListener {
            if (otherSavedAddressSize >= 2)
            GlobalHelper.snackBar(rootSavedPlaces,"You can save atmost two other addresses. ")
            else
            startActivityForResult(Intent(this, SearchPlacesActivity::class.java), OTHER_ADDRESS)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //    super.onActivityResult(requestCode, resultCode, data); // comment this unless you want to pass your result to the activity.
       // data!!.getStringExtra(PLACE_NAME)

        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == HOME_ADDRESS)
            {
                val address = data!!.getStringExtra(PLACE_NAME)
                txtview_addHomeAddress_savedPlaces.text = address

                val latLng = GlobalHelper.getLatLngFromAddress(this,address)
                val saveAddress = SaveAddress()
                saveAddress.type = HOME_ADDRESS_TYPE
                saveAddress.name = ""
                saveAddress.lat = latLng!!.latitude.toString()
                saveAddress.long = latLng!!.longitude.toString()
                saveAddress.location = address

                presenter!!.addAddress(saveAddress)

                imgCross_homeAddress_savedPlaces.visibility = View.VISIBLE
            }
            else if (requestCode == WORK_ADDRESS)
            {
                val address = data!!.getStringExtra(PLACE_NAME)
                txtview_addWork_savedPlaces.text = address

                val latLng = GlobalHelper.getLatLngFromAddress(this,address)
                val saveAddress = SaveAddress()
                saveAddress.type = WORK_ADDRESS_TYPE
                saveAddress.name = ""
                saveAddress.lat = latLng!!.latitude.toString()
                saveAddress.long = latLng!!.longitude.toString()
                saveAddress.location = address

                presenter!!.addAddress(saveAddress)

                imgCross_workAddress_savedPlaces.visibility = View.VISIBLE
            }

            else if (requestCode == OTHER_ADDRESS)
            {
                val address = data!!.getStringExtra(PLACE_NAME)


                val latLng = GlobalHelper.getLatLngFromAddress(this,address)
                val saveAddress = SaveAddress()
                saveAddress.type = OTHER_ADDRESS_TYPE
                saveAddress.name = ""
                saveAddress.lat = latLng!!.latitude.toString()
                saveAddress.long = latLng!!.longitude.toString()
                saveAddress.location = address

                presenter!!.addAddress(saveAddress)

            }

        }



      }




    override fun onAddAddressSuccess(addAddress: AddAddress) {

        try {

            if (addAddress.code.equals(200))
            {
                presenter!!.getSavedAddress(SAVED_PLACES)
            }
        }catch (ex : java.lang.Exception){}
    }

    override fun onGetSavedAddressSuccess(getSavedAddress: GetSavedAddress) {

        try {


            if (getSavedAddress.code.equals(200))
            {

                otherSavedAddressSize = getSavedAddress.body.others.size
                if (getSavedAddress.body.others.size>0)
                {
                    txtview_others.visibility = View.VISIBLE
                    recyclerviewSavedPlaces!!.visibility = View.VISIBLE
                    recyclerviewSavedPlaces?.adapter =  SavedPlacesAdapter(this, getSavedAddress.body,this)
                }
                else
                {
                    txtview_others.visibility = View.GONE
                    recyclerviewSavedPlaces!!.visibility = View.GONE
                }



                if (!getSavedAddress.body.home.address.isEmpty())
                {
                    txtview_addHomeAddress_savedPlaces.text = getSavedAddress.body.home.address
                    imgCross_homeAddress_savedPlaces.visibility = View.VISIBLE
                    homeAddId = getSavedAddress.body.home.id
                }

                if (!getSavedAddress.body.work.address.isEmpty())
                {
                    txtview_addWork_savedPlaces.text = getSavedAddress.body.work.address
                    imgCross_workAddress_savedPlaces.visibility = View.VISIBLE
                    workAddId = getSavedAddress.body.work.id
                }
            }
        }catch (ex : Exception){}



    }

    override fun onDeleteSavedAddressSuccess(deleteSavedAddress: DeleteSavedAddress) {

        try {
            if (deleteSavedAddress.code.equals(200))
            {
                presenter!!.getSavedAddress(SAVED_PLACES)
            }
        }catch (ex : java.lang.Exception){}
    }

    override fun onFailure(error: String) {

    }

    override fun OnSavedPlacesDel(address_id: String) {
      presenter!!.deleteSavedAddress(address_id)
    }

    //--------redudant------------------
    override fun onSynContactsSuccess(synContacts: SynContacts) {

    }

    override fun onLogoutSuccess(logout: Logout) {

    }

    override fun onGetShareTripSuccess(getShareTrip: GetShareTrip) {

    }

    override fun onShareTripSuccess(shareTrip: ShareTrip) {

    }

}
