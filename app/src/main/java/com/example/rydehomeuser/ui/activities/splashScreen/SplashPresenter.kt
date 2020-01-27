package com.example.rydehomedriver.ui.activities.splashScreen

class SplashPresenter(var splashview: SplashView, val splashinteractor: SplashInteractor) : SplashInteractor.OnStart {


    fun onTimeOut() {

    splashview.onHandleTimeout()
    }

    override fun onCounterStart() {
    // splashview.onHandleTimeout()
    }



}