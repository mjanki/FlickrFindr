package org.br.flickrfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_main.*
import org.br.flickrfinder.R
import org.br.flickrfinder.adapters.PhotosRecyclerViewAdapter
import org.br.util.inflate
import org.br.viewmodel.viewmodels.PhotosListViewModel

class MainFragment : BaseFragment() {
    private lateinit var photosListVM: PhotosListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosListVM = ViewModelProviders.of(this).get(PhotosListViewModel::class.java)
        photosListVM.init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_main)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPhotosRecyclerView()

        photosListVM.getAllPhotos().observe(
                viewLifecycleOwner,
                Observer { photos ->
                    if (photos.isEmpty()) return@Observer

                    (rvPhotos.adapter as? PhotosRecyclerViewAdapter)?.let {
                        it.photos = photos.toTypedArray()
                        it.notifyDataSetChanged()
                    }
                }
        )

        photosListVM.retrievePhotos("dogs")

        fabAddTask.setOnClickListener {
            photosListVM.retrievePhotos("dogs", 2)
        }
    }

    private fun setupPhotosRecyclerView() {
        rvPhotos.apply {
            adapter = PhotosRecyclerViewAdapter(arrayOf())
        }
    }
}