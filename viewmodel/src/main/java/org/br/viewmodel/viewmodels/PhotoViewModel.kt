package org.br.viewmodel.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.PhotosRepository
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var photosRepository: PhotosRepository

    lateinit var photo: PhotoViewModelEntity

    // Used to push photo bitmap to UI
    private val photoBitmap = MutableLiveData<Bitmap>()
    fun getPhotoBitmap(): LiveData<Bitmap> = photoBitmap

    // Used to push saved status to UI
    private val photoSavedLiveData = MutableLiveData<Boolean>()
    fun getPhotoSavedLiveData(): LiveData<Boolean> = photoSavedLiveData

    fun init(photo: PhotoViewModelEntity) {
        init(photo = photo, testPhotosRepository = null)
    }

    fun init(photo: PhotoViewModelEntity, testPhotosRepository: PhotosRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        this.photo = photo

        setupPhotoSavedObservers()
    }

    private fun setupPhotoSavedObservers() {
        // If photo is saved notify UI
        photosRepository.photoSaved.subscribe {
            photoSavedLiveData.postValue(it)
        }.addTo(disposables)
    }

    fun postPhoto(bitmap: Bitmap) {
        photoBitmap.postValue(bitmap)
    }

    fun savePhoto() {
        val thumbBitmap = photo.thumbBitmap
        val originalBitmap = photo.originalBitmap

        // Only save if both Bitmaps are not null
        if (thumbBitmap != null && originalBitmap != null) {
            photosRepository.savePhoto(
                    photo.id,
                    thumbBitmap,
                    originalBitmap
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}