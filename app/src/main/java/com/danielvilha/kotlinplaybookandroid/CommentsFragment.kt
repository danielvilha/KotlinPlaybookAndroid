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

    private var adapter = GroupAdapter<ViewHolder>()
    private var id: String? = null

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
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }
    //</editor-fold>

    //<editor-fold desc="onViewCreated">
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as MapsActivity).setActionBarTitle(resources.getString(R.string.title_fragment_comments))

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
    //</editor-fold>

    companion object {
        private val TAG = CommentsFragment::class.java.name.toString()
        private const val ID = "ID"

        @JvmStatic
        //<editor-fold desc="arguments">
        fun arguments(id: String) =
            CommentsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                }
            }
        //</editor-fold>
    }
}