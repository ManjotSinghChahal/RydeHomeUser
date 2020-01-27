package com.example.rydehomeuser.ui.activities.home.fragment.addCard


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.addCard.AddCard
import com.example.rydehomeuser.data.saveData.saveCardInfo.SaveCardInfo
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.CardValidator
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_addcard.view.*
import kotlinx.android.synthetic.main.fragment_help.view.*
import java.lang.Exception
import java.util.*


class Addcard : Fragment(), HomeContract.AddCardView{


    var presenter: HomeContract.HomePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_addcard, container, false)


          GlobalHelper.setToolbar(getString(R.string.add_card),homeBackIconVis = true)
          presenter = HomePresenterImp(this)

         clickListener(view)









        return view
    }

     fun clickListener(view : View)
    {
      Home.homeBackIcon.setOnClickListener {
          activity?.let { it.onBackPressed() }
      }

        view.txtview_exp.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence, start: Int, removed: Int, added: Int) {

                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    view.txtview_exp.setText(p0.toString() + "/")
                    view.txtview_exp.setSelection(3)
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                    view.txtview_exp.setText(p0.toString().replace("/", ""))
                    view.txtview_exp.setSelection(2)
                }
                else  if (start == 2 && start+added == 3 && p0?.contains('/') == false) {

                    try {
                        view.txtview_exp.setText(p0.substring(0, 2) + "/" + p0.substring(2, p0.length))
                        view.txtview_exp.setSelection(4)
                    }catch (ex : Exception){}


                }

            }
        })


        view.nextAddCard.setOnClickListener {


            val validate = CardValidator()
           // validate.validate("4242424242424242")
          //   Log.e("Rfrtvcrtvc",validate.validate("4213242606391319").toString())

            var strMonth : String = ""
            var strYear : String = ""
            var currentYear : String = ""
            if (view.txtview_exp.text.toString().trim().length==5)
            {
                 strMonth = view.txtview_exp.text.toString().trim().subSequence(0,2).toString()
                 strYear = view.txtview_exp.text.toString().trim().subSequence(3,5).toString()

            }

            val yy = Calendar.getInstance().get(Calendar.YEAR)
            if (yy.toString().length==4)
                currentYear = yy.toString().subSequence(2,4).toString()




            if (view.name_addCard.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter Card holder name")
            else if (view.accNum_addCard.text.toString().trim().isEmpty() || view.accNum_addCard.text.toString().trim().length<12)
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter Card number")
            else if (!validate.validate(view.accNum_addCard.text.toString().trim()))
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter valid Card number")
            else if (view.txtview_exp.text.toString().trim().isEmpty() || view.txtview_exp.text.toString().trim().length<5)
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter Expiry date")
            else if (strMonth.toInt()>12)
                GlobalHelper.snackBar(view!!.rootAddCard,"Expiry date is invalid")
            else if (strYear.toInt()<currentYear.toInt())
                GlobalHelper.snackBar(view!!.rootAddCard,"Expiry date is invalid")
            else if (view.txtview_cvv.text.toString().trim().isEmpty() || view.txtview_cvv.text.toString().trim().length<3)
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter CVV")
            else if (view.postCode_addCard.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(view!!.rootAddCard,"Enter Post code")
            else{
                val saveCard = SaveCardInfo()

                saveCard.card_holder_name = view.name_addCard.text.toString().trim()
                saveCard.card_number = view.accNum_addCard.text.toString().trim()


                saveCard.card_expiry_month = strMonth
                saveCard.card_expiry_year = strYear
                saveCard.country = view.countryCode_AddCard.selectedCountryName


                saveCard.cvv = view.txtview_cvv.text.toString().trim()
                saveCard.postal_code = view.postCode_addCard.text.toString().trim()
                saveCard.card_type = validate.getCardType(view.accNum_addCard.text.toString().trim())
                
                presenter!!.addCard(saveCard)



            }

        }

    }

    override fun onAddCardSuccess(addCard: AddCard) {
        try {
            GlobalHelper.hideProgress()
            addCard.message.let { GlobalHelper.snackBar(view!!.rootAddCard, it) }
            activity?.onBackPressed()

        } catch (ex: Exception) {
        }    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootAddCard, it)
            }
        }catch (ex : Exception) {}    }



}
