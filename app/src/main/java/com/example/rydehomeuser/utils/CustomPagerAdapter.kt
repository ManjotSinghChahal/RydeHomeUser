package com.example.rydehomeuser.utils

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.R


 // internal class CustomPagerAdapter( val mContext: Context)  : PagerAdapter() {

/*    override fun instantiateItem(collection: ViewGroup, position: Int): Any {



        var resId = 0
        when (position) {0 -> resId = R.id.linLay_receipt
            1 -> resId = R.id.linLay_receipt
        }
        *//*when (position) {
            0 -> resId = R.id.linLay_receipt
            1 -> resId = R.id.linLay_receipt
        }*//*
      //  return findViewById(resId)

        return 0
    }

    override fun getCount(): Int {
        return 2
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // No super
    }*/
  // }

















/*
class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val modelObject = ModelObject.values()[position]
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(modelObject.getLayoutResId(), collection, false) as ViewGroup
        collection.addView(layout)
        return layout
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return 2
    }

    fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val customPagerEnum = ModelObject.values()[position]
        return mContext.getString(customPagerEnum.getTitleResId())
    }

}*/
