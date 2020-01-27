package com.example.rydehomeuser.ui.activities.home.fragment.wallet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getWalletRewards.GetWalletRewards
import kotlinx.android.synthetic.main.wallet_adpt.view.*


class WalletAdapter(val context: Context, val walletRewards: GetWalletRewards) : RecyclerView.Adapter<WalletAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.wallet_adpt, container, false))
    }

    override fun getItemCount(): Int {
        return walletRewards.body.rides.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindItems()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems() {

            Glide.with(context).load(walletRewards.body.rides.get(adapterPosition).Driver.image).into(itemView.imgview_user_points)

            itemView.driverName_wallet.text = walletRewards.body.rides.get(adapterPosition).Driver.first_name
            itemView.distance_wallet.text = "${context.getString(R.string.distance)} ${walletRewards.body.rides.get(adapterPosition).ride_details.distance}"
            itemView.date_distance.text = "${walletRewards.body.rides.get(adapterPosition).created}"
          //  itemView.date_distance.text = walletRewards.body.get(adapterPosition).created
          //  itemView.time_wallet.text = walletRewards.body.get(adapterPosition).created
            itemView.pickupAddress_wallet.text = walletRewards.body.rides.get(adapterPosition).ride_details.from_address
            itemView.dropAddress_wallet.text = walletRewards.body.rides.get(adapterPosition).ride_details.to_address
            itemView.txtview_earnedPoints.text = walletRewards.body.rides.get(adapterPosition).points_earned

        }

    }
}