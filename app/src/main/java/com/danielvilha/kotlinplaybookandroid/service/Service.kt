package com.danielvilha.kotlinplaybookandroid.service

import com.danielvilha.kotlinplaybookandroid.`object`.Comment
import com.danielvilha.kotlinplaybookandroid.`object`.Post
import com.danielvilha.kotlinplaybookandroid.`object`.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by danielvilha on 2019-07-24
 */
interface Service {

    @GET("users/")
    fun getUser(@Query("id") id: String): Deferred<Response<List<User>>>

    @GET("users")
    fun getUsers(): Deferred<Response<List<User>>>

    @GET("posts")
    fun getPosts(@Query("userId") id: String): Deferred<Response<List<Post>>>

    @GET("comments")
    fun getComments(@Query("postId") id: String): Deferred<Response<List<Comment>>>
}