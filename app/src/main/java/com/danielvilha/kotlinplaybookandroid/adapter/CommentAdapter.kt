package com.danielvilha.kotlinplaybookandroid.adapter

import com.danielvilha.kotlinplaybookandroid.R
import com.danielvilha.kotlinplaybookandroid.`object`.Comment
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.row_comment.view.*

/**
 * Created by danielvilha on 2019-07-25
 */
class CommentAdapter(private var comment: Comment): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name.text = comment.name
        viewHolder.itemView.email.text = comment.email
        viewHolder.itemView.body.text = comment.body
    }

    override fun getLayout() = R.layout.row_comment
}