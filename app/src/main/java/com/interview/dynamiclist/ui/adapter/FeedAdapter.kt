package com.interview.dynamiclist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.interview.dynamiclist.R
import com.interview.dynamiclist.data.model.FeedEventModel

class FeedAdapter() : RecyclerView.Adapter<FeedViewHolder>(), Filterable {
    private val feedEventModelList = ArrayList<FeedEventModel>()
    private val feedEventModelFilterList = ArrayList<FeedEventModel>()
    //private val itemFilter : ItemFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item_row_incoming, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int = feedEventModelFilterList.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int)
            = holder.setFeedData(feedEventModelFilterList[position])


    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

    fun setFeedEventModelList(feedEventModelList: ArrayList<FeedEventModel>) {
        feedEventModelList.addAll(feedEventModelList)
        feedEventModelFilterList.addAll(feedEventModelList)
        notifyDataSetChanged()
    }

    fun addFeedItemToList(feedEventModel: FeedEventModel, index: Int) {
        feedEventModelList.add(index, feedEventModel)
        feedEventModelFilterList.add(index, feedEventModel)
        notifyItemInserted(index)
    }
}