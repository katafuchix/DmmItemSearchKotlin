package com.example.dmmitemsearchsample.common.api

import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.model.DmmApiResult
import io.reactivex.Observable
import retrofit2.http.*

/*
affiliate/v3/ItemList?output=${ Constants.output }&site=DMM.com&offset=21&
service=digital&hits=10&keyword=佐藤&page=2&floor=idol&sort=date&
api_id=Bu045c0gVX51t1zMwn22&affiliate_id=modemode-990"
*/
interface DmmApiService {
    @GET("affiliate/v3/ItemList?output=${Constants.output}&api_id=${Constants.api_id}&affiliate_id=${Constants.affiliate_id}")
    fun getItems(
        @Query("site") site: String,
        @Query("service") service: String,
        @Query("offset") offset: Int,
        @Query("hits") hits: Int,
        @Query("floor") floor: String,
        @Query("sort") sort: String,
        @Query("keyword") keyword: String
    ): Observable<DmmApiResult>
}