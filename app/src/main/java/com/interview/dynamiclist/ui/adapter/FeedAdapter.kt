package com.interview.dynamiclist.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.interview.dynamiclist.R
import com.interview.dynamiclist.data.model.FeedEventModel

class FeedAdapter(val context: Context) : RecyclerView.Adapter<FeedViewHolder>(), Filterable {
    private val TAG = "FeedAdapter"
    private val feedEventModelList = ArrayList<FeedEventModel>()
    private val feedEventModelFilterList = ArrayList<FeedEventModel>()
    //private val itemFilter : ItemFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item_row_incoming, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int = feedEventModelList.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.setFeedData(feedEventModelList[position])
        setAnimation(holder.itemView, position)
    }


    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    fun getFeeds(): ArrayList<FeedEventModel> {
        return feedEventModelList
    }

    fun setFeeds(feeds: ArrayList<FeedEventModel>) {
        feedEventModelList.addAll(feeds)
        Log.d(TAG, "setFeeds: ${feedEventModelList}")
        notifyDataSetChanged()
    }

    fun addFeedItemToList(feedEventModel: FeedEventModel, index: Int) {
        feedEventModelList.add(index, feedEventModel)
        //feedEventModelFilterList.add(index, feedEventModel)
        notifyItemInserted(index)
    }

    fun setAnimation(viewToAnimate: View, position: Int) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        viewToAnimate.startAnimation(animation)
    }
}