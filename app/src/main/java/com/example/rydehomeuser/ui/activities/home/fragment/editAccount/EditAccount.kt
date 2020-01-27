package com.example.rydehomeuser.ui.activities.home.fragment.editAccount


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.editAccount.UpdateProfile
import com.example.rydehomeuser.data.model.getProfile.GetProfile
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.enterMobile.EnterMobile
import com.example.rydehomeuser.ui.activities.signUp.fragments.enterName.EnterName
import com.example.rydehomeuser.utils.*
import com.example.rydehomeuser.utils.Constants.EDITACCOUNT_SCREEN
import com.example.rydehomeuser.utils.Constants.OTP_SCREEN
import com.example.rydehomeuser.utils.GlobalHelper.hideSoftKeyBoard
import com.google.android.material.snackbar.Snackbar
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_edit_account.*
import kotlinx.android.synthetic.main.fragment_edit_account.view.*


class EditAccount : ImagepickerFragment(), HomeContract.EditAccountView {


    var presenter: HomeContract.HomePresenter? = null
    var image_path: String = ""
    var imgviewSignUp: ImageView? = null

    var updateSharedPref: Boolean = false
    var countryCode_editAcc : CountryCodePicker? = null

    var phoneVerified : String = ""
    var country_code : String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_account, container, false)


        initialize(view)
        clickListener(view)





        return view
    }

    fun initialize(view: View) {
        countryCode_editAcc = view.findViewById(R.id.countryCode_editAcc) as CountryCodePicker

        presenter = HomePresenterImp(this)
        presenter?.getProfile(EDITACCOUNT_SCREEN)

        imgviewSignUp = view.findViewById(R.id.imgview_signUp) as ImageView

        GlobalHelper.setToolbar(getString(R.string.edit_account), homeBackIconVis = true)


        val bundle = arguments
        if (bundle!=null && bundle.containsKey(OTP_SCREEN))
        {
             country_code  = bundle.getString("code")
             phoneVerified = bundle.getString("phone")

        }




    }


    fun clickListener(view: View) {


        Home.homeBackIcon.setOnClickListener {
            hideSoftKeyBoard(activity as AppCompatActivity, view.rootEditAccount)
            activity?.let { it.onBackPressed() }
        }

        view.rel_profileImg_signin.setOnClickListener {
            getImage(activity, 0)
        }

        view.submit_signin.setOnClickListener {

            if (Validator.getsInstance().updateProfileValidator(
                    view.edt_fname.text.toString().trim(),
                    view.edt_lname.text.toString().trim(),
                    view.phone_editAcc.text.toString().trim(),
                    view.email_editAcc.text.toString().trim(),
                    view.password_editAcc.text.toString().trim(),
                    rootEditAccount,
                    activity as AppCompatActivity
                )
            ) {
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter?.updateProfile(
                    view.phone_editAcc.text.toString().trim(), view.edt_fname.text.toString().trim(),
                    view.edt_lname.text.toString().trim(), view.password_editAcc.text.toString().trim(),
                    "", countryCode_editAcc!!.selectedCountryCode, view.email_editAcc.text.toString().trim(), image_path
                )
            }
        }


        view.lin_phone_editAcc.setOnClickListener {
            GlobalHelper.replaceFramentAnim(activity as AppCompatActivity, EnterMobile(), R.id.container_map)
        }

        view.phone_editAcc.setOnClickListener {
            GlobalHelper.replaceFramentAnim(activity as AppCompatActivity, EnterMobile(), R.id.container_map)
        }

    }

    override fun selectedImage(imagePath: String?) {
        Glide.with(activity as AppCompatActivity).load(imagePath).into(imgviewSignUp!!)
        image_path = imagePath.toString()

        try {
            view!!.dummyImage_editAccount.visibility = View.GONE
        }catch (ex : Exception){}


    }


    override fun onEditAccountSuccess(updateProfile: UpdateProfile) {

        try {
            GlobalHelper.hideProgress()
            updateProfile.message.let { GlobalHelper.snackBar(view!!.rootEditAccount, it) }

            updateSharedPref = true
            presenter?.getProfile(EDITACCOUNT_SCREEN)

        } catch (ex: Exception) {
        }

    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootEditAccount, it) }
        } catch (ex: Exception) {
        }

    }

    override fun onGetProfileSuccess(getProfile: GetProfile) {


        try {
            GlobalHelper.hideProgress()
            //----- to update sharedPref after change
            if (updateSharedPref) {
                SharedPrefUtil.getInstance()?.saveUserName("${getProfile.body.first_name} ${getProfile.body.last_name}")
                SharedPrefUtil.getInstance()?.saveImage(getProfile.body.photo)

                Home.navUsername.text = "${getProfile.body.first_name} ${getProfile.body.last_name}"
               // Home.nav_headerRatingText.text = GlobalHelper.uptoOneDecimal(getProfile.body.average)

                if (getProfile.body.average.equals("0"))
                    Home.nav_headerRatingText.text = "0"
                else
                    Home.nav_headerRatingText.text = GlobalHelper.uptoOneDecimal(getProfile.body.average)


                if (!SharedPrefUtil.getInstance()?.getImage()?.isEmpty()!!)
                    Glide.with(activity as AppCompatActivity).load(getProfile.body.photo).into(Home.navUseImage)

                updateSharedPref = false

                activity?.let { it.onBackPressed() }

            }


            updateTextView(getProfile)

        } catch (ex: Exception) {
        }

    }

    fun updateTextView(getProfile: GetProfile) {


        view?.edt_fname?.setText(getProfile.body.first_name)
        view?.edt_lname?.setText(getProfile.body.last_name)
        view?.phone_editAcc?.setText("${getProfile.body.phone}")
        // view?.password_editAcc?.setText(getProfile.body.password)
        view?.email_editAcc?.setText(getProfile.body.email)



        try {
            countryCode_editAcc!!.setCountryForPhoneCode(getProfile.body.country.toInt())
        }catch (ex : Exception){}



        if (!phoneVerified.isEmpty())
            view?.phone_editAcc!!.text = phoneVerified
        if (!country_code.isEmpty())
        {
            try {
                countryCode_editAcc!!.setCountryForPhoneCode(country_code.toInt())
            }catch (ex : Exception){}
        }



        imgviewSignUp?.let { Glide.with(activity as AppCompatActivity).load(getProfile.body.photo).into(it) }
        try {
            if (!getProfile.body.photo.isEmpty())
              view!!.dummyImage_editAccount.visibility = View.GONE
        }catch (ex : Exception){}

    }


}
