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
import org.br.flickrfinder.models.PhotoListViewEntity

class PhotosRecyclerViewAdapter(private var photosList: ArrayList<PhotoListViewEntity>) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.TaskViewHolder>() {
    var onClick: ((photoId: String) -> Unit)? = null

    fun setPhotosList(photosList: List<PhotoListViewEntity>) {
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

        if (photo.imgThumb.isNotEmpty()) {
            Glide.with(holder.imageView).load(photo.imgThumb).placeholder(R.drawable.ic_happy).into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(photo.id)
        }
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhotoTitle: TextView = view.tvPhotoTitle
        val imageView: ImageView = view.imageView
    }
}