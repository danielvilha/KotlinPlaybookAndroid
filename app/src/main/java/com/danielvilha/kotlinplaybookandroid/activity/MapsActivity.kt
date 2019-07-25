package com.danielvilha.kotlinplaybookandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by danielvilha on 2019-07-23
 */
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private var activity: MapsActivity? = null
    private lateinit var mMap: GoogleMap
    private var users: List<User>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        activity = this
        activity?.title = resources.getString(R.string.title_activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getUsers()

        googleMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val user = getUser(marker)
        Log.d(TAG, "Marker Click: ${user?.name}")

        supportFragmentManager.beginTransaction()
            .add(R.id.container, PostsFragment.arguments(user?.id.toString()))
            .commit()
    }

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

    private fun insertInMap(user: User) {
        val latLng = LatLng(user.address.geo.lat.toDouble(), user.address.geo.lng.toDouble())
        mMap.setInfoWindowAdapter(MapAdapter(activity, users!!))
        mMap.addMarker(MarkerOptions().position(latLng))
    }

    private fun getUser(marker: Marker?) : User? {
        var user: User? = null

        for (usr in users!!) {
            if (marker?.position?.latitude == usr.address.geo.lat.toDouble() && marker.position.longitude == usr.address.geo.lng.toDouble()) {
                user = usr
            }
        }

        return user
    }

    companion object {
        private val TAG = MapsActivity::class.java.name.toString()
    }
}
