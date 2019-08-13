package com.danielvilha.kotlinplaybookandroid.adapter

import com.danielvilha.kotlinplaybookandroid.R
import com.danielvilha.kotlinplaybookandroid.`object`.Post
import com.danielvilha.kotlinplaybookandroid.`object`.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_post.view.*
import kotlinx.android.synthetic.main.row_post.view.name

/**
 * Created by danielvilha on 2019-07-25
 */
class PostsAdapter(private var post: Post, private var user: User, private var quantity: Int): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title.text = post.title
        viewHolder.itemView.body.text = post.body
        viewHolder.itemView.name.text = user.name
        viewHolder.itemView.number.text = quantity.toString()
    }

    override fun getLayout() = R.layout.row_post
}