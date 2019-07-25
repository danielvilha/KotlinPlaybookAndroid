package com.danielvilha.kotlinplaybookandroid.service

/**
 * Created by danielvilha on 2019-07-24
 */
object ApiFactory {

    val api: Service = RetrofitFactory.retrofit().create(Service::class.java)
}