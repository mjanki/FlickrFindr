package org.br.network.clients

import io.reactivex.Observable
import org.br.network.models.PhotoSizesResultNetworkEntity
import org.br.network.models.SearchResultNetworkEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrClient {
    @GET("/services/rest/?method=flickr.photos.search&api_key=1508443e49213ff84d566777dc211f2a&per_page=25&format=json&nojsoncallback=1")
    fun getPhotos(
            @Query("text") text: String,
            @Query("page") page: Long = 1
    ): Observable<Response<SearchResultNetworkEntity>>

    @GET("/services/rest/?method=flickr.photos.getSizes&api_key=1508443e49213ff84d566777dc211f2a&format=json&nojsoncallback=1")
    fun getPhotoSizes(
            @Query("photo_id") photoId: String
    ): Observable<Response<PhotoSizesResultNetworkEntity>>
}