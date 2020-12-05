package com.interview.dynamiclist.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.interview.dynamiclist.data.model.FeedEventModel
import kotlinx.android.synthetic.main.feed_item_row_incoming.view.*

class FeedViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun setFeedData(feedEventModel: FeedEventModel) {
        val color = Color.parseColor(feedEventModel.color)
        (itemView.feed_shape.drawable.mutate() as GradientDrawable).setColor(color)
        itemView.feed_title_txt.text = feedEventModel.name
        itemView.feed_weight_txt.text = feedEventModel.weight
    }
}