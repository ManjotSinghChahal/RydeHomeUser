package com.example.rydehomeuser.ui.baseclasses

interface BaseContract {

    interface BaseOnCompleteListener {
        fun onFailure(error: String)
    }

    interface BaseView {
        fun onFailure(error: String)
    }
}