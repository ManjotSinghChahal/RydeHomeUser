package com.example.rydehomeuser.ui.activities.signUp.fragments.enterName


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.fragments.bussinessId.BussinessID
import com.example.rydehomeuser.utils.Constants.LOGIN_DATA
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_enter_name.view.*
import kotlinx.android.synthetic.main.fragment_otp.view.*

class EnterName : androidx.fragment.app.Fragment() {

    var data: LoginData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_enter_name, container, false)



        initialize(view)
        clickListener(view)


        return view
    }


    fun initialize(view : View)
    {
        SignUp.signUpBackIcon.visibility = View.VISIBLE
        SignUp.signUpCrossIcon.visibility = View.GONE
        SignUp.title_signUp.visibility = View.VISIBLE
        SignUp.title_signUp.text = getString(R.string.sign_up)

    }


    fun clickListener(view: View) {
        view.txt_name_next.setOnClickListener {

            if (view.fname_name.text.toString().trim().isEmpty())
            {
               GlobalHelper.snackBar(view.rootName,getString(R.string.enter_f_name))
            }
            else if (view.lname_name.text.toString().trim().isEmpty())
            {
                GlobalHelper.snackBar(view.rootName,getString(R.string.enter_l_name))
            }
            else
            {
                val bundle = arguments
                if (bundle != null && bundle.containsKey(LOGIN_DATA))
                {
                    data = bundle.getParcelable(LOGIN_DATA)

                    data!!.f_name = view.fname_name.text.toString().trim()
                    data!!.l_name = view.lname_name.text.toString().trim()

                    val bundleNew = Bundle()
                    bundleNew.putParcelable(LOGIN_DATA,data)
                    val frag = BussinessID()
                    frag.arguments = bundleNew
                    GlobalHelper.replaceFragment(activity as AppCompatActivity,frag,R.id.container_signup)
                }


            }


        }

        SignUp.signUpBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }


    }
}
