package com.example.rydehomeuser.ui.activities.getCards

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.deleteCard.DeleteCard
import com.example.rydehomeuser.data.model.getCard.GetCard
import com.example.rydehomeuser.ui.activities.addCardActivity.AddCardActivity
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.dialogFragment.DeleteCardDialog
import com.example.rydehomeuser.utils.Constants.CARD_ID
import com.example.rydehomeuser.utils.Constants.CVV
import com.example.rydehomeuser.utils.Constants.GET_CARDS
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.PAYMENT_POINTS
import com.example.rydehomeuser.utils.Constants.TIP_TYPE
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.activity_get_cards.*

class GetCards : AppCompatActivity(), HomeContract.GetCardView, GetCardAdapter.DeleteCard,DeleteCardDialog.DeleteCardInterface {



    var recyclerviewCards: RecyclerView? = null
    var presenter: HomeContract.HomePresenter? = null
    var screenEnter : String = ""
    var card_id = ""
   // var cvv = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_cards)


        initialize()
        clickListener()





    }


    fun initialize() {
        presenter = HomePresenterImp(this)
        GlobalHelper.showProgress(this)
        presenter!!.getCard("",GET_CARDS)

        recyclerviewCards = findViewById(R.id.recyclerview_cards) as RecyclerView
        recyclerviewCards?.layoutManager = LinearLayoutManager(this)


        var intent = intent.extras
        intent?.let {

            if (it.containsKey(MAP_ACTIVITY))
                screenEnter = MAP_ACTIVITY
            else if (it.containsKey(PAYMENT_POINTS))
                screenEnter = PAYMENT_POINTS
            else if (it.containsKey(TIP_TYPE))
                screenEnter = TIP_TYPE


        }


       /* val returnIntent = Intent()
        returnIntent.putExtra(FRIEND_IDS, friends)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()*/

    }

    fun clickListener() {

        relBack_getCards.setOnClickListener {
            onBackPressed()
        }

        addCard_getCards.setOnClickListener {
            startActivity(Intent(this, AddCardActivity::class.java))
        }

    }

    override fun onGetCardSuccess(getCard: GetCard) {
        try {
            GlobalHelper.hideProgress()
            getCard.message.let {
          //      GlobalHelper.snackBar(rootGetCard, it)
                if (getCard.code.equals(200))
                {


                    if (getCard.body.size>0)
                    {
                        recyclerviewCards?.adapter = GetCardAdapter(this,getCard,this,screenEnter)

                        for (i in 0..getCard.body.size-1)
                        {
                            if (getCard.body.get(i).selected_card.equals("1"))
                                card_id = getCard.body.get(i).selected_card
                        }

                        addCard_getCards.visibility = View.GONE
                    }
                    else
                    {
                        recyclerviewCards?.adapter = GetCardAdapter(this,getCard,this,screenEnter)

                        addCard_getCards.visibility = View.VISIBLE
                    }

                }

            }
        } catch (ex: Exception) {
          //  Log.e("rcxrx",ex.message)
        }
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let {
                GlobalHelper.snackBar(rootGetCard, it)
            }
        } catch (ex: Exception) {
        }
    }



    override fun onDeleteCardSuccess(deleteCard: DeleteCard) {
        try {
            GlobalHelper.hideProgress()
            deleteCard.message.let { GlobalHelper.snackBar(rootGetCard, it) }
            if (deleteCard.code.equals(200))
            {
                GlobalHelper.hideProgress()
                presenter!!.getCard("",GET_CARDS)
            }


        } catch (ex: Exception) {

        }
    }

    override fun OnDeleteCardSelSuccess(card_id: String,status : String,cvv : String) {


        if (status.equals("del"))
         openDeleteAlert(this,card_id)
        else if (status.equals("check"))
            presenter!!.getCard(card_id,MAP_ACTIVITY)
        else if (status.equals("payment"))
        {
            val returnIntent = Intent()
            returnIntent.putExtra(CARD_ID, card_id)
            returnIntent.putExtra(CVV, cvv)
            setResult(Activity.RESULT_OK, returnIntent)

            super.onBackPressed()
        }

       // cvv = cvv_


    }


    fun openDeleteAlert(context : Context, card_id: String)
    {
        val fragmentManager = supportFragmentManager!!.beginTransaction()
        val delCard = DeleteCardDialog(this,card_id)
        delCard?.show(fragmentManager, "")

    }

    override fun onDelCardInterface(card_id : String) {

        GlobalHelper.showProgress(this)
        presenter!!.deleteCard(card_id)    }


    override fun onBackPressed() {
        super.onBackPressed()

       /* if (screenEnter.equals(PAYMENT_POINTS))
        {

            if (cvv.length==3)
            {
                val returnIntent = Intent()
                returnIntent.putExtra(CARD_ID, card_id)
                returnIntent.putExtra(CVV, cvv)
                setResult(Activity.RESULT_OK, returnIntent)

                super.onBackPressed()
            }
            else{
                GlobalHelper.snackBar(rootGetCard,"Please Enter CVV")
            }
        }
        else
        {
            super.onBackPressed()
        }*/
    }

    override fun onRestart() {
        super.onRestart()

        try {
            presenter!!.getCard("",GET_CARDS)
        }catch (ex : Exception){}
    }



}
