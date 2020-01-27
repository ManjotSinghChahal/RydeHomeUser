package com.example.rydehomeuser.ui.activities.home.fragment.contactList

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getFriendList.GetFriendList
import kotlinx.android.synthetic.main.contactlist_adapter.view.*
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.rydehomeuser.ui.activities.home.Home


class ContactListAdapter(val context: Context, val friendList: GetFriendList,val callback : ContactsSel,val  rel_DoneContactList : RelativeLayout) :
    RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {
    var count: Int = 0


    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contactlist_adapter, container, false))
    }

    override fun getItemCount(): Int {
        return friendList.body.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bindviews(friendList, pos)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBoxContacts = itemView.findViewById(R.id.checkboxContacts) as CheckBox

        fun bindviews(friendList: GetFriendList, pos: Int) {
            itemView.username_contactList.text = friendList.body.get(adapterPosition).name

           /* if (friendList.body.get(adapterPosition).app_user_id.equals("0"))
                checkBoxContacts.visibility = View.GONE*/

            if (friendList.body.get(adapterPosition).contactChecked)
                checkBoxContacts.setChecked(true)
            else
                checkBoxContacts.setChecked(false)


            checkBoxContacts.setOnClickListener {


                if (count < 3) {
                    if (friendList.body.get(adapterPosition).contactChecked) {
                        friendList.body.get(adapterPosition).contactChecked = false
                        checkBoxContacts.setChecked(false)
                        if (count > 0)
                            count = count - 1
                    } else {
                        friendList.body.get(adapterPosition).contactChecked = true
                        checkBoxContacts.setChecked(true)
                        count = count + 1
                    }
                } else {
                    if (friendList.body.get(adapterPosition).contactChecked)
                    {
                        if (count > 0)
                            count = count - 1
                        checkBoxContacts.setChecked(false)
                        friendList.body.get(adapterPosition).contactChecked = false
                    }
                     else
                    {
                        checkBoxContacts.setChecked(false)
                        Toast.makeText(context, "You can select atmost 3 Persons.", Toast.LENGTH_SHORT).show()
                    }

                }


            }

            rel_DoneContactList.setOnClickListener {


                var friend_list : String = ""
                for (p in 0..friendList.body.size-1)
                {

                    if (!friendList.body.get(p).is_user.equals("0"))
                    {
                        if (friendList.body.get(p).contactChecked)
                        {
                            if (friend_list.isEmpty())
                                friend_list = friendList.body.get(p).app_user_id
                            else
                            {
                                friend_list = "${friend_list},${friendList.body.get(p).app_user_id}"
                            }
                        }
                    }

                }

                callback.OnContactsSel(friend_list)
            }

        }
    }

    interface ContactsSel
    {
        fun OnContactsSel(friends : String)
    }

}