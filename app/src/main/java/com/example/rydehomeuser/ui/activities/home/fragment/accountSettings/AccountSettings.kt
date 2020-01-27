package com.example.rydehomeuser.ui.activities.home.fragment.accountSettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import kotlinx.android.synthetic.main.fragment_account_settings.view.*
import com.example.rydehomeuser.ui.activities.home.Home
import android.graphics.Paint
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import com.bumptech.glide.Glide
import com.example.rydehomeuser.data.model.addAddress.AddAddress
import com.example.rydehomeuser.data.model.deleteSavedAddress.DeleteSavedAddress
import com.example.rydehomeuser.data.model.getSavedAddress.GetSavedAddress
import com.example.rydehomeuser.data.model.getShareTrip.GetShareTrip
import com.example.rydehomeuser.data.model.logout.Logout
import com.example.rydehomeuser.data.model.shareTrip.ShareTrip
import com.example.rydehomeuser.data.model.synContacts.SynContacts
import com.example.rydehomeuser.data.saveData.saveAddress.SaveAddress
import com.example.rydehomeuser.ui.activities.contactListActivity.ContactListActivity
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.contactList.ContactList
import com.example.rydehomeuser.ui.activities.home.fragment.editAccount.EditAccount
import com.example.rydehomeuser.ui.activities.savedPlaces.SavedPlaces
import com.example.rydehomeuser.ui.activities.searchPlacesActivity.SearchPlacesActivity
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.dialogFragment.LogoutDialog
import com.example.rydehomeuser.ui.models.ContactModel
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTING
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTINGS
import com.example.rydehomeuser.utils.Constants.CONTACT_LIST
import com.example.rydehomeuser.utils.Constants.FRIEND_IDS
import com.example.rydehomeuser.utils.Constants.HOME_ADDRESS_TYPE
import com.example.rydehomeuser.utils.Constants.PLACE_NAME
import com.example.rydehomeuser.utils.Constants.WORK_ADDRESS_TYPE
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.collections.ArrayList


class AccountSettings : androidx.fragment.app.Fragment() , HomeContract.AccountSettingView, LogoutDialog.LogoutInterface{



    private var contactModelArrayList: ArrayList<ContactModel>? = null
    private var tagList = arrayListOf<String>()
    var presenter: HomeContract.HomePresenter? = null

    lateinit var  chipGroup : ChipGroup
    var  chip1 : Chip? = null
    var  chip2 : Chip? = null
    var  chip3 : Chip? = null

    val HOME_ADDRESS : Int = 1
    val WORK_ADDRESS : Int = 2
    val CONTACT_TYPE : Int = 3

    var homeAddId : String = ""
    var workAddId : String = ""
    var frndId1 : String = ""
    var frndId2 : String = ""
    var frndId3 : String = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account_settings, container, false)



        initialize(view)
        clickListener(view)




        return view
    }


    fun initialize(view : View)
    {
        GlobalHelper.setToolbar(getString(R.string.account_settings),homeCrossIconVis = true)
        presenter = HomePresenterImp(this)
        presenter!!.getSavedAddress(ACCOUNT_SETTINGS)
        presenter!!.getShareTrip("")

        chipGroup = view.findViewById(R.id.tag_group) as ChipGroup
        chip1 = view.findViewById(R.id.chip1) as Chip
        chip2 = view.findViewById(R.id.chip2) as Chip
        chip3 = view.findViewById(R.id.chip3) as Chip
        // addChip()


        val bundle = arguments
        bundle?.let {
            if (it.containsKey(Constants.CONTACT_NAME))
            {
                val value = it.getString(Constants.CONTACT_NAME)
              //  addChip(value)
            }


        }

       /* loadContacts()
        val gson = Gson()
        val element = gson.toJsonTree(contactModelArrayList, object : TypeToken<List<ContactModel>>() {}.type)
        if (!element.isJsonArray) { throw Exception() as Throwable
        }
        val jsonArray = element.asJsonArray
        Log.e("printContacts",jsonArray.toString())
        presenter!!.synContacts(ACCOUNT_SETTINGS,jsonArray.toString())*/



        view.select_contacts_setting.paintFlags = view.select_contacts_setting.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG
        view.add_more_setting.paintFlags = view.add_more_setting.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG


        view.username_accSettings.text = SharedPrefUtil.getInstance()?.getUserName()
        Glide.with(activity as AppCompatActivity).load(SharedPrefUtil.getInstance()?.getImage()).into(view.userimage_accSettings)

    }


    fun clickListener(view : View)
    {
        view.rel_switch_left_setting.setOnClickListener {
            presenter!!.getShareTrip("1")
        }

        view.rel_switch_right_setting.setOnClickListener {
            presenter!!.getShareTrip("0")
        }

        chip1!!.setOnCloseIconClickListener {
          //  chipGroup.removeView(chip1 as View)

            if(!frndId2.isEmpty() && !frndId3.isEmpty())
            {
                val  frnd_id = "$frndId2,$frndId3"
                presenter!!.shareTrip(frnd_id)
            }
            else if(!frndId2.isEmpty())
            {
                presenter!!.shareTrip(frndId2)
            }
            else if(!frndId3.isEmpty())
            {
                presenter!!.shareTrip(frndId3)
            }
            else if(frndId2.isEmpty() && frndId3.isEmpty())
            {
                presenter!!.shareTrip("")
            }
        }

        chip2!!.setOnCloseIconClickListener {
          //  chipGroup.removeView(chip2 as View)
            if(!frndId1.isEmpty() && !frndId3.isEmpty())
            {
                val  frnd_id = "$frndId1,$frndId3"
                presenter!!.shareTrip(frnd_id)
            }
            else if(!frndId1.isEmpty())
            {
                presenter!!.shareTrip(frndId1)
            }
            else if(!frndId3.isEmpty())
            {
                presenter!!.shareTrip(frndId3)
            }
            else if(frndId1.isEmpty() && frndId3.isEmpty())
            {
                presenter!!.shareTrip("")
            }
        }

        chip3!!.setOnCloseIconClickListener {
         //   chipGroup.removeView(chip3 as View)
            if(!frndId1.isEmpty() && !frndId2.isEmpty())
            {
                val  frnd_id = "$frndId1,$frndId2"
                presenter!!.shareTrip(frnd_id)
            }
            else if(!frndId1.isEmpty())
            {
                presenter!!.shareTrip(frndId1)
            }
            else if(!frndId2.isEmpty())
            {
                presenter!!.shareTrip(frndId2)
            }
            else if(frndId1.isEmpty() && frndId2.isEmpty())
            {
                presenter!!.shareTrip("")
            }
        }


        /*view.txtview_addWork.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,Tip()).addToBackStack(null).commit()
        }*/


        Home.homeCrossIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        view.lin_addHome.setOnClickListener {
            activity!!.startActivityForResult(Intent(activity as AppCompatActivity, SearchPlacesActivity::class.java), HOME_ADDRESS)
       }

        view.lin_addWorkAddress.setOnClickListener {
            activity!!.startActivityForResult(Intent(activity as AppCompatActivity, SearchPlacesActivity::class.java), WORK_ADDRESS)
        }

        view.signout.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager!!.beginTransaction()
            val logoutDialog = LogoutDialog(this)
            logoutDialog?.show(fragmentManager, "")
        }



        view.select_contacts_setting.setOnClickListener {

            val intent = Intent(activity as AppCompatActivity, ContactListActivity::class.java)
            intent.putExtra(ACCOUNT_SETTING, ACCOUNT_SETTING)
            activity!!.startActivityForResult(intent, CONTACT_TYPE)

           /* view.select_contacts_setting.visibility = View.GONE
            view.add_more_setting.visibility = View.VISIBLE


            val bundle  = Bundle()
            bundle.putParcelableArrayList(CONTACT_LIST,contactModelArrayList)
            val contactList : ContactList = ContactList()
            contactList.arguments = bundle*/
          //  (context   as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map, ContactList()).addToBackStack(null).commit()
        }

        view.add_more_setting.setOnClickListener {
            val bundle  = Bundle()
            bundle.putParcelableArrayList(CONTACT_LIST,contactModelArrayList)
            val contactList : ContactList = ContactList()
            contactList.arguments = bundle
         //   (context   as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map, ContactList()).addToBackStack(null).commit()
        }


        view.lin_userdetail_accSett.setOnClickListener {
            GlobalHelper.replaceFragment(activity as AppCompatActivity,EditAccount(),R.id.container_map)
        }

        view.imgCross_homeAddress.setOnClickListener {
            presenter!!.deleteSavedAddress(homeAddId)
            view!!.txtview_addHomeAddress.text = getString(R.string.add_home)
            view.imgCross_homeAddress.visibility = View.GONE
        }

        view.imgCross_workAddress.setOnClickListener {
            presenter!!.deleteSavedAddress(workAddId)
            view!!.txtview_addWork.text = getString(R.string.add_work)
            view.imgCross_workAddress.visibility = View.GONE
        }

        view.moreplaces_AccSettings.setOnClickListener {
            activity?.let {   it.startActivity(Intent(it, SavedPlaces::class.java)) }
        }




    }


    //----------------------------load contacts---------------------------
    fun loadContacts()  {

     contactModelArrayList = arrayListOf<ContactModel>()

     val phones = activity?.contentResolver?.query(
         ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
         null,
         null,
         null,
         ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
     )
     while (phones!!.moveToNext()) {
         var name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
         var contact = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

         if (!contact.isEmpty())
         {
             if (contact.contains(" "))
             {
                 contact = contact.replace(" ","")
             }
             val contactModel = ContactModel(name,contact)
             contactModelArrayList!!.add(contactModel)

         }
         Log.e("name------->>", name + "  " + contact)

     }
     phones.close()
 }








  fun addChip(value: String)
  {
      var chip = Chip(activity)
      chip.text = value
      chip.isCloseIconEnabled = true
      chip.setCloseIconTintResource(R.color.colorWhite)
      chip.setTextColor(resources.getColor(R.color.colorWhite))
      chip.chipBackgroundColor = getColorStateList(activity as AppCompatActivity, R.color.colorAppTheme)
      chipGroup.addView(chip)




      chip.setOnCloseIconClickListener {
          chipGroup.removeView(chip)
      }
  }

    override fun onSynContactsSuccess(synContacts: SynContacts) {
     }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let {
                GlobalHelper.snackBar(view!!.rootAccSettings, it)
            }
        } catch (ex: Exception) {
        }
     }

    override fun onLogoutSuccess(logout: Logout) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootAccSettings,logout.message)
            if (logout.code.equals(200))
            {
                SharedPrefUtil.getInstance()?.setLogin(false)
                SharedPrefUtil.getInstance()?.clear()


                val intent = Intent(activity, SignUp::class.java)
                startActivity(intent)
                activity?.finish()
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }

        } catch (ex: Exception) {
        }
    }
    override fun onLogoutInterface() {
        GlobalHelper.showProgress(activity as AppCompatActivity)
        presenter!!.logout(ACCOUNT_SETTING)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    //    super.onActivityResult(requestCode, resultCode, data); // comment this unless you want to pass your result to the activity.


        if (data!=null)
        {

            if (resultCode == Activity.RESULT_OK) {

                if (requestCode == HOME_ADDRESS) {
                    val address = data!!.getStringExtra(PLACE_NAME)
                    view!!.txtview_addHomeAddress.text = address

                    val latLng = GlobalHelper.getLatLngFromAddress(activity as AppCompatActivity, address)
                    val saveAddress = SaveAddress()
                    saveAddress.type = HOME_ADDRESS_TYPE
                    saveAddress.name = ""
                    saveAddress.lat = latLng!!.latitude.toString()
                    saveAddress.long = latLng!!.longitude.toString()
                    saveAddress.location = address

                    presenter!!.addAddress(saveAddress)

                    view!!.imgCross_homeAddress.visibility = View.VISIBLE

                } else if (requestCode == WORK_ADDRESS) {
                    val address = data!!.getStringExtra(PLACE_NAME)
                    view!!.txtview_addWork.text = address

                    val latLng = GlobalHelper.getLatLngFromAddress(activity as AppCompatActivity, address)
                    val saveAddress = SaveAddress()
                    saveAddress.type = WORK_ADDRESS_TYPE
                    saveAddress.name = ""
                    saveAddress.lat = latLng!!.latitude.toString()
                    saveAddress.long = latLng!!.longitude.toString()
                    saveAddress.location = address

                    presenter!!.addAddress(saveAddress)

                    view!!.imgCross_workAddress.visibility = View.VISIBLE
                }

                else if (requestCode == CONTACT_TYPE) {
                    val frnd_ids = data!!.getStringExtra(FRIEND_IDS)
                    presenter!!.shareTrip(frnd_ids)
                }

            }

        }


    }

    override fun onAddAddressSuccess(addAddress: AddAddress) {

    }

    override fun onGetSavedAddressSuccess(getSavedAddress: GetSavedAddress) {

        try {


           if (getSavedAddress.code.equals(200))
           {


               if (!getSavedAddress.body.home.address.isEmpty())
               {
                   view!!.txtview_addHomeAddress.text = getSavedAddress.body.home.address
                   view!!.imgCross_homeAddress.visibility = View.VISIBLE
                   homeAddId = getSavedAddress.body.home.id
               }

               if (!getSavedAddress.body.work.address.isEmpty())
               {
                   view!!.txtview_addWork.text = getSavedAddress.body.work.address
                   view!!.imgCross_workAddress.visibility = View.VISIBLE
                   workAddId = getSavedAddress.body.work.id
               }
           }
        }catch (ex : Exception){}
    }

    override fun onDeleteSavedAddressSuccess(deleteSavedAddress: DeleteSavedAddress) {

    }

    override fun onShareTripSuccess(shareTrip: ShareTrip) {

        try {
            if (shareTrip.code.equals(200))
                presenter!!.getShareTrip("")
        }catch (ex : Exception){}
    }


    override fun onGetShareTripSuccess(getShareTrip: GetShareTrip) {

        try {



            if (getShareTrip.code.equals(200))
            {
                frndId1 = ""
                frndId2 = ""
                frndId3 = ""

               if (getShareTrip.body.status.equals("0"))
                  offShareTrip(view!!)
               else if (getShareTrip.body.status.equals("1"))
                  onShareTrip(view!!)


                if(getShareTrip.body.status.equals("0"))
                {
                    offShareTrip(view!!)
                }
                else if(getShareTrip.body.status.equals("1"))
                {
                    onShareTrip(view!!)

                    if(getShareTrip.body.User.size>0)
                    {
                        /*for (i in 0..getShareTrip.body.User.size-1)
                        {
                            addChip(getShareTrip.body.User.get(i).name)
                        }*/
                        view!!.select_contacts_setting.visibility = View.GONE

                        if (getShareTrip.body.User.size==1)
                        {
                            chip1!!.visibility = View.VISIBLE
                            chip1!!.text = getShareTrip.body.User.get(0).name
                            frndId1 = getShareTrip.body.User.get(0).id

                            chip2!!.visibility = View.GONE
                            chip3!!.visibility = View.GONE
                        }
                        else if (getShareTrip.body.User.size==2)
                        {
                            chip1!!.visibility = View.VISIBLE
                            chip1!!.text = getShareTrip.body.User.get(0).name
                            frndId1 = getShareTrip.body.User.get(0).id
                            chip2!!.visibility = View.VISIBLE
                            chip2!!.text = getShareTrip.body.User.get(1).name
                            frndId2 = getShareTrip.body.User.get(1).id

                            chip3!!.visibility = View.GONE
                        }
                        else if (getShareTrip.body.User.size==3)
                        {
                            chip1!!.visibility = View.VISIBLE
                            chip1!!.text = getShareTrip.body.User.get(0).name
                            frndId1 = getShareTrip.body.User.get(0).id
                            chip2!!.visibility = View.VISIBLE
                            chip2!!.text = getShareTrip.body.User.get(1).name
                            frndId2 = getShareTrip.body.User.get(1).id
                            chip3!!.visibility = View.VISIBLE
                            chip3!!.text = getShareTrip.body.User.get(2).name
                            frndId3 = getShareTrip.body.User.get(2).id
                        }

                    }
                    else
                    {

                        view!!.select_contacts_setting.visibility = View.VISIBLE
                        chip1!!.visibility = View.GONE
                        chip2!!.visibility = View.GONE
                        chip3!!.visibility = View.GONE
                    }

                }
            }
        }catch (ex : Exception){}
    }


    fun onShareTrip(view : View)
    {
        GlobalHelper.changeBackground(view.rel_toggle,resources.getColor(R.color.colorAppTheme ))
        view.img_switch_left_setting.visibility = View.VISIBLE
        view.img_switch_right_setting.visibility = View.GONE
    }

    fun offShareTrip(view : View)
    {
        GlobalHelper.changeBackground(view.rel_toggle,resources.getColor(R.color.gray))
        view!!.select_contacts_setting.visibility = View.GONE
        view.img_switch_left_setting.visibility = View.GONE
        view.img_switch_right_setting.visibility = View.VISIBLE

        chip1!!.visibility = View.GONE
        chip2!!.visibility = View.GONE
        chip3!!.visibility = View.GONE
    }

}
