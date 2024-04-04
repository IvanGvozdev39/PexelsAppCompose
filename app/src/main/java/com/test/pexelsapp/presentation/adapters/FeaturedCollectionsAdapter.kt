package com.test.pexelsapp.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R

class FeaturedCollectionsAdapter(
    private val context: Context?,
    private var collectionList: List<com.test.domain.models.images.Collection>,
    private val imageRVAdapter: ImageRVAdapter
) : RecyclerView.Adapter<FeaturedCollectionsAdapter.ViewHolder>() {

    private var selectedPosition: Int = -1
    private var wasClicked: Boolean = false


    fun setData(collections: List<com.test.domain.models.images.Collection>) {
        collectionList = collections
        notifyDataSetChanged()
    }


    fun resetSelection() {
        selectedPosition = -1
        notifyDataSetChanged()
    }


    fun setSelectedCollection(position: Int) {
        selectedPosition = position
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTV: TextView = view.findViewById(R.id.text_view)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.featured_collections_item, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return collectionList.count()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = collectionList[position]
        holder.titleTV.text = collection.title

        if (position == selectedPosition) {
            holder.titleTV.backgroundTintList =
                ColorStateList.valueOf(context?.getColor(R.color.red) ?: Color.RED)
            holder.titleTV.setTextColor(context?.getColor(R.color.actual_white) ?: Color.WHITE)
        } else {
            holder.titleTV.backgroundTintList =
                ColorStateList.valueOf(context?.getColor(R.color.lighter_gray) ?: Color.LTGRAY)
            holder.titleTV.setTextColor(context?.getColor(R.color.gray) ?: Color.GRAY)
        }

        holder.titleTV.setOnClickListener {
            setSelectedCollection(position)
            // Notify the fragment about the selected collection
            onCollectionSelected(collection)
            imageRVAdapter.setImageData(ArrayList<Photo>(), context!!)
        }
    }

    var onCollectionSelected: (com.test.domain.models.images.Collection) -> Unit = {}

}