package org.br.flickrfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_photo.view.*
import org.br.flickrfinder.R
import org.br.flickrfinder.models.PhotoViewEntity

class PhotosRecyclerViewAdapter(private var photosList: ArrayList<PhotoViewEntity>) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.TaskViewHolder>() {
    var onClick: ((photo: PhotoViewEntity) -> Unit)? = null
    var onNextPage: (() -> Unit)? = null

    // Use DiffUtil to calculate and load changes to photosList items efficiently
    fun setPhotosList(photosList: List<PhotoViewEntity>) {
        val diffCallback = PhotosDiffCallback(this.photosList, photosList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.photosList.clear()
        this.photosList.addAll(photosList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
            TaskViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.row_photo,
                            parent,
                            false
                    )
            )

    override fun getItemCount(): Int = photosList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val photo = photosList[position]

        if (photo.title.isNotEmpty()) {
            holder.tvPhotoTitle.text = photosList[position].title
        }

        // Hide check unless saved
        holder.ivCheck.visibility = View.GONE

        // Load Bitmap if exists, if not load URL if exists, if not set empty image
        when {
            photo.thumbBitmap != null -> {
                Glide.with(holder.imageView)
                        .load(photo.thumbBitmap)
                        .placeholder(R.drawable.ic_happy)
                        .into(holder.imageView)

                // Show check if saved
                holder.ivCheck.visibility = View.VISIBLE
            }

            photo.thumbUrl.isNotEmpty() -> {
                Glide.with(holder.imageView)
                        .load(photo.thumbUrl)
                        .placeholder(R.drawable.ic_happy)
                        .into(holder.imageView)
            }

            else -> {
                Glide.with(holder.imageView)
                        .load(R.drawable.ic_happy)
                        .into(holder.imageView)
            }
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(photo)
        }

        // I didn't feel the need to implement the new Paging adapter for this
        // This will notify the fragment of reaching the next page to load more images
        if (position == photosList.size - 1) {
            onNextPage?.invoke()
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhotoTitle: TextView = view.tvPhotoTitle
        val imageView: ImageView = view.imageView
        val ivCheck: ImageView = view.ivCheck
    }
}