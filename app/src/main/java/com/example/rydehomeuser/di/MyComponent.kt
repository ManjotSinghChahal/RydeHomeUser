package com.example.rydehomeuser.di

import com.example.rydehomeuser.MainActivity
import dagger.Component
import javax.inject.Singleton


/*@Singleton
@Component(modules = { SharedPrefModule::class })*/
interface MyComponent {
    fun inject(activity: MainActivity)
}