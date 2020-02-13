package org.br.flickrfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_photo.view.*
import org.br.flickrfinder.R

class PhotosRecyclerViewAdapter(var photos: Array<String>) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.row_photo,
                            parent,
                            false
                    )
            )

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.tvPhotoTitle.text = photos[position]
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhotoTitle: TextView = view.tvPhotoTitle
    }
}