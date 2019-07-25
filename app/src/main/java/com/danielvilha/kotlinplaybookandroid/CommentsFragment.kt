package com.danielvilha.kotlinplaybookandroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danielvilha.kotlinplaybookandroid.adapter.CommentAdapter
import com.danielvilha.kotlinplaybookandroid.service.ApiFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import kotlinx.android.synthetic.main.fragment_comments.*

/**
 * Created by danielvilha on 2019-07-25
 */
class CommentsFragment : Fragment() {

    private var id: String? = null
    private var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.apply {
            id = arguments?.getString(ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Comments"

        val service = ApiFactory.api

        GlobalScope.launch(Dispatchers.Main) {
            val request = service.getComments(id!!)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    val comments = response.body()
                    Log.d(TAG, comments.toString())
                    for (comment in comments!!) {
                        adapter.add(CommentAdapter(comment))
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
        private const val TAG = "CommentsFragment"
        private const val ID = "ID"

        @JvmStatic
        fun arguments(id: String) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                }
            }
    }
}