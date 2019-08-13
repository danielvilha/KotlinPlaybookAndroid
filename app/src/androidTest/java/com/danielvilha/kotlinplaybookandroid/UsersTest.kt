package com.danielvilha.kotlinplaybookandroid

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danielvilha.kotlinplaybookandroid.service.ApiFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by danielvilha on 2019-08-09
 */
@RunWith(AndroidJUnit4::class)
class UsersTest {

    @Test
    fun getUsers() {
        val service = ApiFactory.api

        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getUsers()
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    for (user in response.body()!!) {
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

    companion object {
        private var TAG = UsersTest::class.java.name
    }
}