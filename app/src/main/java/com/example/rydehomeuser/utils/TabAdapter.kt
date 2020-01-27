package com.example.rydehomeuser.utils

import android.content.Context
import android.graphics.PorterDuff
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.rydehomeuser.R


class TabAdapter (fm: androidx.fragment.app.FragmentManager, val context : androidx.fragment.app.FragmentActivity?) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {
    private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragmentList[position]
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        //return mFragmentTitleList.get(position);
        return null
    }

      fun getTabView(position: Int, context: Context?): View
    {

        val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tabTextView : TextView = view.findViewById(R.id.tabTextView)
        tabTextView.text = mFragmentTitleList[position]
        return view
    }

    fun getSelectedTabView(position: Int, context: Context?): View
    {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val tabTextView : TextView = view.findViewById(R.id.tabTextView)
        tabTextView.text = mFragmentTitleList[position]
        context?.resources?.getColor(R.color.white_light)?.let {
            tabTextView.setTextColor(it)

        }


        return view
    }
}
