package com.example.rydehomeuser.ui.activities.signUp.fragments.socialLogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.fbModel.FbModel
import com.example.rydehomeuser.data.saveData.saveFbModel.SaveFbModel
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.SignupContract
import com.example.rydehomeuser.ui.activities.signUp.SignupPresenterImp
import com.example.rydehomeuser.utils.Constants.DEVICE_TYPE
import com.example.rydehomeuser.utils.FbLogin
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import kotlinx.android.synthetic.main.activity_social_login.*
import kotlinx.android.synthetic.main.fragment_social_login.view.*

class SocialLoginActivity : AppCompatActivity() , FbLogin.FbLoginInterface, SignupContract.SocialLoginView{



    private lateinit var mFbModule: FbLogin
    var presenter: SignupContract.SignupPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_login)

        mFbModule = FbLogin(this)
        initialize()
        clickListener()
    }



    fun initialize() {
        SignUp.signUpBackIcon.visibility = View.VISIBLE
        SignUp.signUpCrossIcon.visibility = View.GONE
        SignUp.title_signUp.visibility = View.GONE
        SignUp.title_signUp.text = ""

        presenter = SignupPresenterImp(this)

    }

    fun clickListener() {
        relBack_socialLogin.setOnClickListener {
            onBackPressed()
        }

        fbLogin.setOnClickListener {
            mFbModule.fbLogin(this)
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //If signin with facebook
        mFbModule.mCallbackManager.onActivityResult(requestCode, resultCode, data)



    }

    override fun fbLoginResult(socialId: String, email: String, name: String, profilePic: String) {
       val fbBundle = SaveFbModel()

        fbBundle.device_token = SharedPrefUtil!!.getInstance()?.getDeviceToken().toString()
        fbBundle.device_type = DEVICE_TYPE
        fbBundle.image = profilePic
        fbBundle.social_id = socialId
        fbBundle.social_type = "F"
        fbBundle.name = name
        presenter!!.fbLogin(fbBundle)

    }

    override fun onSocialLoginSuccess(fbModel: FbModel) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootSocialLogin, fbModel.message)
            if (fbModel.code.equals(200))
            {
                SharedPrefUtil.getInstance()?.setLogin(true)
                SharedPrefUtil.getInstance()?.saveAuthToken(fbModel.body.authorization_key)
                SharedPrefUtil.getInstance()?.saveUserId(fbModel.body.id)
                ActivityCompat.finishAffinity(this)
                 startActivity(Intent(this, Home::class.java))
                 finish()
                 overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        } catch (ex: Exception) {
        }
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(rootSocialLogin, it) }
        } catch (ex: Exception) {
        }
    }

}
