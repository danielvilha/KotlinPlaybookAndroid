package com.danielvilha.kotlinplaybookandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.danielvilha.kotlinplaybookandroid.PostsFragment
import com.danielvilha.kotlinplaybookandroid.R
import com.danielvilha.kotlinplaybookandroid.`object`.User
import com.danielvilha.kotlinplaybookandroid.adapter.MapAdapter
import com.danielvilha.kotlinplaybookandroid.service.ApiFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by danielvilha on 2019-07-23
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private var activity: MapsActivity? = null
    private var users: List<User>? = null
    private lateinit var mMap: GoogleMap

    //<editor-fold desc="onCreate">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(toolbar)

        activity = this
        toolbar.title = resources.getString(R.string.title_activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    //</editor-fold>

    //<editor-fold desc="onMapReady">
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getUsers()

        googleMap.setOnInfoWindowClickListener(this)
    }
    //</editor-fold>

    //<editor-fold desc="onInfoWindowClick">
    override fun onInfoWindowClick(marker: Marker?) {
        val user = getUser(marker)
        Log.d(TAG, "Marker Click: ${user?.name}")

        supportFragmentManager.beginTransaction()
            .add(R.id.container, PostsFragment.arguments(user?.id.toString()))
            .addToBackStack(MapsActivity::class.java.name)
            .commit()
    }
    //</editor-fold>

    //<editor-fold desc="onOptionsItemSelected">
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
    //</editor-fold>

    //<editor-fold desc="getUsers">
    private fun getUsers() {
        val service = ApiFactory.api

        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getUsers()
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    users = response.body()
                    for (user in users!!) {
                        insertInMap(user)
                        Log.d(TAG, "User: $user")
                    }
                } else {
                    Log.d(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="insertInMap">
    private fun insertInMap(user: User) {
        val latLng = LatLng(user.address.geo.lat.toDouble(), user.address.geo.lng.toDouble())
        mMap.setInfoWindowAdapter(MapAdapter(activity, users!!))
        mMap.addMarker(MarkerOptions().position(latLng))
    }
    //</editor-fold>

    //<editor-fold desc="getUser">
    private fun getUser(marker: Marker?) : User? {
        for (usr in users!!) {
            if (marker?.position?.latitude == usr.address.geo.lat.toDouble() && marker.position.longitude == usr.address.geo.lng.toDouble()) {
                return usr
            }
        }

        return null
    }
    //</editor-fold>

    //<editor-fold desc="setActionBarTitle">
    fun setActionBarTitle(title: String) {
        supportActionBar!!.title = title
    }
    //</editor-fold>

    companion object {
        private val TAG = MapsActivity::class.java.name.toString()
    }
}
