package com.example.rydehomeuser.ui.activities.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.view.MenuItem
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.rydehomedriver.ui.baseclasses.BaseActivity
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getProfile.GetProfile
import com.example.rydehomeuser.data.model.logout.Logout
import com.example.rydehomeuser.data.model.synContacts.SynContacts
import com.example.rydehomeuser.di.UserDemo
import com.example.rydehomeuser.di.UserModel
import com.example.rydehomeuser.ui.activities.home.fragment.accountSettings.AccountSettings
import com.example.rydehomeuser.ui.activities.home.fragment.freeRTrips.FreeTrips
import com.example.rydehomeuser.ui.activities.home.fragment.help.Help
import com.example.rydehomeuser.ui.activities.home.fragment.homeFragment.HomeFragment
import com.example.rydehomeuser.ui.activities.home.fragment.loyalty.Loyalty
import com.example.rydehomeuser.ui.activities.home.fragment.payment.Payment
import com.example.rydehomeuser.ui.activities.home.fragment.yourTrips.YourTrips
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.models.ContactModel
import com.example.rydehomeuser.utils.Constants.HOME_ACTIVITY
import com.example.rydehomeuser.utils.Constants.HOME_SCREEN
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.GlobalHelper.PROMO_CODE
import com.example.rydehomeuser.utils.LocationService
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.async


class Home : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView ,
    OnMapReadyCallback/*,Cloneable*/ {



    var presenter: HomeContract.HomePresenter? = null
    private val REQUEST_PERMISSION_LOCATION = 10
    private var mMap: GoogleMap? = null
    private var contactModelArrayList: ArrayList<ContactModel>? = null



    companion object {
        lateinit var titleHome: TextView
        lateinit var relToggle: RelativeLayout
        lateinit var relMenuHome: RelativeLayout
        lateinit var relDoneHome: RelativeLayout

        lateinit var homeMenuIcon: ImageView
        lateinit var homeBackIcon: ImageView
        lateinit var homeCrossIcon: ImageView
        lateinit var navUseImage: ImageView
        lateinit var relInfoHome: RelativeLayout
        lateinit var drawer: DrawerLayout
        lateinit var navUsername  : TextView
        lateinit var nav_headerRatingText  : TextView
        lateinit var nav_headerRating  : RatingBar
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        Log.e("authKey_print", SharedPrefUtil.getInstance()?.getAuthToken())


        drawer = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        titleHome = findViewById<TextView>(R.id.title_home)
        relToggle = findViewById<RelativeLayout>(R.id.rel_toggle)
        relDoneHome = findViewById<RelativeLayout>(R.id.rel_Done_Home)
        relMenuHome = findViewById<RelativeLayout>(R.id.rel_menu_home)
        homeBackIcon = findViewById<ImageView>(R.id.home_back_icon)
        homeMenuIcon = findViewById<ImageView>(R.id.home_menu_icon)
        homeCrossIcon = findViewById<ImageView>(R.id.home_cross_icon)
        relInfoHome = findViewById<RelativeLayout>(R.id.rel_info_Home)

        presenter = HomePresenterImp(this)
        presenter?.getProfile(HOME_SCREEN)



        txtview_Done.paintFlags = txtview_Done.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG




        homeMenuIcon.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        navUsername = headerView.findViewById(R.id.nav_header_textView) as TextView
        nav_headerRatingText = headerView.findViewById(R.id.nav_header_textView1) as TextView
        navUseImage = headerView.findViewById(R.id.nav_header_imageView) as ImageView
      //  nav_headerRating = headerView.findViewById(R.id.nav_header_rating)
        navUsername.text = SharedPrefUtil.getInstance()?.getUserName()
        if (!SharedPrefUtil.getInstance()?.getImage()?.isEmpty()!!)
         Glide.with(this).load(SharedPrefUtil.getInstance()?.getImage()).into(navUseImage)



          supportFragmentManager.beginTransaction().replace(R.id.container_map, HomeFragment()).commit()
       // supportFragmentManager.beginTransaction().replace(R.id.container_map, Tip("")).commit()
        clickListener()


      //--------------------load contacts-----------------------
        try {

        loadContacts()
        val gson = Gson()
        val element = gson.toJsonTree(contactModelArrayList, object : TypeToken<List<ContactModel>>() {}.type)
        if (!element.isJsonArray) { throw Exception() as Throwable
        }
        val jsonArray = element.asJsonArray
       // Log.e("printContacts",jsonArray.toString())
        presenter!!.synContacts(HOME_SCREEN,jsonArray.toString())

        }catch (ex : Exception){}


    }


    fun clickListener() {
        /*rel_switch_left.setOnClickListener {
            img_switch_left.visibility = View.VISIBLE
            img_switch_right.visibility = View.GONE
            title_home.text = getString(R.string.online)

        }

        rel_switch_right.setOnClickListener {
            img_switch_left.visibility = View.GONE
            img_switch_right.visibility = View.VISIBLE
            title_home.text = getString(R.string.offline)
        }*/

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_item_yourTrip -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                YourTrips()
            ).addToBackStack(null).commit()
            R.id.nav_item_loyalty -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                Loyalty()
            ).addToBackStack(null).commit()
            R.id.nav_item_help -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                Help()
            ).addToBackStack(null).commit()
            R.id.nav_item_freeTrips -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                FreeTrips()
            ).addToBackStack(null).commit()
            R.id.nav_item_settings -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                AccountSettings()
            ).addToBackStack(null).commit()
            R.id.nav_item_payment -> supportFragmentManager.beginTransaction().replace(
                R.id.container_map,
                Payment()
            ).addToBackStack(null).commit()


        }
        drawer.closeDrawer(GravityCompat.START)

        return true

    }


    override fun onGetProfileSuccess(getProfile: GetProfile) {

        try {

            if (getProfile.code.equals(200))
            {
                PROMO_CODE = getProfile.body.referral_code

                SharedPrefUtil.getInstance()?.saveUserName("${getProfile.body.first_name} ${getProfile.body.last_name}")
                SharedPrefUtil.getInstance()?.saveImage(getProfile.body.photo)
                navUsername.text = "${getProfile.body.first_name} ${getProfile.body.last_name}"

                if (getProfile.body.average.equals("0"))
                    nav_headerRatingText.text = "0"
                else
                    nav_headerRatingText.text = GlobalHelper.uptoOneDecimal(getProfile.body.average)


                if (!getProfile.body.photo.isEmpty())
                    Glide.with(this).load(getProfile.body.photo).into(navUseImage)
            }



        }catch (ex : Exception){}


    }

    override fun onFailure(error: String) {

        if (error.equals("Invalid authorization_key"))
          presenter!!.logout(HOME_ACTIVITY)
    }



    override fun onResume() {
        super.onResume()

        if (checkPermissionForLocation(this)) {
            startService(Intent(this, LocationService::class.java))
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startService(Intent(this, LocationService::class.java))

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LocationService::class.java))
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        this.mMap = googleMap

        if (!GlobalHelper.CURRENT_LAT.equals(0.0) && !GlobalHelper.CURRENT_LNG.equals(0.0))
        {
            val position = LatLng(GlobalHelper.CURRENT_LAT, GlobalHelper.CURRENT_LNG)
            mMap?.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(""))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14f))
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
        val fragment : Fragment? = getSupportFragmentManager().findFragmentById(R.id.container_map)
        fragment!!.onActivityResult(requestCode, resultCode, data)


    }*/


    fun loadContacts()   {

        contactModelArrayList = arrayListOf<ContactModel>()

        val phones = contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            var name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var contact = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            if (!contact.isEmpty())
            {
                if (contact.contains(" "))
                {
                    contact = contact.replace(" ","")
                }
                val contactModel = ContactModel(name,contact)
                contactModelArrayList!!.add(contactModel)

            }
            Log.e("name------->>", name + "  " + contact)

        }
        phones.close()
    }

    override fun onSynContactsSuccess(synContacts: SynContacts) {

    }

    override fun onLogoutSuccess(logout: Logout) {

        try {
            if (logout.code.equals(200))
            {
                SharedPrefUtil.getInstance()?.setLogin(false)
                SharedPrefUtil.getInstance()?.clear()

                val intent = Intent(this, SignUp::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }catch (ex : Exception){}
    }


}
