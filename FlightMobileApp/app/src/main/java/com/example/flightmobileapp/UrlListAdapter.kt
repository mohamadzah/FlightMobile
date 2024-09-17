package com.example.flightmobileapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UrlListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<UrlListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var urls = emptyList<UrlItem>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urlItemView: TextView = itemView.findViewById(R.id.itemView) // might change this  to the text thingy
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.list_item, parent, false) // this too
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = urls[position]
        holder.urlItemView.text = current.url
    }

    internal fun setUrls(urls: List<UrlItem>) {
        this.urls = urls
        notifyDataSetChanged()
    }

    override fun getItemCount() = urls.size
}