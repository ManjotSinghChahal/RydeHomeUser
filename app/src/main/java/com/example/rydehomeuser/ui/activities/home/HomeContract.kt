package com.example.rydehomeuser.ui.activities.home

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
import com.example.rydehomeuser.ui.baseclasses.BaseContract


interface HomeContract : BaseContract
{


    interface HomePresenter {
        fun help(ride_id: String)
        fun getProfile(screen : String)
        fun updateProfile(phone: String, first_name : String, last_name : String, password: String, city : String, country : String, email : String, image : String)
        fun otpVerifiy(otp: String,phone_number : String)
        fun sendOtp(c_code : String,phone_number : String)
        fun synContacts(screen : String,contacts : String)
        fun contactList()
        fun getTrips(screen : String)
        fun getCarTypes(locationData: LocationData)
        fun requestCab(screen : String, locationData: LocationData)
        fun requestScheduleCab(locationData: LocationData)
        fun confirmPickup(ride_id : String, lat : String, lng : String)
        fun splitFare(friends : String, amount : String, ride_id : String)
        fun splitRequest(ride_id : String,status : String)
        fun cancelRide(trip_id : String, amount : String,cancel_reason : String)
        fun rating(ride_id : String, rating : String,review : String,type : String)
        fun addCard(saveCardInfo: SaveCardInfo)
        fun getCard(card_id : String,screen : String)
        fun deleteCard(card_id : String)
        fun logout(screen : String)
        fun addAddress(saveAddress: SaveAddress)
        fun getSavedAddress(screen : String)
        fun deleteSavedAddress(address_id : String)
        fun tripShared(address_id : String)
        fun updateBusinessId(business_id : String)
        fun rideDetails(ride_id : String)
        fun getUserRideStatus(screen : String)
        fun paymentDone(ride_id : String, card_id: String, rewards_redeemed: String,cvv : String)
        fun getShareTrip(status : String)
        fun applyPromoCode(code : String)
        fun getWalletRewards()
        fun shareTrip(friends : String)
        fun sos(ride_id : String)
        fun pickupNotes(ride_id : String, msg : String)
        fun tip(ride_id : String, amount : String, card_id : String, cvv: String)
        fun heatMap(from_lat : String, from_long : String, screen : String)
        fun pickupChange(ride_id : String, lat : String,lng : String, distance : String, amount : String,address: String)


    }

    interface HomeInteractor {
        fun help(ride_id: String, callback: OnHomeCompleteListener)
        fun getProfile(screen : String,callback: OnHomeCompleteListener)
        fun updateProfile(phone: String, first_name : String, last_name : String, password: String, city : String, country : String, email : String, image : String, callback: OnHomeCompleteListener)
        fun otpVerifiy(otp: String, phone_number : String, callback: OnHomeCompleteListener)
        fun sendOtp(c_code : String,phone_number : String, callback: OnHomeCompleteListener)
        fun synContacts(screen : String,contacts : String, callback: OnHomeCompleteListener)
        fun contactList(callback: OnHomeCompleteListener)
        fun getTrips(screen : String, callback: OnHomeCompleteListener)
        fun getCarTypes(locationData: LocationData, callback: OnHomeCompleteListener)
        fun requestCab(screen : String,locationData: LocationData, callback: OnHomeCompleteListener)
        fun requestScheduleCab(locationData: LocationData, callback: OnHomeCompleteListener)
        fun confirmPickup(ride_id : String, lat : String, lng : String, callback: OnHomeCompleteListener)
        fun splitFare(friends : String, amount : String, ride_id : String, callback: OnHomeCompleteListener)
        fun splitRequest(ride_id : String,status : String, callback: OnHomeCompleteListener)
        fun cancelRide(trip_id : String, amount : String,cancel_reason : String, callback: OnHomeCompleteListener)
        fun rating(ride_id : String, rating : String,review : String,type : String, callback: OnHomeCompleteListener)
        fun addCard(saveCardInfo: SaveCardInfo, callback: OnHomeCompleteListener)
        fun getCard(card_id : String,screen : String,callback: OnHomeCompleteListener)
        fun deleteCard(card_id : String, callback: OnHomeCompleteListener)
        fun logout(screen : String, callback: OnHomeCompleteListener)
        fun addAddress(saveAddress: SaveAddress,callback: OnHomeCompleteListener)
        fun getSavedAddress(screen : String, callback: OnHomeCompleteListener)
        fun deleteSavedAddress(address_id : String,callback: OnHomeCompleteListener)
        fun updateBusinessId(business_id : String, callback: OnHomeCompleteListener)
        fun rideDetails(ride_id : String, callback: OnHomeCompleteListener)
        fun getUserRideStatus(screen : String, callback: OnHomeCompleteListener)
        fun paymentDone(ride_id : String, card_id: String, rewards_redeemed: String,cvv : String, callback: OnHomeCompleteListener)
        fun getShareTrip(status : String, callback: OnHomeCompleteListener)
        fun applyPromoCode(code : String, callback: OnHomeCompleteListener)
        fun getWalletRewards(callback: OnHomeCompleteListener)
        fun shareTrip(friends : String, callback: OnHomeCompleteListener)
        fun sos(ride_id : String, callback: OnHomeCompleteListener)
        fun pickupNotes(ride_id : String, msg : String, callback: OnHomeCompleteListener)
        fun heatMap(from_lat : String, from_long : String, screen : String, callback: OnHomeCompleteListener)
        fun tip(ride_id : String, amount : String, card_id : String, cvv: String, callback: OnHomeCompleteListener)
        fun pickupChange(ride_id : String, lat : String,lng : String, distance : String, amount : String,address: String, callback: OnHomeCompleteListener)
    }


    interface OnHomeCompleteListener : BaseContract.BaseOnCompleteListener
    {
        fun onHelpSucces(ride_id: HelpModel)
        fun onEditAccountSucces(updateProfile: UpdateProfile)
        fun onGetProfileSucces(screen : String,getProfile: GetProfile)
        fun onVerifyOtpSucces(otpVerified: OtpVerified)
        fun onSendOtpSucces(phoneVerify: SendOtp)
        fun onSynContactsSucces(screen : String,synContacts: SynContacts)
        fun onContactListSucces(getFriendList: GetFriendList)
        fun onGetTripsSucces(screen : String, getTrips: GetTrips)
        fun onGetCarTypesSucces(getCarTypes: GetCarTypes)
        fun onRequestCabSucces(screen : String, requestCab: RequestCab)
        fun onRequestScheduleCabSucces(requestCabSchedule: RequestCabSchedule)
        fun onConfirmPickupSucces(confirmPickup: ConfirmPickup)
        fun onSplitFareSucces(splitFare: SplitFare)
        fun onSplitRequestSucces(splitRequest: SplitRequest)
        fun onCancelRideSucces(cancelRide: CancelRide)
        fun onRatingSucces(ratingModel: RatingModel)
        fun onAddcardSucces(addCard: AddCard)
        fun onGetCardSucces(screen : String, getCard: GetCard)
        fun onDeleteCardSucces(deleteCard: DeleteCard)
        fun onLogoutSucces(screen : String, logout: Logout)
        fun onAddAddressSucces(addAddress: AddAddress)
        fun onGetSavedAddressSucces(screen : String, getSavedAddress: GetSavedAddress)
        fun onDeleteSavedAddressSucces(deleteSavedAddress: DeleteSavedAddress)
        fun onUpdateBusinessIdSucces(updateBusinessId: UpdateBusinessId)
        fun onRideDetailsSucces(rideDetails: RideDetails)
        fun onGetUserRideStatusSucces(screen : String, userRideStatus: UserRideStatus)
        fun onPaymentDoneSucces(paymentDone: PaymentDone)
        fun onGetShareTripSucces(getShareTrip: GetShareTrip)
        fun onApplyPromoCodeSucces(applyPromoCode: ApplyPromoCode)
        fun onGetWalletRewardsSucces(getWalletRewards: GetWalletRewards)
        fun onShareTripSucces(shareTrip: ShareTrip)
        fun onSosSucces(sos: Sos)
        fun onPickupSucces(pickupNotes: PickupNotes)
        fun onPickupChangeSucces(pickupChange: PickupChange)
        fun onHeatMapSucces(carRequests: CarRequests,screen : String)
        fun onTipSucces(tipModel: TipModel)





    }

    interface HelpView : BaseContract.BaseView
    {
        fun onHelpSuccess(ride_id: HelpModel)
        fun onGetTripsSuccess(getTrips: GetTrips)
    }
    interface EditAccountView : BaseContract.BaseView
    {
        fun onEditAccountSuccess(updateProfile: UpdateProfile)
        fun onGetProfileSuccess(getProfile: GetProfile)
    }
    interface VerifyOtpView : BaseContract.BaseView
    {
        fun onVerifyOtpSuccess(otpVerified: OtpVerified)
    }
    interface SendOtpView : BaseContract.BaseView
    {
        fun onSendOtpSuccess(phoneVerify: SendOtp)
    }

    interface HomeView : BaseContract.BaseView
    {
        fun onGetProfileSuccess(getProfile: GetProfile)
        fun onSynContactsSuccess(synContacts: SynContacts)
        fun onLogoutSuccess(logout: Logout)
    }

    interface AccountSettingView : BaseContract.BaseView
    {
        fun onSynContactsSuccess(synContacts: SynContacts)
        fun onLogoutSuccess(logout: Logout)
        fun onAddAddressSuccess(addAddress: AddAddress)
        fun onGetSavedAddressSuccess(getSavedAddress: GetSavedAddress)
        fun onDeleteSavedAddressSuccess(deleteSavedAddress: DeleteSavedAddress)
        fun onGetShareTripSuccess(getShareTrip: GetShareTrip)
        fun onShareTripSuccess(shareTrip: ShareTrip)
    }

    interface ContactListView : BaseContract.BaseView
    {
        fun onContactListSuccess(getFriendList: GetFriendList)
        fun onSplitFareSuccess(splitFare: SplitFare)
    }

    interface GetTripsView : BaseContract.BaseView
    {
        fun onGetTripsSuccess(getTrips: GetTrips)
    }

    interface MapView : BaseContract.BaseView
    {
        fun onGetCarTypesSuccess(getCarTypes: GetCarTypes)
        fun onRequestCabSuccess(requestCab: RequestCab)
        fun onRequestCabScheduleSuccess(requestCabSchedule: RequestCabSchedule)
        fun onConfirmPickupSuccess(confirmPickup: ConfirmPickup)
        fun onSplitRequestSuccess(splitRequest: SplitRequest)
        fun onUserRideStatusSuccess(userRideStatus: UserRideStatus)
        fun onSosSuccess(sos: Sos)
        fun onPickupNotesSuccess(pickupNotes: PickupNotes)
        fun onLogoutSuccess(logout: Logout)
        fun onPickupChangeSuccess(pickupChange: PickupChange)
        fun onHeatMApSuccess(carRequests: CarRequests)
    }
    interface CancelRideView : BaseContract.BaseView
    {
        fun onCancelRideSuccess(cancelRide: CancelRide)
    }
    interface RatingView : BaseContract.BaseView
    {
        fun onRatingSuccess(ratingModel: RatingModel)
    }
    interface AddCardView : BaseContract.BaseView
    {
        fun onAddCardSuccess(addCard: AddCard)
    }
    interface GetCardView : BaseContract.BaseView
    {
        fun onGetCardSuccess(getCard: GetCard)
        fun onDeleteCardSuccess(deleteCard: DeleteCard)
    }

    interface PaymentView : BaseContract.BaseView
    {
        fun onUpdateBusinessIdSuccess(updateBusinessId: UpdateBusinessId)
    }

    interface ApplyPromoCodeView : BaseContract.BaseView
    {
        fun onApplyPromoCodeSuccess(applyPromoCode: ApplyPromoCode)
    }

    interface PaymentPonitsView : BaseContract.BaseView
    {
        fun onPaymentPonitsSuccess(rideDetails: RideDetails)
        fun onPaymentDoneSuccess(paymentDone: PaymentDone)
        fun onGetCardSuccess(getCard: GetCard)
    }
    interface HomeFragmentView : BaseContract.BaseView
    {
        fun onUserRideStatusSuccess(userRideStatus: UserRideStatus)
        fun onHeatMApSuccess(carRequests: CarRequests)
    }

    interface WalletRewardsView : BaseContract.BaseView
    {
        fun onWalletRewardSuccess(getWalletRewards: GetWalletRewards)
    }

    interface SearchPlacesView : BaseContract.BaseView
    {
        fun onGetSavedAddressSuccess(getSavedAddress: GetSavedAddress)
    }

    interface TipView : BaseContract.BaseView
    {
        fun onTipSuccess(tipModel: TipModel)
    }

}