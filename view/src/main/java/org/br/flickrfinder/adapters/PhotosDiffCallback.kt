package org.br.flickrfinder.adapters

import androidx.recyclerview.widget.DiffUtil
import org.br.flickrfinder.models.PhotoListViewEntity

class PhotosDiffCallback(
        private val oldArray: List<PhotoListViewEntity>,
        private val newArray: List<PhotoListViewEntity>
) : DiffUtil.Callback()  {
    override fun getOldListSize(): Int = oldArray.size

    override fun getNewListSize(): Int = newArray.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldArray[oldItemPosition].id == newArray[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldArray[oldItemPosition]
        val newItem = newArray[newItemPosition]

        return oldItem.title == newItem.title && oldItem.imgThumb == newItem.imgThumb
    }
}