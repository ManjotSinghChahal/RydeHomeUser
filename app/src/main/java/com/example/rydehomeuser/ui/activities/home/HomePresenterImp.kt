package com.example.rydehomeuser.ui.activities.home

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
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTING
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTINGS
import com.example.rydehomeuser.utils.Constants.GET_CARDS
import com.example.rydehomeuser.utils.Constants.HOMEFRAGMENT_SCREEN
import com.example.rydehomeuser.utils.Constants.HOME_ACTIVITY
import com.example.rydehomeuser.utils.Constants.HOME_SCREEN
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.PAYMENT_POINTS
import com.example.rydehomeuser.utils.Constants.REQUEST_TYPE
import com.example.rydehomeuser.utils.Constants.SAVED_PLACES
import com.example.rydehomeuser.utils.Constants.SEARCH_PLACES
import com.example.rydehomeuser.utils.Constants.YOUR_TRIPS


class HomePresenterImp() : HomeContract.HomePresenter, HomeContract.OnHomeCompleteListener {



    lateinit var interactor: HomeContract.HomeInteractor
    var helpView: HomeContract.HelpView? = null
    var editAccountView: HomeContract.EditAccountView? = null
    var otpVerifyView: HomeContract.VerifyOtpView? = null
    var sendVerifyView: HomeContract.SendOtpView? = null
    var homeView: HomeContract.HomeView? = null
    var accountSettingView: HomeContract.AccountSettingView? = null
    var contactListView: HomeContract.ContactListView? = null
    var getTripsView: HomeContract.GetTripsView? = null
    var mapView: HomeContract.MapView? = null
    var cancelRideView: HomeContract.CancelRideView? = null
    var ratingView: HomeContract.RatingView? = null
    var addCardView: HomeContract.AddCardView? = null
    var getCardView: HomeContract.GetCardView? = null
    var paymentView: HomeContract.PaymentView? = null
    var paymentPointsView: HomeContract.PaymentPonitsView? = null
    var homeFragmentView: HomeContract.HomeFragmentView? = null
    var ApplyPromoCodeView: HomeContract.ApplyPromoCodeView? = null
    var walletRewardsView: HomeContract.WalletRewardsView? = null
    var searchPlacesView: HomeContract.SearchPlacesView? = null
    var tipView: HomeContract.TipView? = null

    init {

    }


    //------------------constructor for each view----------------------
    constructor(help_view: HomeContract.HelpView) : this() {
        interactor = HomeInteractorImp()
        helpView = help_view
    }

    constructor(editAccount_view: HomeContract.EditAccountView) : this() {
        interactor = HomeInteractorImp()
        editAccountView = editAccount_view
    }

    constructor(otpVerify_view: HomeContract.VerifyOtpView) : this() {
        interactor = HomeInteractorImp()
        otpVerifyView = otpVerify_view
    }

    constructor(sendVerify_view: HomeContract.SendOtpView) : this() {
        interactor = HomeInteractorImp()
        sendVerifyView = sendVerify_view
    }

    constructor(home_view: HomeContract.HomeView) : this() {
        interactor = HomeInteractorImp()
        homeView = home_view
    }

    constructor(contactList_view: HomeContract.ContactListView) : this() {
        interactor = HomeInteractorImp()
        contactListView = contactList_view
    }

    constructor(accSettings_view: HomeContract.AccountSettingView) : this() {
        interactor = HomeInteractorImp()
        accountSettingView = accSettings_view
    }

    constructor(getTrips_view: HomeContract.GetTripsView) : this() {
        interactor = HomeInteractorImp()
        getTripsView = getTrips_view
    }

    constructor(map_view: HomeContract.MapView) : this() {
        interactor = HomeInteractorImp()
        mapView = map_view
    }

    constructor(cancelRide_view: HomeContract.CancelRideView) : this() {
        interactor = HomeInteractorImp()
        cancelRideView = cancelRide_view
    }

    constructor(rating_view: HomeContract.RatingView) : this() {
        interactor = HomeInteractorImp()
        ratingView = rating_view
    }

    constructor(addCard_view: HomeContract.AddCardView) : this() {
        interactor = HomeInteractorImp()
        addCardView = addCard_view
    }

    constructor(getCard_view: HomeContract.GetCardView) : this() {
        interactor = HomeInteractorImp()
        getCardView = getCard_view
    }

    constructor(payment_view: HomeContract.PaymentView) : this() {
        interactor = HomeInteractorImp()
        paymentView = payment_view
    }

    constructor(paymentPoints_view: HomeContract.PaymentPonitsView) : this() {
        interactor = HomeInteractorImp()
        paymentPointsView = paymentPoints_view
    }

    constructor(homeFragment_view: HomeContract.HomeFragmentView) : this() {
        interactor = HomeInteractorImp()
        homeFragmentView = homeFragment_view
    }

    constructor(ApplyPromoCode_view: HomeContract.ApplyPromoCodeView) : this() {
        interactor = HomeInteractorImp()
        ApplyPromoCodeView = ApplyPromoCode_view
    }

    constructor(walletRewards_view: HomeContract.WalletRewardsView) : this() {
        interactor = HomeInteractorImp()
        walletRewardsView = walletRewards_view
    }

    constructor(searchPlaces_view: HomeContract.SearchPlacesView) : this() {
        interactor = HomeInteractorImp()
        searchPlacesView = searchPlaces_view
    }

    constructor(tip_view: HomeContract.TipView) : this() {
        interactor = HomeInteractorImp()
        tipView = tip_view
    }





    //--------------passing data to interactor----------------------


    override fun help(ride_id: String) {
        if (App.hasNetwork()) {
            interactor.help(ride_id, this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun getTrips(screen : String) {
        if (App.hasNetwork()) {
            interactor.getTrips(screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun splitFare(friends: String, amount: String, ride_id: String) {
        if (App.hasNetwork()) {
            interactor.splitFare(friends,amount,ride_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }




    override fun updateProfile(
        phone: String, first_name: String, last_name: String, password: String, city: String,
        country: String, email: String, image: String
    ) {
        if (App.hasNetwork()) {
            interactor.updateProfile(phone, first_name, last_name, password, city, country, email, image, this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun getProfile(screen: String) {
        if (App.hasNetwork()) {
            interactor.getProfile(screen, this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }


    override fun otpVerifiy(otp: String, phone_number: String) {
        if (App.hasNetwork()) {
            interactor.otpVerifiy(otp, phone_number, this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }


    override fun sendOtp(c_code : String,phone_number: String) {
        if (App.hasNetwork()) {
            interactor.sendOtp(c_code ,phone_number, this)
        }
    }

    override fun synContacts(screen : String,contacts: String) {
        if (App.hasNetwork()) {
            interactor.synContacts(screen,contacts, this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }


    override fun contactList() {
        if (App.hasNetwork()) {
            interactor.contactList(this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun getCarTypes(locationData: LocationData) {
        if (App.hasNetwork()) {
            interactor.getCarTypes(locationData,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun requestCab(screen : String,locationData: LocationData) {
        if (App.hasNetwork()) {
            interactor.requestCab(screen,locationData,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }     }

    override fun confirmPickup(ride_id: String, lat: String, lng: String) {
        if (App.hasNetwork()) {
            interactor.confirmPickup(ride_id,lat,lng,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }     }


    override fun splitRequest(ride_id: String, status: String) {
        if (App.hasNetwork()) {
            interactor.splitRequest(ride_id,status,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun cancelRide(trip_id: String, amount: String, cancel_reason: String) {
        if (App.hasNetwork()) {
            interactor.cancelRide(trip_id,amount,cancel_reason,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun rating(ride_id: String, rating: String, review: String, type: String) {

        if (App.hasNetwork()) {
            interactor.rating(ride_id,rating,review,type,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun addCard(saveCardInfo: SaveCardInfo) {
        if (App.hasNetwork()) {
            interactor.addCard(saveCardInfo,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }     }
    override fun getCard(card_id : String, screen : String) {
        if (App.hasNetwork()) {
            interactor.getCard(card_id,screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }


    override fun deleteCard(card_id: String) {
        if (App.hasNetwork()) {
            interactor.deleteCard(card_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun logout(screen : String) {
        if (App.hasNetwork()) {
            interactor.logout(screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun addAddress(saveAddress: SaveAddress) {
        if (App.hasNetwork()) {
            interactor.addAddress(saveAddress,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun getSavedAddress(screen : String) {
        if (App.hasNetwork()) {
            interactor.getSavedAddress(screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun deleteSavedAddress(address_id: String) {
        if (App.hasNetwork()) {
            interactor.deleteSavedAddress(address_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun requestScheduleCab(locationData: LocationData) {
        if (App.hasNetwork()) {
            interactor.requestScheduleCab(locationData,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun updateBusinessId(business_id: String) {
        if (App.hasNetwork()) {
            interactor.updateBusinessId(business_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }



    override fun tripShared(address_id: String) {

    }

    override fun rideDetails(ride_id: String) {
        if (App.hasNetwork()) {
            interactor.rideDetails(ride_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }     }

    override fun getUserRideStatus(screen: String) {
        if (App.hasNetwork()) {
            interactor.getUserRideStatus(screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun paymentDone(ride_id: String, card_id: String, rewards_redeemed: String,cvv : String) {
        if (App.hasNetwork()) {
            interactor.paymentDone(ride_id,card_id,rewards_redeemed,cvv,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun getShareTrip(status: String) {
        if (App.hasNetwork()) {
            interactor.getShareTrip(status,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun applyPromoCode(code: String) {
        if (App.hasNetwork()) {
            interactor.applyPromoCode(code,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }


    override fun getWalletRewards() {
        if (App.hasNetwork()) {
            interactor.getWalletRewards(this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }     }

    override fun shareTrip(friends: String) {
        if (App.hasNetwork()) {
            interactor.shareTrip(friends,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun sos(ride_id: String) {
        if (App.hasNetwork()) {
            interactor.sos(ride_id,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun pickupNotes(ride_id: String, msg: String) {
        if (App.hasNetwork()) {
            interactor.pickupNotes(ride_id,msg,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }


    override fun pickupChange(ride_id: String, lat: String, lng: String, distance: String, amount: String,address: String) {
        if (App.hasNetwork()) {
            interactor.pickupChange(ride_id,lat,lng,distance,amount,address,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun heatMap(from_lat: String, from_long: String, screen: String) {
        if (App.hasNetwork()) {
            interactor.heatMap(from_lat,from_long,screen,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun tip(ride_id: String, amount: String, card_id: String, cvv: String) {
        if (App.hasNetwork()) {
            interactor.tip(ride_id,amount,card_id,cvv,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }





    //---------------------onApi-success-----------------------------


    override fun onHelpSucces(ride_id: HelpModel) {
        ride_id?.let { helpView?.onHelpSuccess(it) }
    }

    override fun onEditAccountSucces(updateProfile: UpdateProfile) {
        updateProfile?.let { editAccountView?.onEditAccountSuccess(it) }
    }

    override fun onGetProfileSucces(screen: String, getProfile: GetProfile) {
        if (screen.equals(HOME_SCREEN))
            getProfile?.let { homeView?.onGetProfileSuccess(it) }
        else
            getProfile?.let { editAccountView?.onGetProfileSuccess(it) }

    }

    override fun onVerifyOtpSucces(otpVerified: OtpVerified) {
        otpVerified?.let { otpVerifyView?.onVerifyOtpSuccess(it) }
    }

    override fun onSendOtpSucces(phoneVerify: SendOtp) {
        phoneVerify?.let { sendVerifyView?.onSendOtpSuccess(it) }
    }

    override fun onSynContactsSucces(screen: String,synContacts: SynContacts) {
        if (screen.equals(ACCOUNT_SETTINGS))
        synContacts?.let { accountSettingView?.onSynContactsSuccess(it) }
        else if (screen.equals(HOME_SCREEN))
            synContacts?.let { homeView?.onSynContactsSuccess(it) }
    }



    override fun onAddAddressSucces(addAddress: AddAddress) {
        addAddress?.let { accountSettingView?.onAddAddressSuccess(it) }    }

    override fun onContactListSucces(getFriendList: GetFriendList) {
        getFriendList?.let { contactListView?.onContactListSuccess(it) }
    }

    override fun onGetTripsSucces(screen : String, getTrips: GetTrips) {
        if (screen.equals(YOUR_TRIPS))
        getTrips?.let { getTripsView?.onGetTripsSuccess(it) }
        else
        getTrips?.let { helpView?.onGetTripsSuccess(it) }
    }

    override fun onGetCarTypesSucces(getCarTypes: GetCarTypes) {
        getCarTypes?.let { mapView?.onGetCarTypesSuccess(it) }    }

    override fun onRequestCabSucces(screen : String, requestCab: RequestCab) {
      //  if (screen.equals(REQUEST_TYPE))
         requestCab?.let { mapView?.onRequestCabSuccess( it) }     }

    override fun onConfirmPickupSucces(confirmPickup: ConfirmPickup) {
        confirmPickup?.let { mapView?.onConfirmPickupSuccess(it) }      }

    override fun onSplitFareSucces(splitFare: SplitFare) {
        splitFare?.let { contactListView?.onSplitFareSuccess(it) }    }

    override fun onSplitRequestSucces(splitRequest: SplitRequest) {
        splitRequest?.let { mapView?.onSplitRequestSuccess(it) }     }

    override fun onCancelRideSucces(cancelRide: CancelRide) {
        cancelRide?.let { cancelRideView?.onCancelRideSuccess(it) }    }

    override fun onRatingSucces(ratingModel: RatingModel) {
        ratingModel?.let { ratingView?.onRatingSuccess(it) }    }

    override fun onAddcardSucces(addCard: AddCard) {
        addCard?.let { addCardView?.onAddCardSuccess(it) }     }

    override fun onGetCardSucces(screen : String, getCard: GetCard) {
        if (screen.equals(GET_CARDS))
         getCard?.let { getCardView?.onGetCardSuccess(it) }
        else if (screen.equals(PAYMENT_POINTS))
            getCard?.let { paymentPointsView?.onGetCardSuccess(it) }
    }

    override fun onDeleteCardSucces(deleteCard: DeleteCard) {
        deleteCard?.let { getCardView?.onDeleteCardSuccess(it) }    }

    override fun onLogoutSucces(screen: String, logout: Logout) {
        if (screen.equals(ACCOUNT_SETTING))
          logout?.let { accountSettingView?.onLogoutSuccess(it) }
        else if (screen.equals(HOME_ACTIVITY))
          logout?.let { homeView?.onLogoutSuccess(it) }
        else if (screen.equals(MAP_ACTIVITY))
            logout?.let { mapView?.onLogoutSuccess(it) }
    }

    override fun onGetSavedAddressSucces(screen : String, getSavedAddress: GetSavedAddress) {
        if (screen.equals(ACCOUNT_SETTINGS))
          getSavedAddress?.let { accountSettingView?.onGetSavedAddressSuccess(it) }
        else if (screen.equals(SEARCH_PLACES))
            getSavedAddress?.let { searchPlacesView?.onGetSavedAddressSuccess(it) }
        else if (screen.equals(SAVED_PLACES))
            getSavedAddress?.let { accountSettingView?.onGetSavedAddressSuccess(it) }

    }

    override fun onDeleteSavedAddressSucces(deleteSavedAddress: DeleteSavedAddress) {
        deleteSavedAddress?.let { accountSettingView?.onDeleteSavedAddressSuccess(it) }     }

    override fun onRequestScheduleCabSucces(requestCab: RequestCabSchedule) {
        requestCab?.let { mapView?.onRequestCabScheduleSuccess(it) }    }

    override fun onUpdateBusinessIdSucces(updateBusinessId: UpdateBusinessId) {
        updateBusinessId?.let { paymentView?.onUpdateBusinessIdSuccess(it) }     }

    override fun onRideDetailsSucces(rideDetails: RideDetails) {
        rideDetails?.let { paymentPointsView?.onPaymentPonitsSuccess(it) }     }

    override fun onGetUserRideStatusSucces(screen: String, userRideStatus: UserRideStatus) {
        if (screen.equals(MAP_ACTIVITY))
          userRideStatus?.let { mapView?.onUserRideStatusSuccess(it) }
        else if (screen.equals(HOMEFRAGMENT_SCREEN))
            userRideStatus?.let { homeFragmentView?.onUserRideStatusSuccess(it) }
    }

    override fun onPaymentDoneSucces(paymentDone: PaymentDone) {
        paymentDone?.let { paymentPointsView?.onPaymentDoneSuccess(it) }      }

    override fun onGetShareTripSucces(getShareTrip: GetShareTrip) {
        getShareTrip?.let { accountSettingView?.onGetShareTripSuccess(it) }    }

    override fun onApplyPromoCodeSucces(applyPromoCode: ApplyPromoCode) {
        applyPromoCode?.let { ApplyPromoCodeView?.onApplyPromoCodeSuccess(it) }    }

    override fun onGetWalletRewardsSucces(getWalletRewards: GetWalletRewards) {
        getWalletRewards?.let { walletRewardsView?.onWalletRewardSuccess(it) }      }

    override fun onShareTripSucces(shareTrip: ShareTrip) {
        shareTrip?.let { accountSettingView?.onShareTripSuccess(it) }     }

    override fun onSosSucces(sos: Sos) {
        sos?.let { mapView?.onSosSuccess(it) }    }

    override fun onPickupSucces(pickupNotes: PickupNotes) {
        pickupNotes?.let { mapView?.onPickupNotesSuccess(it) }     }

    override fun onPickupChangeSucces(pickupChange: PickupChange) {
        pickupChange?.let { mapView?.onPickupChangeSuccess(it) }
    }

    override fun onHeatMapSucces(carRequests: CarRequests, screen: String) {
        if (screen.equals(MAP_ACTIVITY))
            carRequests?.let { mapView?.onHeatMApSuccess(it) }
        else if (screen.equals(HOMEFRAGMENT_SCREEN))
            carRequests?.let { homeFragmentView?.onHeatMApSuccess(it) }
    }

    override fun onTipSucces(tipModel: TipModel) {
        tipModel?.let { tipView?.onTipSuccess(it) }    }


    //-----------------on api failure-----------------------
    override fun onFailure(error: String) {

        if (helpView != null)
            helpView?.onFailure(error)
        else if (editAccountView != null)
            editAccountView?.onFailure(error)
        else if (otpVerifyView != null)
            otpVerifyView?.onFailure(error)
        else if (sendVerifyView != null)
            sendVerifyView?.onFailure(error)
        else if (homeView != null)
            homeView?.onFailure(error)
        else if (accountSettingView != null)
            accountSettingView?.onFailure(error)
        else if (contactListView != null)
            contactListView?.onFailure(error)
        else if (getTripsView != null)
            contactListView?.onFailure(error)
        else if (mapView != null)
            mapView?.onFailure(error)
        else if (cancelRideView != null)
            cancelRideView?.onFailure(error)
        else if (addCardView != null)
            addCardView?.onFailure(error)
        else if (paymentPointsView != null)
            paymentPointsView?.onFailure(error)
        else if (ApplyPromoCodeView != null)
            ApplyPromoCodeView?.onFailure(error)
        else if (walletRewardsView != null)
            walletRewardsView?.onFailure(error)
        else if (tipView != null)
            tipView?.onFailure(error)

    }


}