package org.br.network.daos

import io.reactivex.subjects.PublishSubject
import org.br.network.clients.FlickrClient
import org.br.network.models.PhotoSizesResultNetworkEntity
import org.br.network.models.SearchResultNetworkEntity
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class PhotosNetworkDao : BaseNetworkDao() {
    private var requestInterface: FlickrClient = Retrofit.Builder()
            .baseUrl("https://www.flickr.com")
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(FlickrClient::class.java)

    fun setRequestInterface(flickrClient: FlickrClient) {
        requestInterface = flickrClient
    }

    // region All Photos Info

    // Used to inform observers if it's currently retrieving photos
    val isRetrievingPhotos = PublishSubject.create<Boolean>()

    val retrievedPhotos = PublishSubject.create<Response<SearchResultNetworkEntity>>()

    fun retrievePhotos(text: String, page: Long = 1) {
        isRetrievingPhotos.onNext(true)
        executeNetworkCall(
                observable = requestInterface.getPhotos(text, page),
                action = "Fetching photos from Flickr",
                onSuccess = {
                    retrievedPhotos.onNext(it)
                },
                onComplete = {
                    isRetrievingPhotos.onNext(false)
                }
        )
    }

    // endregion All Photos Info

    // region Photo Sizes

    val retrievedPhotoSizes = PublishSubject.create<Response<PhotoSizesResultNetworkEntity>>()

    fun retrievePhotoSize(photoId: String) {
        executeNetworkCall(
                observable = requestInterface.getPhotoSizes(photoId),
                action = "Fetching photo sizes from Flickr",
                onSuccess = {
                    it.body()?.let { result ->
                        result.photoId = photoId
                        retrievedPhotoSizes.onNext(it)
                    }
                }
        )
    }

    // endregion Photo Sizes
}