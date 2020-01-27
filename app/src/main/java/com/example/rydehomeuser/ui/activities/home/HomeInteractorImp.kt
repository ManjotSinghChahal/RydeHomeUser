package com.example.rydehomeuser.ui.activities.home

import android.util.Log
import com.app.passerby.data.injectorApi.InjectorApi
import com.app.passerby.data.injectorApi.InterfaceApi
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.addAddress.AddAddress
import com.example.rydehomeuser.data.model.addCard.AddCard
import com.example.rydehomeuser.data.model.applyPromoCode.ApplyPromoCode
import com.example.rydehomeuser.data.model.cancelRide.CancelRide
import com.example.rydehomeuser.data.model.carRequests.CarRequests
import com.example.rydehomeuser.data.model.confirmPickup.ConfirmPickup
import com.example.rydehomeuser.data.model.deleteCard.DeleteCard
import com.example.rydehomeuser.data.model.deleteSavedAddress.DeleteSavedAddress
import com.example.rydehomeuser.data.model.editAccount.UpdateProfile
import com.example.rydehomeuser.data.model.getCarTypes.GetCarTypes
import com.example.rydehomeuser.data.model.getCard.GetCard
import com.example.rydehomeuser.data.model.getFriendList.GetFriendList
import com.example.rydehomeuser.data.model.getProfile.GetProfile
import com.example.rydehomeuser.data.model.getSavedAddress.GetSavedAddress
import com.example.rydehomeuser.data.model.getShareTrip.GetShareTrip
import com.example.rydehomeuser.data.model.getTrips.GetTrips
import com.example.rydehomeuser.data.model.getWalletRewards.GetWalletRewards
import com.example.rydehomeuser.data.model.help.HelpModel
import com.example.rydehomeuser.data.model.logout.Logout
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.model.paymentDone.PaymentDone
import com.example.rydehomeuser.data.model.pickupNotes.PickupNotes
import com.example.rydehomeuser.data.model.pickup_change.PickupChange
import com.example.rydehomeuser.data.model.ratingModel.RatingModel
import com.example.rydehomeuser.data.model.requestCab.RequestCab
import com.example.rydehomeuser.data.model.requestCabSchedule.RequestCabSchedule
import com.example.rydehomeuser.data.model.rideDetails.RideDetails
import com.example.rydehomeuser.data.model.sendOtp.SendOtp
import com.example.rydehomeuser.data.model.shareTrip.ShareTrip
import com.example.rydehomeuser.data.model.sos.Sos
import com.example.rydehomeuser.data.model.splitFare.SplitFare
import com.example.rydehomeuser.data.model.splitRequest.SplitRequest
import com.example.rydehomeuser.data.model.synContacts.SynContacts
import com.example.rydehomeuser.data.model.tipModel.TipModel
import com.example.rydehomeuser.data.model.updateBusinessId.UpdateBusinessId
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.data.saveData.locationData.LocationData
import com.example.rydehomeuser.data.saveData.saveAddress.SaveAddress
import com.example.rydehomeuser.data.saveData.saveCardInfo.SaveCardInfo
import com.example.rydehomeuser.ui.baseclasses.App
import com.example.rydehomeuser.utils.Constants.MESSAGE
import com.example.rydehomeuser.utils.SharedPrefUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException


class HomeInteractorImp : HomeContract.HomeInteractor {



    var mApi: InterfaceApi

    init {
        this.mApi = InjectorApi.provideApi()
    }


    override fun help(ride_id: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.help(ride_id).enqueue(object : retrofit2.Callback<HelpModel> {
            override fun onResponse(call: Call<HelpModel>, response: Response<HelpModel>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onHelpSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<HelpModel>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun updateProfile(phone: String, first_name: String, last_name: String, password: String, city: String,
        country: String, email: String, image: String, callback: HomeContract.OnHomeCompleteListener) {

        var imagePartBody: MultipartBody.Part? = null
        val file = File(image)
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        imagePartBody = MultipartBody.Part.createFormData("image", image, requestBody)


        val phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone)
        val first_nameBody = RequestBody.create(MediaType.parse("text/plain"), first_name)
        val last_nameBody = RequestBody.create(MediaType.parse("text/plain"), last_name)
        val passwordBody = RequestBody.create(MediaType.parse("text/plain"), password)
        val cityBody = RequestBody.create(MediaType.parse("text/plain"), "")
        val countryBody = RequestBody.create(MediaType.parse("text/plain"), country)
        val emailBody = RequestBody.create(MediaType.parse("text/plain"), email)




        if (image.isEmpty())
        {
            mApi.updateProfile1(phoneBody,first_nameBody,last_nameBody,passwordBody,cityBody,countryBody,emailBody/*,imagePartBody*/).enqueue(object : retrofit2.Callback<UpdateProfile>

            {
                override fun onResponse(call: Call<UpdateProfile>, response: Response<UpdateProfile>) {

                    if(response.isSuccessful)
                        response.body()?.let { callback.onEditAccountSucces(it) }
                    else
                    {
                        try {
                            val body = response.errorBody()!!.string()
                            val `object` = JSONObject(body)
                            val error = `object`.getString(MESSAGE)
                            callback.onFailure(error)
                        } catch (e: JSONException) {
                            App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                            e.printStackTrace()
                        } catch (e: IOException) {
                            App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                    callback.onFailure(t.message.toString())
                }



            })
        }
        else
        {
            mApi.updateProfile(phoneBody,first_nameBody,last_nameBody,passwordBody,cityBody,countryBody,emailBody,imagePartBody).enqueue(object : retrofit2.Callback<UpdateProfile>

            {
                override fun onResponse(call: Call<UpdateProfile>, response: Response<UpdateProfile>) {

                    if(response.isSuccessful)
                        response.body()?.let { callback.onEditAccountSucces(it) }
                    else
                    {
                        try {
                            val body = response.errorBody()!!.string()
                            val `object` = JSONObject(body)
                            val error = `object`.getString(MESSAGE)
                            callback.onFailure(error)
                        } catch (e: JSONException) {
                            App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                            e.printStackTrace()
                        } catch (e: IOException) {
                            App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                            e.printStackTrace()
                        }

                    }
                }

                override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                    callback.onFailure(t.message.toString())
                }



            })
        }






    }




    override fun otpVerifiy(otp: String, phone_number: String, callback: HomeContract.OnHomeCompleteListener) {
        // type 1 for phone number and 2 for email

        mApi.otpVerify("User",otp,"1",phone_number,"").enqueue(object : retrofit2.Callback<OtpVerified> {
            override fun onResponse(call: Call<OtpVerified>, response: Response<OtpVerified>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onVerifyOtpSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<OtpVerified>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })

    }



    override fun sendOtp(c_code : String,phone_number: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.sendOtp(c_code,phone_number).enqueue(object : retrofit2.Callback<SendOtp> {
            override fun onResponse(call: Call<SendOtp>, response: Response<SendOtp>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onSendOtpSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<SendOtp>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun getProfile(screen : String,callback: HomeContract.OnHomeCompleteListener) {

        mApi.getProfile().enqueue(object : retrofit2.Callback<GetProfile> {
            override fun onResponse(call: Call<GetProfile>, response: Response<GetProfile>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetProfileSucces(screen, it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<GetProfile>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })
     }


    override fun synContacts(screen: String,contacts: String, callback: HomeContract.OnHomeCompleteListener) {

      //  Log.e("printResponse",contacts)
        mApi.synContacts(contacts).enqueue(object : retrofit2.Callback<SynContacts> {
            override fun onResponse(call: Call<SynContacts>, response: Response<SynContacts>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onSynContactsSucces(screen,it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<SynContacts>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })

    }

    override fun contactList(callback: HomeContract.OnHomeCompleteListener) {

        mApi.getFriendList().enqueue(object : retrofit2.Callback<GetFriendList> {
            override fun onResponse(call: Call<GetFriendList>, response: Response<GetFriendList>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onContactListSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<GetFriendList>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getTrips(screen : String, callback: HomeContract.OnHomeCompleteListener) {



        mApi.getTrips().enqueue(object : retrofit2.Callback<GetTrips> {
            override fun onResponse(call: Call<GetTrips>, response: Response<GetTrips>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetTripsSucces(screen,it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<GetTrips>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getCarTypes(locationData: LocationData, callback: HomeContract.OnHomeCompleteListener) {


        mApi.getCarTypes(locationData.source_lat,locationData.source_lng,locationData.source_loc,
            locationData.dest_lat,locationData.dest_lng,locationData.dest_loc).enqueue(object : retrofit2.Callback<GetCarTypes> {
            override fun onResponse(call: Call<GetCarTypes>, response: Response<GetCarTypes>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetCarTypesSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<GetCarTypes>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun requestCab(screen : String,locationData: LocationData, callback: HomeContract.OnHomeCompleteListener) {

        var jsonString = ""

        if (locationData.stop_loc.isEmpty())
            jsonString = "[]"
        else
        {
            var json = JSONObject()
            json.put("lat",locationData.stop_lat)
            json.put("long",locationData.stop_lng)
            json.put("location",locationData.stop_loc)

            var jsonArray = JSONArray()
            jsonString =  jsonArray.put(json).toString()
        }


        mApi.requestCab(locationData.source_lat,locationData.source_lng,locationData.source_loc,
                        locationData.dest_lat,locationData.dest_lng,locationData.dest_loc,
                        locationData.payment_mode,locationData.est_price,jsonString,
                        locationData.vehilce_id,locationData.booking_date,locationData.time,locationData.amount,locationData.distance,locationData.ride_id
        ).enqueue(object : retrofit2.Callback<RequestCab> {
            override fun onResponse(call: Call<RequestCab>, response: Response<RequestCab>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onRequestCabSucces(screen, it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<RequestCab>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun confirmPickup(ride_id: String, lat: String, lng: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.confirmPickup(ride_id,lat,lng).enqueue(object : retrofit2.Callback<ConfirmPickup> {
            override fun onResponse(call: Call<ConfirmPickup>, response: Response<ConfirmPickup>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onConfirmPickupSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<ConfirmPickup>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun splitFare(friends: String, amount: String, ride_id: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.splitFare(friends,amount,ride_id).enqueue(object : retrofit2.Callback<SplitFare> {
            override fun onResponse(call: Call<SplitFare>, response: Response<SplitFare>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onSplitFareSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<SplitFare>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun splitRequest(ride_id: String, status: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.splitRequest(ride_id,status).enqueue(object : retrofit2.Callback<SplitRequest> {
            override fun onResponse(call: Call<SplitRequest>, response: Response<SplitRequest>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onSplitRequestSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<SplitRequest>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun cancelRide(trip_id: String, amount: String, cancel_reason: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.cancelRide(trip_id,amount,cancel_reason).enqueue(object : retrofit2.Callback<CancelRide> {
            override fun onResponse(call: Call<CancelRide>, response: Response<CancelRide>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onCancelRideSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<CancelRide>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })    }

    override fun rating(ride_id: String, rating: String, review: String, type: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.rating(ride_id,rating,review,type).enqueue(object : retrofit2.Callback<RatingModel> {
            override fun onResponse(call: Call<RatingModel>, response: Response<RatingModel>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onRatingSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<RatingModel>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun addCard(saveCardInfo: SaveCardInfo, callback: HomeContract.OnHomeCompleteListener) {

        mApi.addCard(saveCardInfo).enqueue(object : retrofit2.Callback<AddCard> {
            override fun onResponse(call: Call<AddCard>, response: Response<AddCard>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onAddcardSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<AddCard>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getCard(card_id : String, screen : String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.getCards(card_id).enqueue(object : retrofit2.Callback<GetCard> {
            override fun onResponse(call: Call<GetCard>, response: Response<GetCard>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetCardSucces(screen,it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<GetCard>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun deleteCard(card_id: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.deleteCard(card_id).enqueue(object : retrofit2.Callback<DeleteCard> {
            override fun onResponse(call: Call<DeleteCard>, response: Response<DeleteCard>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onDeleteCardSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }
            override fun onFailure(call: Call<DeleteCard>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun logout(screen : String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.logout(SharedPrefUtil!!.getInstance()?.getUserId()!!).enqueue(object : retrofit2.Callback<Logout> {
            override fun onResponse(call: Call<Logout>, response: Response<Logout>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onLogoutSucces(screen, it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<Logout>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun addAddress(saveAddress: SaveAddress, callback: HomeContract.OnHomeCompleteListener) {

        mApi.addAddress(saveAddress).enqueue(object : retrofit2.Callback<AddAddress> {
            override fun onResponse(call: Call<AddAddress>, response: Response<AddAddress>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onAddAddressSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<AddAddress>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getSavedAddress(screen : String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.getSavedAddress().enqueue(object : retrofit2.Callback<GetSavedAddress> {
            override fun onResponse(call: Call<GetSavedAddress>, response: Response<GetSavedAddress>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetSavedAddressSucces(screen,it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<GetSavedAddress>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })
    }


    override fun deleteSavedAddress(address_id: String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.deleteSavedAddress(address_id).enqueue(object : retrofit2.Callback<DeleteSavedAddress> {
            override fun onResponse(call: Call<DeleteSavedAddress>, response: Response<DeleteSavedAddress>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onDeleteSavedAddressSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<DeleteSavedAddress>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun requestScheduleCab(locationData: LocationData, callback: HomeContract.OnHomeCompleteListener) {

        var jsonString = ""

        if (locationData.add_stop.isEmpty())
            jsonString = "[]"
        else
        {
            var json = JSONObject()
            json.put("lat",locationData.stop_lat)
            json.put("long",locationData.stop_lng)
            json.put("location",locationData.stop_loc)

            var jsonArray = JSONArray()
            jsonString =  jsonArray.put(json).toString()

        }


        mApi.requestCabSchedule(locationData.source_lat,locationData.source_lng,locationData.source_loc,
            locationData.dest_lat,locationData.dest_lng,locationData.dest_loc,
            locationData.payment_mode,locationData.est_price,jsonString,
            locationData.vehilce_id,locationData.booking_date,locationData.time,locationData.amount,locationData.distance, locationData.duration).enqueue(object : retrofit2.Callback<RequestCabSchedule> {
            override fun onResponse(call: Call<RequestCabSchedule>, response: Response<RequestCabSchedule>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onRequestScheduleCabSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<RequestCabSchedule>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun updateBusinessId(business_id: String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.updateBusinessId(business_id).enqueue(object : retrofit2.Callback<UpdateBusinessId> {
            override fun onResponse(call: Call<UpdateBusinessId>, response: Response<UpdateBusinessId>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onUpdateBusinessIdSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<UpdateBusinessId>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun rideDetails(ride_id: String, callback: HomeContract.OnHomeCompleteListener) {


        mApi.rideDetails(ride_id).enqueue(object : retrofit2.Callback<RideDetails> {
            override fun onResponse(call: Call<RideDetails>, response: Response<RideDetails>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onRideDetailsSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<RideDetails>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getUserRideStatus(screen: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.getUserRideStatus().enqueue(object : retrofit2.Callback<UserRideStatus> {
            override fun onResponse(call: Call<UserRideStatus>, response: Response<UserRideStatus>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetUserRideStatusSucces(screen, it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<UserRideStatus>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })    }

    override fun paymentDone(ride_id: String, card_id: String, rewards_redeemed: String,cvv : String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.paymentDone(ride_id,card_id,rewards_redeemed,cvv).enqueue(object : retrofit2.Callback<PaymentDone> {
            override fun onResponse(call: Call<PaymentDone>, response: Response<PaymentDone>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onPaymentDoneSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<PaymentDone>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun getShareTrip(status: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.getShareTrip(status).enqueue(object : retrofit2.Callback<GetShareTrip> {
            override fun onResponse(call: Call<GetShareTrip>, response: Response<GetShareTrip>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetShareTripSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<GetShareTrip>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun applyPromoCode(code: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.applyPromoCode(code).enqueue(object : retrofit2.Callback<ApplyPromoCode> {
            override fun onResponse(call: Call<ApplyPromoCode>, response: Response<ApplyPromoCode>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onApplyPromoCodeSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ApplyPromoCode>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun getWalletRewards(callback: HomeContract.OnHomeCompleteListener) {

        mApi.getWalletRewards().enqueue(object : retrofit2.Callback<GetWalletRewards> {
            override fun onResponse(call: Call<GetWalletRewards>, response: Response<GetWalletRewards>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onGetWalletRewardsSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<GetWalletRewards>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun shareTrip(friends: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.shareTrip(friends).enqueue(object : retrofit2.Callback<ShareTrip> {
            override fun onResponse(call: Call<ShareTrip>, response: Response<ShareTrip>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onShareTripSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ShareTrip>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun sos(ride_id: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.sos(ride_id).enqueue(object : retrofit2.Callback<Sos> {
            override fun onResponse(call: Call<Sos>, response: Response<Sos>) {

                if(response.isSuccessful)
                    response.body()?.let { callback.onSosSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<Sos>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }

    override fun pickupNotes(ride_id: String, msg: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.pickupNotes(ride_id,msg).enqueue(object : retrofit2.Callback<PickupNotes> {
            override fun onResponse(call: Call<PickupNotes>, response: Response<PickupNotes>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onPickupSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<PickupNotes>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun pickupChange(ride_id: String, lat: String, lng: String, distance: String, amount: String,address : String, callback: HomeContract.OnHomeCompleteListener)
    {

        mApi.pickupChange(ride_id,lat,lng,distance,amount,address).enqueue(object : retrofit2.Callback<PickupChange> {
            override fun onResponse(call: Call<PickupChange>, response: Response<PickupChange>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onPickupChangeSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<PickupChange>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })

    }


    override fun heatMap(from_lat: String, from_long: String, screen: String, callback: HomeContract.OnHomeCompleteListener)
    {

        mApi.heatMap(from_lat,from_long).enqueue(object : retrofit2.Callback<CarRequests> {
            override fun onResponse(call: Call<CarRequests>, response: Response<CarRequests>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onHeatMapSucces(it, screen) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<CarRequests>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })

    }

    override fun tip(ride_id: String, amount: String, card_id: String, cvv: String, callback: HomeContract.OnHomeCompleteListener) {

        mApi.tip(ride_id,amount,cvv).enqueue(object : retrofit2.Callback<TipModel> {
            override fun onResponse(call: Call<TipModel>, response: Response<TipModel>) {


                if(response.isSuccessful)
                    response.body()?.let { callback.onTipSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<TipModel>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })


    }

}