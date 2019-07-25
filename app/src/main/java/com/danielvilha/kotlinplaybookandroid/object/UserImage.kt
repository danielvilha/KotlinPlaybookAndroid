package com.danielvilha.kotlinplaybookandroid.`object`

import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by danielvilha on 2019-07-24
 */
object UserImage {

    private val list = listOf("eyes1.png", "eyes10.png", "eyes2.png", "eyes3.png", "eyes4.png", "eyes5.png", "eyes6.png", "eyes7.png", "eyes9.png",
        "nose2.png", "nose3.png", "nose4.png", "nose5.png", "nose6.png", "nose7.png", "nose8.png", "nose9.png",
        "mouth1.png", "mouth10.png", "mouth11.png", "mouth3.png", "mouth5.png", "mouth6.png", "mouth7.png", "mouth9.png")

    fun getImage(image: ImageView) {
        Picasso.get().load("https://api.adorable.io/avatars/" + list.random()).into(image)
    }
}