package com.example.rydehomeuser.ui.activities.contactListActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getFriendList.Body
import com.example.rydehomeuser.data.model.getFriendList.GetFriendList
import com.example.rydehomeuser.data.model.splitFare.SplitFare
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.accountSettings.AccountSettings
import com.example.rydehomeuser.ui.activities.home.fragment.contactList.ContactListAdapter
import com.example.rydehomeuser.ui.models.ContactModel
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.ACCOUNT_SETTING
import com.example.rydehomeuser.utils.Constants.AMOUNT
import com.example.rydehomeuser.utils.Constants.FRIEND_IDS
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.RIDE_ID
import com.example.rydehomeuser.utils.GlobalHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_contact_list.*
import kotlinx.android.synthetic.main.fragment_contact_list.*
import kotlinx.android.synthetic.main.fragment_contact_list.view.*

class   ContactListActivity : AppCompatActivity() , HomeContract.ContactListView , ContactListAdapter.ContactsSel {


    var contactModelArrayList: ArrayList<ContactModel>? = null
    var presenter: HomeContract.HomePresenter? = null
    var recyclerviewContactList : RecyclerView? = null

    var ride_id : String = ""
    var amount : String = ""

    var EnteredFrom : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)


        setValues()
        clickListener()


    }


    fun setValues() {
        presenter = HomePresenterImp(this)
        presenter!!.contactList()

      //  GlobalHelper.setToolbar(getString(R.string.contacts), homeBackIconVis = true, relHomeDone = true)


        var intent = intent.extras
        intent?.let {

            if (it.containsKey(MAP_ACTIVITY))
            {
             ride_id = intent.getString(RIDE_ID)
             amount = intent.getString(AMOUNT)
             EnteredFrom = MAP_ACTIVITY  // for sharing from map Act
            }
            else if (it.containsKey(ACCOUNT_SETTING))
            {
                /*val returnIntent = Intent()
                returnIntent.putExtra(PLACE_NAME, place)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()*/
                EnteredFrom = ACCOUNT_SETTING  // for share trip cont add
            }


        }


        recyclerviewContactList = findViewById(R.id.recyclerview_contactList) as RecyclerView
        recyclerviewContactList?.layoutManager = LinearLayoutManager(this)


    }

    fun clickListener() {
        rel_ContactList.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onContactListSuccess(getFriendList: GetFriendList) {


        val gson : Gson =  GsonBuilder().create()
        var payloadStr : String = gson.toJson(getFriendList)
        Log.e("printResult",payloadStr)

        try {
            var body : ArrayList<Body> = ArrayList()
            for (i in 0..getFriendList.body.size-1)
            {

                if (!getFriendList.body.get(i).app_user_id.equals("0"))
                {

                    val bodyData = Body(
                        getFriendList.body.get(i).app_user_id,
                        getFriendList.body.get(i).contact,
                        getFriendList.body.get(i).created,
                        getFriendList.body.get(i).id,
                        getFriendList.body.get(i).is_blocked,
                        getFriendList.body.get(i).is_user,
                        getFriendList.body.get(i).name,
                        getFriendList.body.get(i).user_id,
                        getFriendList.body.get(i).contactChecked)
                        body.add(bodyData)

                }

            }
            val getFrndList = GetFriendList(body,getFriendList.code,getFriendList.message,getFriendList.success)
            recyclerviewContactList?.adapter =  ContactListAdapter(this, getFrndList,this,rel_DoneContactList)
        }catch (ex : Exception){}

    }

    override fun onFailure(error: String) {
        Log.e("printResult",error)
        try {
            GlobalHelper.snackBar(rootContactList,error)
        }catch (ex : Exception){}
    }

    override fun OnContactsSel(friends: String) {

        if (EnteredFrom.equals(MAP_ACTIVITY))
        {
            GlobalHelper.showProgress(this)
            presenter!!.splitFare(friends,amount,ride_id)
        }
        else if (EnteredFrom.equals(ACCOUNT_SETTING))
        {
            val returnIntent = Intent()
            returnIntent.putExtra(FRIEND_IDS, friends)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

    }

    override fun onSplitFareSuccess(splitFare: SplitFare) {
        try {
            Log.e("printList",splitFare.message)
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootContactListAct,splitFare.message)
            if (splitFare.code.equals(200))
              onBackPressed()

        }catch (ex : Exception){}
    }
}
