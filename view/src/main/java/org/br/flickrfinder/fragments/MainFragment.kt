package org.br.flickrfinder.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.comp_search.*
import kotlinx.android.synthetic.main.comp_search.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.br.flickrfinder.R
import org.br.flickrfinder.adapters.PhotosRecyclerViewAdapter
import org.br.flickrfinder.mappers.PhotoListViewViewModelMapper
import org.br.util.inflate
import org.br.viewmodel.viewmodels.PhotosListViewModel

class MainFragment : BaseFragment() {
    private lateinit var photosListVM: PhotosListViewModel

    private val photoListViewViewModelMapper = PhotoListViewViewModelMapper()

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

                    (rvPhotos.adapter as? PhotosRecyclerViewAdapter)?.let { adapter ->
                        adapter.setPhotosList(
                                photos.map {
                                    photoListViewViewModelMapper.upstream(it)
                                }
                        )

                        adapter.notifyDataSetChanged()
                    }
                }
        )

        photosListVM.getAllSearchTerms().observe(
                viewLifecycleOwner,
                Observer { searchTerms ->
                    context?.let {
                        actvSearch.setAdapter(
                                ArrayAdapter<String>(
                                        it,
                                        android.R.layout.simple_dropdown_item_1line,
                                        searchTerms
                                )
                        )
                    }
                }
        )

        comp_search.bSearch.setOnClickListener {
            photosListVM.retrievePhotos(comp_search.actvSearch.text.toString())
        }
    }

    private fun setupPhotosRecyclerView() {
        val adapter = PhotosRecyclerViewAdapter(arrayListOf())
        adapter.onClick = {
            findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToPhotoFragment(it)
            )
        }

        rvPhotos.adapter = adapter
    }
}