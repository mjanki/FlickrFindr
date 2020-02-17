package org.br.flickrfinder.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.comp_search.*
import kotlinx.android.synthetic.main.comp_search.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.br.flickrfinder.R
import org.br.flickrfinder.adapters.PhotosRecyclerViewAdapter
import org.br.flickrfinder.mappers.PhotoViewViewModelMapper
import org.br.util.extensions.setVisible
import org.br.viewmodel.viewmodels.PhotosListViewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var photosListVM: PhotosListViewModel

    private val photoViewViewModelMapper = PhotoViewViewModelMapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        photosListVM = ViewModelProviders.of(this).get(PhotosListViewModel::class.java)
        photosListVM.init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.app_name)

        setupPhotosRecyclerView()

        setupPhotosObservers()
        setupSearchTermsObservers()

        comp_search.bSearch.setOnClickListener {
            photosListVM.retrievePhotosFirstPage(comp_search.actvSearch.text.toString())
        }
    }

    private fun setupPhotosObservers() {
        // When photos are retrieved, map them to current layer and push to adapter
        photosListVM.getAllPhotos().observe(
                viewLifecycleOwner,
                Observer { photos ->
                    (rvPhotos.adapter as? PhotosRecyclerViewAdapter)?.let { adapter ->
                        adapter.setPhotosList(
                                photos.map {
                                    photoViewViewModelMapper.upstream(it)
                                }
                        )
                    }
                }
        )

        // If loading photos display Progress Dialog
        photosListVM.getIsRetrievingPhotos().observe(
                viewLifecycleOwner,
                Observer {
                    progressBar.setVisible(it)
                }
        )

        // If No Results display Snackbar
        photosListVM.getNoResults().observe(
                viewLifecycleOwner,
                Observer {
                    if (it) { showNoResultsSnackbar() }
                }
        )
    }

    private fun setupSearchTermsObservers() {
        // Retrieve saved search terms (history) from database and push to auto complete adapter
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
    }

    private fun setupPhotosRecyclerView() {
        val adapter = PhotosRecyclerViewAdapter(arrayListOf())

        // When item is clicked, if photo doesn't exist then display message saying that
        adapter.onClick = {
            if (it.originalUrl.isNotEmpty() || it.originalBitmap != null) {
                findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToPhotoFragment(it.id)
                )
            } else {
                showImageMissingSnackbar()
            }
        }

        adapter.onNextPage = {
            photosListVM.retrievePhotosNextPage()
        }

        rvPhotos.adapter = adapter
    }

    private fun showNoResultsSnackbar() {
        Snackbar.make(clMainFragment, "No results found!", Snackbar.LENGTH_SHORT).show()
    }
    private fun showImageMissingSnackbar() {
        Snackbar.make(clMainFragment, "Image doesn't exist!", Snackbar.LENGTH_SHORT).show()
    }
}