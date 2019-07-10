package com.example.dmmitemsearchsample.common.repository

import com.example.dmmitemsearchsample.common.Constants
import com.example.dmmitemsearchsample.common.api.DmmApiService
import com.example.dmmitemsearchsample.common.api.RetrofitServiceGenerator
import com.example.dmmitemsearchsample.model.*
import io.reactivex.Observable

fun RetrofitServiceGenerator.createCategoriesApiService(): DmmApiService {
    val apiUrl = Constants.BASE_URL
    val client = RetrofitServiceGenerator.builderHttpClient()
    //val gson = com.google.gson.GsonBuilder().setFieldNamingPolicy(com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    val gson = com.google.gson.GsonBuilder().setFieldNamingPolicy(
        com.google.gson.FieldNamingPolicy.IDENTITY).create()

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(client)
        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create(gson))
        .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
        .build()
    return retrofit.create(DmmApiService::class.java)
}

interface DmmApiRepository {
    fun getItems(
        site: String,
        service: String,
        offset: Int,
        hits: Int,
        floor: String,
        sort: String,
        keyword: String
    ): Observable<DmmApiResultItem>

    fun getActresses(
        offset: Int,
        hits: Int,
        floor: String,
        sort: String,
        keyword: String
    ): Observable<DmmApiResultActress>
}

class DmmApiRepositoryImpl : DmmApiRepository {
    override fun getItems(
        site: String,
        service: String,
        offset: Int,
        hits: Int,
        floor: String,
        sort: String,
        keyword: String

    ): Observable<DmmApiResultItem> {

        return RetrofitServiceGenerator.createCategoriesApiService()
            .getItems(
                site,
                service,
                offset,
                hits,
                floor,
                sort,
                keyword
            )

    }

    override fun getActresses(
        offset: Int,
        hits: Int,
        floor: String,
        sort: String,
        keyword: String
    ): Observable<DmmApiResultActress> {
        return RetrofitServiceGenerator.createCategoriesApiService()
            .getActresses(
                offset,
                hits,
                floor,
                sort,
                keyword
            )

    }
}