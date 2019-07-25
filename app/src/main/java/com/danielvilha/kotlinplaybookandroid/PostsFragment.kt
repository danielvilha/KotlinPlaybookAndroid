package com.danielvilha.kotlinplaybookandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danielvilha.kotlinplaybookandroid.adapter.PostsAdapter
import com.danielvilha.kotlinplaybookandroid.service.ApiFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            id = arguments?.getString(ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Posts"

        val service = ApiFactory.api

        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getPosts(id!!)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val posts = response.body()
                    Log.d(TAG, posts.toString())
                    for (post in posts!!) {
                        adapter.add(PostsAdapter(post))
                    }

                    adapter.setOnItemClickListener { item, _ ->
                        val post = item as PostsAdapter
                        Log.d(TAG, "Clicked item: ${post.id}")
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(R.id.container, CommentsFragment.arguments(post.id.absoluteValue.toString()))
                            ?.commit()
                    }
                } else {
                    Log.d(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        recycler.adapter = adapter
    }

    companion object {
        private val TAG = PostsFragment::class.java.name.toString()
        private const val ID = "ID"

        @JvmStatic
        fun arguments(id: String) =
            PostsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                }
            }
    }
}