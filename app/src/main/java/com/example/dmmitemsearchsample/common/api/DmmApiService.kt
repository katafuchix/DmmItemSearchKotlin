package com.example.dmmitemsearchsample.common.api

import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.model.*
import io.reactivex.Observable
import retrofit2.http.*

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
    ): Observable<DmmApiResultItem>

    @GET("affiliate/v3/ActressSearch?output=${Constants.output}&api_id=${Constants.api_id}&affiliate_id=${Constants.affiliate_id}")
    fun getActresses(
        @Query("offset") offset: Int,
        @Query("hits") hits: Int,
        @Query("sort") sort: String,
        @Query("keyword") keyword: String
    ): Observable<DmmApiResultActress>
}