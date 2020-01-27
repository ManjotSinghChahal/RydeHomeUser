package com.example.rydehomeuser.ui.activities.getCards

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getCard.Body
import com.example.rydehomeuser.data.model.getCard.GetCard
import com.example.rydehomeuser.utils.Constants.PAYMENT_POINTS
import com.example.rydehomeuser.utils.Constants.TIP_TYPE
import kotlinx.android.synthetic.main.getcard_adapter.view.*
import java.lang.Exception


class GetCardAdapter(
    val context: Context,
    val card: GetCard,
    val callback: DeleteCard,
    val screenEnter: String
) :
    RecyclerView.Adapter<GetCardAdapter.ViewHolder>() {

    var lastSelCardPos : Int = -1
    var lastSelCardView : View? = null
    var lastShownCvv : View? = null

    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.getcard_adapter, container, false))
    }

    override fun getItemCount(): Int {
        return card.body.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bindviews(card.body)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkboxBankDetails = itemView.findViewById(R.id.checkbox_bankDetails) as RadioButton

        fun bindviews(card: List<Body>) {

           /* if (screenEnter.equals(PAYMENT_POINTS))
                itemView.lin_cvv_getCards.visibility = View.VISIBLE
            else
                itemView.lin_cvv_getCards.visibility = View.GONE*/

            itemView.cardType_getCard.text = card.get(adapterPosition).card_type
            val accNum: String = card.get(adapterPosition).card_number
            if (accNum.length>4)
                itemView.accNum_getCard.text = "**** **** **** ${accNum.substring(accNum.length-4,accNum.length) }"

            itemView.deleteCard.setOnClickListener {
               callback.OnDeleteCardSelSuccess(card.get(adapterPosition).id,"del","")
            }

            if (card.get(adapterPosition).selected_card.equals("1"))
            {
                checkboxBankDetails.setChecked(true)

                if (screenEnter.equals(PAYMENT_POINTS) || screenEnter.equals(TIP_TYPE))
                    itemView.lin_cvv_getCards.visibility = View.VISIBLE

                card.get(adapterPosition).cardSel = true
                lastSelCardView = itemView.checkbox_bankDetails
                lastSelCardPos = adapterPosition

                lastShownCvv = itemView.lin_cvv_getCards
            }


            if (card.get(adapterPosition).cardSel)
            {
                checkboxBankDetails.setChecked(true)

                if (screenEnter.equals(PAYMENT_POINTS))
                    itemView.lin_cvv_getCards.visibility = View.VISIBLE
            }
            else
            {
                checkboxBankDetails.setChecked(false)
                if (screenEnter.equals(PAYMENT_POINTS))
                    itemView.lin_cvv_getCards.visibility = View.GONE
            }



            /*itemView.edt_cvv_getCardsAdpt.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })*/

            itemView.relDone_getCardsAdpt.setOnClickListener {
                try {
                    if (itemView.edt_cvv_getCardsAdpt.text.toString().trim().length==3)
                        callback.OnDeleteCardSelSuccess(card.get(adapterPosition).id,"payment",itemView.edt_cvv_getCardsAdpt.text.toString().trim())
                }catch (ex : Exception){}
            }


            checkboxBankDetails.setOnClickListener {

                callback.OnDeleteCardSelSuccess(card.get(adapterPosition).id,"check","")

                //---to deselect last pos---
                if (lastSelCardPos==-1)
                {
                    card.get(adapterPosition).cardSel = true
                    lastSelCardView = itemView.checkbox_bankDetails
                    itemView.checkbox_bankDetails.isChecked = true
                    lastSelCardPos = adapterPosition

                    if (screenEnter.equals(PAYMENT_POINTS))
                        itemView.lin_cvv_getCards.visibility = View.VISIBLE

                    lastShownCvv = itemView.lin_cvv_getCards

                }
                else
                {
                    if (lastSelCardView!=null)
                    {
                        lastSelCardView!!.checkbox_bankDetails.isChecked = false
                        card.get(lastSelCardPos).cardSel = false

                        if (screenEnter.equals(PAYMENT_POINTS))
                            lastShownCvv!!.lin_cvv_getCards.visibility = View.GONE

                    }



                    itemView!!.checkbox_bankDetails.isChecked = true
                    card.get(adapterPosition).cardSel = true
                    lastSelCardView = itemView.checkbox_bankDetails
                    lastSelCardPos = adapterPosition

                    if (screenEnter.equals(PAYMENT_POINTS))
                        itemView!!.lin_cvv_getCards.visibility = View.VISIBLE

                        lastShownCvv = itemView.lin_cvv_getCards
                }


                for (pos in 0..card.size-1)
                {
                //    Log.e("rfrfcrcx",card.get(pos).cardSel.toString())
                }


            }


        }
      }

    interface DeleteCard
    {
        fun OnDeleteCardSelSuccess(card_id : String,sel : String,cvv : String)
    }
    }