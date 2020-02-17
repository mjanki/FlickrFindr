package org.br.viewmodel.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.br.repository.repositories.PhotosRepository
import org.br.viewmodel.mappers.PhotoViewModelRepoMapper
import org.br.viewmodel.models.PhotoViewModelEntity

class PhotoViewModel(application: Application) : BaseViewModel(application) {
    private lateinit var photosRepository: PhotosRepository

    private val photoViewModelRepoMapper = PhotoViewModelRepoMapper()

    private val photoLiveData = MutableLiveData<PhotoViewModelEntity>()
    fun getPhotoLiveData(): LiveData<PhotoViewModelEntity> = photoLiveData

    lateinit var photoId: String

    // Used to push photo bitmap to UI
    private val photoBitmap = MutableLiveData<Bitmap>()
    fun getPhotoBitmap(): LiveData<Bitmap> = photoBitmap

    // Used to push saved status to UI
    private val photoSavedLiveData = MutableLiveData<Boolean>()
    fun getPhotoSavedLiveData(): LiveData<Boolean> = photoSavedLiveData

    fun init(photoId: String) {
        init(photoId = photoId, testPhotosRepository = null)
    }

    fun init(photoId: String, testPhotosRepository: PhotosRepository? = null) {
        photosRepository = testPhotosRepository ?: PhotosRepository(getApplication())
        photosRepository.init()

        this.photoId = photoId

        setupPhotoObservers()
        setupPhotoSavedObservers()
    }

    private fun setupPhotoObservers() {
        photosRepository.getPhotoById(photoId).subscribe {
            if (it.isEmpty()) { return@subscribe }

            photoLiveData.postValue(
                    photoViewModelRepoMapper.upstream(it.first())
            )
        }.addTo(disposables)
    }

    private fun setupPhotoSavedObservers() {
        // If photo is saved notify UI
        photosRepository.photoSaved.subscribe {
            photoSavedLiveData.postValue(it)
        }.addTo(disposables)
    }

    fun postPhoto(bitmap: Bitmap) {
        photoLiveData.value?.originalBitmap = bitmap
        photoBitmap.postValue(bitmap)
    }

    fun savePhotoThumbnail(bitmap: Bitmap) {
        photoLiveData.value?.thumbBitmap = bitmap
    }

    fun savePhoto() {
        val thumbBitmap = photoLiveData.value?.thumbBitmap
        val originalBitmap = photoLiveData.value?.originalBitmap

        // Only save if both Bitmaps are not null
        if (thumbBitmap != null && originalBitmap != null) {
            photoLiveData.value?.let {
                photosRepository.savePhoto(
                        it.id,
                        thumbBitmap,
                        originalBitmap
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        photosRepository.clearDisposables()
    }
}