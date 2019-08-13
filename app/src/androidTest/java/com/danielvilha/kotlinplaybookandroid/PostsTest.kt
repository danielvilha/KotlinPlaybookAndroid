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
class PostsTest {

    @Test
    fun getPost() {
        val service = ApiFactory.api

        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getPosts("0")
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    for (post in response.body()!!) {
                        Log.d(TAG, "Post: $post")
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
        private val TAG = PostsTest::class.java.name
    }
}