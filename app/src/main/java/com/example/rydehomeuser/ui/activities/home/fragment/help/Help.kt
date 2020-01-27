package com.example.rydehomeuser.ui.activities.home.fragment.help


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.GetTrips
import com.example.rydehomeuser.data.model.help.HelpModel
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.Constants.HELP
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_help.view.*
import java.lang.Exception


class Help : Fragment() , HomeContract.HelpView , HelpAdapter.ReportIssue{



    var presenter: HomeContract.HomePresenter? = null
    var recyclerviewHelp : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_help, container, false)


        initialize(view)
        clickLstener(view)


        return view
    }


    fun clickLstener(view: View) {
        Home.homeCrossIcon.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    fun initialize(view: View) {

        recyclerviewHelp =
            view.findViewById(R.id.recyclerview_help) as RecyclerView
        recyclerviewHelp?.layoutManager = LinearLayoutManager(activity)


        GlobalHelper.setToolbar(getString(R.string.help), homeCrossIconVis = true)
        GlobalHelper.showProgress(activity as AppCompatActivity)
        presenter = HomePresenterImp(this)
        presenter?.getTrips(HELP)
      //  presenter?.help("10")

       /* val webviewHelp: WebView = view.findViewById(R.id.webview_help) as WebView

        webviewHelp.settings.javaScriptEnabled = true
        webviewHelp.loadUrl(
            "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                    "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                    "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                    "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE"
        )

        "60.41".conCat(view, view.amount_help)
*/

    }

    override fun onHelpSuccess(ride_id: HelpModel) {
        try {
            GlobalHelper.hideProgress()
            ride_id.message.let { GlobalHelper.snackBar(view!!.rootHelp, it) }
        } catch (ex: Exception) {
        }

    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootHelp, it)
        }
    }catch (ex : Exception) {}

}

    override fun onGetTripsSuccess(getTrips: GetTrips) {
         try {
                   GlobalHelper.hideProgress()
             getTrips.message.let { GlobalHelper.snackBar(view!!.rootHelp, it) }
                   recyclerviewHelp?.adapter = activity?.let { HelpAdapter(it, getTrips.body.past,this) }
               } catch (ex: Exception) {
               }
    }

    override fun onReportIssueSuccess(ride_id: String) {
        presenter?.help(ride_id)
    }

}
