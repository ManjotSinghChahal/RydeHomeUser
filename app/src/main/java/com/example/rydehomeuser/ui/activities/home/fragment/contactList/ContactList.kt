package com.example.rydehomeuser.ui.activities.home.fragment.contactList


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getFriendList.GetFriendList
import com.example.rydehomeuser.data.model.splitFare.SplitFare
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.accountSettings.AccountSettings
import com.example.rydehomeuser.ui.models.ContactModel
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.CONTACT_NAME
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_contact_list.view.*


class ContactList : Fragment(), HomeContract.ContactListView , ContactListAdapter.ContactsSel{



    var contactModelArrayList: ArrayList<ContactModel>? = null
    var presenter: HomeContract.HomePresenter? = null
    var recyclerviewContactList : RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)


        setValues(view)
        clickListener(view)


        return view
    }


    fun setValues(view: View) {
        presenter = HomePresenterImp(this)
        presenter!!.contactList()

        GlobalHelper.setToolbar(getString(R.string.contacts), homeBackIconVis = true, relHomeDone = true)


        var bundle = arguments
        bundle?.let {

            if (it.containsKey(MAP_ACTIVITY))
            {

            }
            else
            contactModelArrayList = it.getParcelableArrayList<ContactModel>(Constants.CONTACT_LIST)

        }


         recyclerviewContactList =
            view.findViewById(R.id.recyclerview_contactList) as RecyclerView
        recyclerviewContactList?.layoutManager = LinearLayoutManager(activity)


    }

    fun clickListener(view: View) {
        Home.homeBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        Home.relDoneHome.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CONTACT_NAME, "Bradnon")
            val accountSettings = AccountSettings()
            accountSettings.arguments = bundle
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container_map, accountSettings).addToBackStack(null).commit()


        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Home.relDoneHome.visibility = View.GONE
    }

    override fun onContactListSuccess(getFriendList: GetFriendList) {


        val gson : Gson =  GsonBuilder().create()
        var payloadStr : String = gson.toJson(getFriendList)
        Log.e("printResult",payloadStr)

        try {
           // recyclerviewContactList?.adapter = activity?.let { ContactListAdapter(it, getFriendList,this) }
        }catch (ex : Exception){}

    }

    override fun onFailure(error: String) {
        Log.e("printResult",error)
        try {
            GlobalHelper.snackBar(view!!.rootContactList,error)
            activity?.let {
                it.supportFragmentManager.popBackStack()
               // GlobalHelper.replaceFragment(activity as AppCompatActivity,)
            }
        }catch (ex : Exception){}
    }

    override fun OnContactsSel(friends: String) {
     presenter!!.splitFare("","","")
    }

    override fun onSplitFareSuccess(splitFare: SplitFare) {
        try {
          GlobalHelper.snackBar(view!!.rootContactList,splitFare.message)
        }catch (ex : Exception){}
    }


}
