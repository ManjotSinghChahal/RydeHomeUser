package com.example.rydehomeuser.ui.activities.signUp.fragments.chooseType



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.fragments.mobileNumber.MobileNumber
import com.example.rydehomeuser.ui.activities.signUp.fragments.socialLogin.SocialLoginActivity
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_choose_type.view.*



class ChooseType : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_choose_type, container, false)

       clickListener(view)

        SignUp.signUpBackIcon.visibility = View.GONE
        SignUp.signUpCrossIcon.visibility = View.GONE
        SignUp.title_signUp.visibility = View.GONE

        clickListener(view)




        return view
    }






    fun clickListener(view : View)
    {

        view.txtview_socialAccount.setOnClickListener {
          //  (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_signup,SocialLogin()).addToBackStack(null).commit()
           activity?.let {   it.startActivity(Intent(it, SocialLoginActivity::class.java)) }
        }

        view.lin_mobileNum_chooseType.setOnClickListener {
            GlobalHelper.replaceFragment(activity as AppCompatActivity,MobileNumber(),R.id.container_signup)
        }
        view.txt_phone_chooseType.setOnClickListener {
            GlobalHelper.replaceFragment(activity as AppCompatActivity,MobileNumber(),R.id.container_signup)
        }

    }


}
