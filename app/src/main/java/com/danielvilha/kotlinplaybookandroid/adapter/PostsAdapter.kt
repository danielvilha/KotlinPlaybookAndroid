package com.danielvilha.kotlinplaybookandroid.adapter

import com.danielvilha.kotlinplaybookandroid.R
import com.danielvilha.kotlinplaybookandroid.`object`.Post
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_post.view.*

/**
 * Created by danielvilha on 2019-07-25
 */
class PostsAdapter(private var post: Post): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title.text = post.title
        viewHolder.itemView.body.text = post.body
        viewHolder.itemView.name.text = "SET TEXT"
        viewHolder.itemView.number.text = "0"
    }

    override fun getLayout() = R.layout.row_post
}