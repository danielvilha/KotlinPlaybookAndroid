package com.danielvilha.kotlinplaybookandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danielvilha.kotlinplaybookandroid.`object`.Post
import com.danielvilha.kotlinplaybookandroid.`object`.User
import com.danielvilha.kotlinplaybookandroid.adapter.PostsAdapter
import com.danielvilha.kotlinplaybookandroid.service.ApiFactory
import com.danielvilha.kotlinplaybookandroid.service.Service
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

import kotlinx.android.synthetic.main.fragment_posts.*

/**
 * Created by danielvilha on 2019-07-25
 */
class PostsFragment : Fragment() {

    private var id: String? = null
    private var adapter = GroupAdapter<ViewHolder>()

    //<editor-fold desc="onCreate">
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            id = arguments?.getString(ID)
        }
    }
    //</editor-fold>

    //<editor-fold desc="onCreateView">
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }
    //</editor-fold>

    //<editor-fold desc="onViewCreated">
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = ApiFactory.api

//        getUser(service)

        GlobalScope.launch(Dispatchers.Main) {
            val requestUser = service.getUser(id!!)
            try {
                val responseUser = requestUser.await()
                if (responseUser.isSuccessful) {
                    val user = responseUser.body()
                    GlobalScope.launch(Dispatchers.Main) {
                        val requestPost = service.getPosts(id!!)
                        try {
                            val responsePost = requestPost.await()
                            if (responsePost.isSuccessful) {
                                val posts = responsePost.body()
                                Log.d(TAG, posts.toString())
                                for (post in posts!!) {
                                    GlobalScope.launch(Dispatchers.Main) {
                                        val requestComments =  service.getComments(post.id.toString())
                                        try {
                                            val responseComments = requestComments.await()
                                            if (responseComments.isSuccessful) {
                                                adapter.add(PostsAdapter(post, user?.get(0)!!, responseComments.body()?.size!!))
                                            } else {
                                                Log.d(TAG, responseComments.errorBody().toString())
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    }
                                }

                                adapter.setOnItemClickListener { item, _ ->
                                    val post = item as PostsAdapter
                                    Log.d(TAG, "Clicked item: ${post.id}")
                                    activity?.supportFragmentManager?.beginTransaction()
                                        ?.replace(R.id.container, CommentsFragment.arguments(post.id.absoluteValue.toString()), CommentsFragment::class.java.simpleName)
                                        ?.addToBackStack(PostsFragment::class.java.name)
                                        ?.commit()
                                }
                            } else {
                                Log.d(TAG, responsePost.errorBody().toString())
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Log.d(TAG, responseUser.errorBody().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        recycler.adapter = adapter
    }
    //</editor-fold>

    //<editor-fold desc="getUser">
    private fun getUser(service: Service) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getUser(id!!)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val user = response.body()
                    getPosts(service, user!![0])
                } else {
                    Log.d(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="getPosts">
    private fun getPosts(service: Service, user: User) {
        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getPosts(id!!)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val posts = response.body()
                    Log.d(TAG, posts.toString())
                    for (post in posts!!) {
                        getQuantityComments(service, post, user)
                    }

                    adapter.setOnItemClickListener { item, _ ->
                        val post = item as PostsAdapter
                        Log.d(TAG, "Clicked item: ${post.id}")
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.container, CommentsFragment.arguments(post.id.absoluteValue.toString()), CommentsFragment::class.java.simpleName)
                            ?.addToBackStack(PostsFragment::class.java.name)
                            ?.commit()
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

    //<editor-fold desc="getQuantityComments">
    private fun getQuantityComments(service: Service, post: Post, user: User) {
        GlobalScope.launch(Dispatchers.Main) {
            val request =  service.getComments(post.id.toString())
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    adapter.add(PostsAdapter(post, user, response.body()?.size!!))
                } else {
                    Log.d(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    //</editor-fold>

    companion object {
        private val TAG = PostsFragment::class.java.name.toString()
        private const val ID = "ID"

        @JvmStatic
        //<editor-fold desc="arguments">
        fun arguments(id: String) =
            PostsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                }
            }
        //</editor-fold>
    }
}