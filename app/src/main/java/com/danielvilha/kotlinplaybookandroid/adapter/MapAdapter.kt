package com.danielvilha.kotlinplaybookandroid.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.danielvilha.kotlinplaybookandroid.R
import com.danielvilha.kotlinplaybookandroid.`object`.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

/**
 * Created by danielvilha on 2019-07-25
 */
class MapAdapter(private val context: Context?, private val users: List<User>): GoogleMap.InfoWindowAdapter {

    private val list = listOf("eyes1.png", "eyes10.png", "eyes2.png", "eyes3.png", "eyes4.png", "eyes5.png", "eyes6.png", "eyes7.png", "eyes9.png",
        "nose2.png", "nose3.png", "nose4.png", "nose5.png", "nose6.png", "nose7.png", "nose8.png", "nose9.png",
        "mouth1.png", "mouth10.png", "mouth11.png", "mouth3.png", "mouth5.png", "mouth6.png", "mouth7.png", "mouth9.png")


    override fun getInfoContents(marker: Marker?): View {
        val view = (context as AppCompatActivity).layoutInflater.inflate(R.layout.row_user, null)
        val user: User? = getUser(marker)

        val image = view.findViewById<ImageView>(R.id.image)
        Picasso.get().load("https://api.adorable.io/avatars/" + list.random()).into(image)

        val tvName = view.findViewById<TextView>(R.id.name)
        val tvEmail = view.findViewById<TextView>(R.id.email)
        val tvPhone = view.findViewById<TextView>(R.id.phone)
        val tvWebsite = view.findViewById<TextView>(R.id.website)
        val tvCompany = view.findViewById<TextView>(R.id.company)
        val tvAddress = view.findViewById<TextView>(R.id.address)

        tvName.text = String.format("${user?.name} ${user?.username}")
        tvEmail.text = user?.email
        tvPhone.text = user?.phone
        tvWebsite.text = user?.website
        tvCompany.text = user?.company?.name
        tvAddress.text = String.format("${user?.address?.street} ${user?.address?.suite} - ${user?.address?.city}, ${user?.address?.zipcode}")

        Log.d("MapAdapter", "User id: ${user?.id}")

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    private fun getUser(marker: Marker?) : User? {
        var user: User? = null

        for (usr in users) {
            if (marker?.position?.latitude == usr.address.geo.lat.toDouble() && marker.position.longitude == usr.address.geo.lng.toDouble()) {
                user = usr
            }
        }

        return user
    }
}