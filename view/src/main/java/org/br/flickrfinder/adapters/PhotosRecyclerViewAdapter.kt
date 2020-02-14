package org.br.flickrfinder.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.row_photo.view.*
import org.br.flickrfinder.R
import org.br.flickrfinder.models.PhotoListViewEntity

class PhotosRecyclerViewAdapter(var photosList: Array<PhotoListViewEntity>) : RecyclerView.Adapter<PhotosRecyclerViewAdapter.TaskViewHolder>() {
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

        //if (photo.imgThumb.isNotEmpty()) {
            Glide.with(holder.imageView).load(photo.imgThumb).placeholder(R.drawable.ic_happy).into(holder.imageView)
            // TODO: this is where you can insert the image
            // https:\/\/live.staticflickr.com\/65535\/49523435823_31284ca41e_t.jpg
        //}
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhotoTitle: TextView = view.tvPhotoTitle
        val imageView: ImageView = view.imageView
    }
}