package com.example.rydehomeuser.ui.activities.addCardActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.addCard.AddCard
import com.example.rydehomeuser.data.saveData.saveCardInfo.SaveCardInfo
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.CardValidator
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.activity_add_card.*
import java.util.*

class AddCardActivity : AppCompatActivity(), HomeContract.AddCardView {

    var presenter: HomeContract.HomePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        GlobalHelper.setToolbar(getString(R.string.add_card),homeBackIconVis = true)
        presenter = HomePresenterImp(this)

        clickListener()



    }

    fun clickListener()
    {
        relBack_addCards.setOnClickListener {
            onBackPressed()
        }

       txtview_exp.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence, start: Int, removed: Int, added: Int) {

                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                   txtview_exp.setText(p0.toString() + "/")
                   txtview_exp.setSelection(3)
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                   txtview_exp.setText(p0.toString().replace("/", ""))
                   txtview_exp.setSelection(2)
                }
                else  if (start == 2 && start+added == 3 && p0?.contains('/') == false) {

                    try {
                        txtview_exp.setText(p0.substring(0, 2) + "/" + p0.substring(2, p0.length))
                        txtview_exp.setSelection(4)
                    }catch (ex : Exception){}


                }

            }
        })


       nextAddCard.setOnClickListener {


            val validate = CardValidator()
            // validate.validate("4242424242424242")
            //   Log.e("Rfrtvcrtvc",validate.validate("4213242606391319").toString())

            var strMonth : String = ""
            var strYear : String = ""
            var currentYear : String = ""
            if (txtview_exp.text.toString().trim().length==5)
            {
                strMonth = txtview_exp.text.toString().trim().subSequence(0,2).toString()
                strYear =  txtview_exp.text.toString().trim().subSequence(3,5).toString()

            }

            val yy = Calendar.getInstance().get(Calendar.YEAR)
            if (yy.toString().length==4)
                currentYear = yy.toString().subSequence(2,4).toString()




            if (name_addCard.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootAddCard,"Enter Card holder name")
            else if (accNum_addCard.text.toString().trim().isEmpty() || accNum_addCard.text.toString().trim().length<12)
                GlobalHelper.snackBar(rootAddCard,"Enter Card number")
            else if (!validate.validate(accNum_addCard.text.toString().trim()))
                GlobalHelper.snackBar(rootAddCard,"Enter valid Card number")
            else if (txtview_exp.text.toString().trim().isEmpty() || txtview_exp.text.toString().trim().length<5)
                GlobalHelper.snackBar(rootAddCard,"Enter Expiry date")
            else if (strMonth.toInt()>12)
                GlobalHelper.snackBar(rootAddCard,"Expiry date is invalid")
            else if (strYear.toInt()<currentYear.toInt())
                GlobalHelper.snackBar(rootAddCard,"Expiry date is invalid")
            else if (txtview_cvv.text.toString().trim().isEmpty() || txtview_cvv.text.toString().trim().length<3)
                GlobalHelper.snackBar(rootAddCard,"Enter CVV")
            else if (postCode_addCard.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootAddCard,"Enter Post code")
            else{
                val saveCard = SaveCardInfo()

                saveCard.card_holder_name = name_addCard.text.toString().trim()
                saveCard.card_number = accNum_addCard.text.toString().trim()


                saveCard.card_expiry_month = strMonth
                saveCard.card_expiry_year = strYear
                saveCard.country = countryCode_AddCard.selectedCountryName


                saveCard.cvv = txtview_cvv.text.toString().trim()
                saveCard.postal_code = postCode_addCard.text.toString().trim()
                saveCard.card_type = validate.getCardType(accNum_addCard.text.toString().trim())

                presenter!!.addCard(saveCard)



            }

        }

    }

    override fun onAddCardSuccess(addCard: AddCard) {
        try {
            GlobalHelper.hideProgress()
            addCard.message.let { GlobalHelper.snackBar(rootAddCard, it) }
            onBackPressed()

        } catch (ex: Exception) {
        }    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(rootAddCard, it)
            }
        }catch (ex : Exception) {}    }


}
