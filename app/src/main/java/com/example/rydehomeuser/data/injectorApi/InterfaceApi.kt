package com.app.passerby.data.injectorApi


import com.example.rydehomeuser.data.model.addAddress.AddAddress
import com.example.rydehomeuser.data.model.addCard.AddCard
import com.example.rydehomeuser.data.model.applyPromoCode.ApplyPromoCode
import com.example.rydehomeuser.data.model.cancelRide.CancelRide
import com.example.rydehomeuser.data.model.carRequests.CarRequests
import com.example.rydehomeuser.data.model.confirmPickup.ConfirmPickup
import com.example.rydehomeuser.data.model.deleteCard.DeleteCard
import com.example.rydehomeuser.data.model.deleteSavedAddress.DeleteSavedAddress
import com.example.rydehomeuser.data.model.editAccount.UpdateProfile
import com.example.rydehomeuser.data.model.fbModel.FbModel
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
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.model.pickupNotes.PickupNotes
import com.example.rydehomeuser.data.model.pickup_change.PickupChange
import com.example.rydehomeuser.data.model.ratingModel.RatingModel
import com.example.rydehomeuser.data.model.register.Register
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
import com.example.rydehomeuser.data.saveData.saveAddress.SaveAddress
import com.example.rydehomeuser.data.saveData.saveCardInfo.SaveCardInfo
import com.example.rydehomeuser.data.saveData.saveFbModel.SaveFbModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface InterfaceApi {




    @FormUrlEncoded
    @POST("phone_verify")
    fun phoneVerify(
        @Field("country_code") country_code: String,
        @Field("phone_no") phone_no: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Call<PhoneVerify>

    @FormUrlEncoded
    @POST("send_otp")
    fun sendOtp(@Field("country_code") country_code: String,@Field("phone_no") phone_no: String): Call<SendOtp>

    @FormUrlEncoded
    @POST("help")
    fun help(@Field("ride_id") ride_id: String): Call<HelpModel>

    @FormUrlEncoded
    @POST("user_signup")
    fun register(@Field("country") country: String,
                 @Field("phone") phone: String,
                 @Field("first_name") first_name: String,
                 @Field("last_name") last_name: String,
                 @Field("device_type") device_type: String,
                 @Field("device_token") device_token: String,
                 @Field("business_id") business_id: String,
                 @Field("lat") lat: String,
                 @Field("long") long: String
    ): Call<Register>


    @Multipart
    @POST("User_update_profile")
    fun updateProfile1(
        @Part("phone") phone: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("email") email: RequestBody
//        @Part image: MultipartBody.Part
    ): Call<UpdateProfile>

    @Multipart
    @POST("User_update_profile")
    fun updateProfile(
        @Part("phone") phone: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<UpdateProfile>

    @FormUrlEncoded
    @POST("verify_otp")
    fun otpVerify(@Field("model") model: String,
                 @Field("otp") otp: String,
                 @Field("type") type: String,
                 @Field("phone_number") phone_number: String,
                 @Field("country_code") country_code: String
    ): Call<OtpVerified>


    @POST("get_profile")
    fun getProfile(): Call<GetProfile>

    @POST("socail_login")
    fun fblogin(@Body saveFbModel: SaveFbModel): Call<FbModel>

    @FormUrlEncoded
    @POST("sync_contacts")
    fun synContacts(@Field("contacts") contacts: String): Call<SynContacts>

    @FormUrlEncoded
    @POST("user_confirm_pickup")
    fun confirmPickup(@Field("ride_id") ride_id: String,
                      @Field("lat") lat: String,
                      @Field("long") long: String): Call<ConfirmPickup>

    @FormUrlEncoded
    @POST("split_friends")
    fun splitFare(@Field("friends") friends: String,
                      @Field("amount") amount: String,
                      @Field("ride_id") ride_id: String): Call<SplitFare>

    @FormUrlEncoded
    @POST("split_request_approve")
    fun splitRequest(@Field("ride_id") ride_id: String,
                  @Field("status") status: String): Call<SplitRequest>

    @FormUrlEncoded
    @POST("ride_details")
    fun rideDetails(@Field("ride_id") ride_id: String): Call<RideDetails>

    @FormUrlEncoded
    @POST("user_accept_reject_ride")
    fun cancelRide(@Field("trip_id") trip_id: String,
                  @Field("amount") amount: String,
                  @Field("cancel_reason") cancel_reason: String): Call<CancelRide>

    @FormUrlEncoded
    @POST("ride_review")
    fun rating(@Field("ride_id") ride_id: String,
                   @Field("rating") rating: String,
                   @Field("review") review: String,
                   @Field("type") type : String): Call<RatingModel>

    @GET("get_friends")
    fun getFriendList(): Call<GetFriendList>

    @GET("user_ride_status")
    fun getUserRideStatus(): Call<UserRideStatus>

    @POST("user_add_card")
    fun addCard(@Body saveCard : SaveCardInfo): Call<AddCard>

    @POST("user_add_address")
    fun addAddress(@Body saveAddress: SaveAddress): Call<AddAddress>

    @GET("user_get_address")
    fun getSavedAddress(): Call<GetSavedAddress>

    @GET("get_trips")
    fun getTrips(): Call<GetTrips>

    @FormUrlEncoded
    @POST("user_get_card")
    fun getCards(@Field("card_id") card_id : String): Call<GetCard>

    @GET("get_rewards_history")
    fun getWalletRewards(): Call<GetWalletRewards>

    @FormUrlEncoded
    @POST("user_logout")
    fun logout(@Field("user_id") driver_id : String): Call<Logout>

    @FormUrlEncoded
    @POST("share_trip")
    fun shareTrip(@Field("friends") friends : String): Call<ShareTrip>

    @FormUrlEncoded
    @POST("user_sos_notify")
    fun sos(@Field("ride_id") ride_id : String): Call<Sos>

    @FormUrlEncoded
    @POST("user_pickup_notes")
    fun pickupNotes(@Field("trip_id") trip_id : String,@Field("message") message : String): Call<PickupNotes>

    @FormUrlEncoded
    @POST("user_change_pickup_point")
    fun pickupChange(@Field("ride_id") ride_id : String,@Field("lat") lat : String, @Field("long") long : String,
                     @Field("distance") distance : String,@Field("amount") amount : String,@Field("address") address : String): Call<PickupChange>

    @FormUrlEncoded
    @POST("heat_map_data")
    fun heatMap(@Field("from_lat") from_lat : String, @Field("from_long") from_long : String): Call<CarRequests>


    @FormUrlEncoded
    @POST("user_ride_tip")
    fun tip(@Field("ride_id") ride_id : String, @Field("amount") amount : String,
            @Field("cvv") cvv : String): Call<TipModel>


    @FormUrlEncoded
    @POST("user_share_trip_on_off")
    fun getShareTrip(@Field("status") status : String): Call<GetShareTrip>


    @FormUrlEncoded
    @POST("user_apply_promocode")
    fun applyPromoCode(@Field("promo_code") promo_code : String): Call<ApplyPromoCode>


    @FormUrlEncoded
    @POST("user_ride_payment")
    fun paymentDone(@Field("ride_id") ride_id : String,@Field("card_id") card_id : String,@Field("rewards_redeemed") rewards_redeemed : String,@Field("cvv") cvv : String): Call<PaymentDone>

    @FormUrlEncoded
    @POST("user_remove_card")
    fun deleteCard(@Field("card_id") card_id : String): Call<DeleteCard>

    @FormUrlEncoded
    @POST("user_delete_address")
    fun deleteSavedAddress(@Field("address_id") address_id : String): Call<DeleteSavedAddress>

    @FormUrlEncoded
    @POST("User_update_business_id")
    fun updateBusinessId(@Field("business_id") business_id : String): Call<UpdateBusinessId>


    @FormUrlEncoded
    @POST("get_car_type")
    fun getCarTypes(
        @Field("source_latitude") source_latitude: String,
        @Field("source_longitude") source_longitude: String,
        @Field("source_location") source_location: String,
        @Field("destination_latitude") destination_latitude: String,
        @Field("destination_longitude") destination_longitude: String,
        @Field("destination_location") destination_location: String
    ): Call<GetCarTypes>


    @FormUrlEncoded
    @POST("ride_request")
    fun requestCab(
        @Field("source_latitude") source_latitude: String,
        @Field("source_longitude") source_longitude: String,
        @Field("source_location") source_location: String,
        @Field("destination_latitude") destination_latitude: String,
        @Field("destination_longitude") destination_longitude: String,
        @Field("destination_location") destination_location: String,
        @Field("payment_mode") payment_mode: String,
        @Field("estimate_price") estimate_price: String,
        @Field("add_stop") add_stop: String,
        @Field("vehicle_type_id") vehicle_type_id: String,
        @Field("booking_date") booking_date: String,
        @Field("time") time: String,
        @Field("amount") amount: String,
        @Field("distance") distance: String,
        @Field("previous_ride_id") previous_ride_id: String
    ): Call<RequestCab>


    @FormUrlEncoded
    @POST("ride_request_planned")
    fun requestCabSchedule(
        @Field("source_latitude") source_latitude: String,
        @Field("source_longitude") source_longitude: String,
        @Field("source_location") source_location: String,
        @Field("destination_latitude") destination_latitude: String,
        @Field("destination_longitude") destination_longitude: String,
        @Field("destination_location") destination_location: String,
        @Field("payment_mode") payment_mode: String,
        @Field("estimate_price") estimate_price: String,
        @Field("add_stop") add_stop: String,
        @Field("vehicle_type_id") vehicle_type_id: String,
        @Field("booking_date") booking_date: String,
        @Field("time") time: String,
        @Field("amount") amount: String,
        @Field("distance") distance: String,
        @Field("duration") duration: String
    ): Call<RequestCabSchedule>

}