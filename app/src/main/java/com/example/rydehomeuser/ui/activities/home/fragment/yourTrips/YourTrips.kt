package com.example.rydehomeuser.ui.activities.home.fragment.yourTrips


import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.GetTrips
import com.example.rydehomeuser.data.model.getTrips.Past
import com.example.rydehomeuser.data.model.getTrips.Upcoming
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.pastTrips.PastTrips
import com.example.rydehomeuser.ui.activities.home.fragment.upcommingTrips.UpcommingTrips
import com.example.rydehomeuser.utils.Constants.YOUR_TRIPS
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.example.rydehomeuser.utils.TabAdapter
import kotlinx.android.synthetic.main.fragment_past_trips.*
import kotlinx.android.synthetic.main.fragment_your_trips.view.*
import java.lang.Exception


class YourTrips : Fragment(), HomeContract.GetTripsView {


    lateinit var  viewPager : ViewPager
    lateinit var  tabLayout : TabLayout
    lateinit var  tabAdapter : TabAdapter

    var pastListNew : List<Past> = arrayListOf<Past>()
    var upComingListNew : List<Upcoming> = arrayListOf<Upcoming>()
    var pastAdapter : PastTrips? = null
    var upComingAdapter : UpcommingTrips? = null

    var presenter: HomeContract.HomePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view  = inflater.inflate(R.layout.fragment_your_trips, container, false)

        pastListNew  = arrayListOf<Past>()
        upComingListNew = arrayListOf<Upcoming>()


        tabSetup(view)
        clickListener(view)

        return view
    }



    fun clickListener(view: View )
    {
        Home.homeCrossIcon.setOnClickListener {
            activity?.onBackPressed()
        }
    }
    fun tabSetup(view : View)
    {
        GlobalHelper.setToolbar(getString(R.string.your_trips),homeCrossIconVis = true)

      //  GlobalHelper.showProgress(activity as AppCompatActivity)
        presenter = HomePresenterImp(this)
        presenter?.getTrips(YOUR_TRIPS)

        viewPager = view.findViewById(R.id.viewPager_history) as androidx.viewpager.widget.ViewPager
        tabLayout  = view.findViewById(R.id.tabLayout_history) as TabLayout

        tabAdapter = TabAdapter(childFragmentManager,activity)



        /*pastAdapter  = PastTrips(getString(R.string.past),pastListNew)
        upComingAdapter  = UpcommingTrips(getString(R.string.upcoming),upComingListNew)

        tabAdapter.addFragment(pastAdapter!!,getString(R.string.past))
        tabAdapter.addFragment(upComingAdapter!!,getString(R.string.upcoming))

        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)
        highLightCurrentTab(0)
*/


        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                highLightCurrentTab(p0)

            }
        })








    }


    private fun highLightCurrentTab(position: Int) {

        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)!!
            tab.customView = null
            tab.customView = tabAdapter.getTabView(i,activity)
        }

        val tab = tabLayout.getTabAt(position)!!
        tab.customView = null
        tab.customView = tabAdapter.getSelectedTabView(position,activity)
    }

    override fun onGetTripsSuccess(getTrips: GetTrips) {

        try {
            GlobalHelper.hideProgress()
            getTrips.message.let { GlobalHelper.snackBar(view!!.rootYourTrips, it) }



            if (getTrips.code.equals(200))
            {

                if (getTrips.message.equals("No Data Found"))
                {
                    pastAdapter  = PastTrips(getString(R.string.past),pastListNew)
                    upComingAdapter  = UpcommingTrips(getString(R.string.upcoming),upComingListNew)

                    tabAdapter.addFragment(pastAdapter!!,getString(R.string.past))
                    tabAdapter.addFragment(upComingAdapter!!,getString(R.string.upcoming))

                    viewPager.adapter = tabAdapter
                    tabLayout.setupWithViewPager(viewPager)
                    highLightCurrentTab(0)
                }
                else
                {
                    pastListNew = getTrips.body.past
                    upComingListNew = getTrips.body.upcoming

                    pastAdapter  = PastTrips(getString(R.string.past),pastListNew)
                    upComingAdapter  = UpcommingTrips(getString(R.string.upcoming),upComingListNew)


                    tabAdapter.addFragment(pastAdapter!!,getString(R.string.past))
                    tabAdapter.addFragment(upComingAdapter!!,getString(R.string.upcoming))
                    viewPager.adapter = tabAdapter
                    tabLayout.setupWithViewPager(viewPager)
                    highLightCurrentTab(0)

                }
            }


        } catch (ex: Exception) {

        }    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootYourTrips, it)

                pastAdapter  = PastTrips(getString(R.string.past),pastListNew)
                upComingAdapter  = UpcommingTrips(getString(R.string.upcoming),upComingListNew)

                tabAdapter.addFragment(pastAdapter!!,getString(R.string.past))
                tabAdapter.addFragment(upComingAdapter!!,getString(R.string.upcoming))

                viewPager.adapter = tabAdapter
                tabLayout.setupWithViewPager(viewPager)
                highLightCurrentTab(0)
            }
        }catch (ex : Exception) {}    }


}
