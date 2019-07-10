package com.example.dmmitemsearchsample.model

data class SearchData(
    val result: ItemsData
)

data class ItemsData(
    val items: List<Item>?,
    val status: Int,
    val result_count: Int,
    val total_count: Int,
    val first_position: Int
)

data class Item (
    var service_code: String,
    var service_name: String,
    var floor_code: String,
    var floor_name: String,
    var category_name: String,
    var content_id: String,
    var product_id: String,
    var title: String,
    var volume: String,
    var imageURL: ImageURL?,
    var review: Review?,
    var URL: String,
    var affiliateURL: String
)

data class DmmApiResult(
    val result: ItemsData
)

data class ImageURL(
    var list: String?,
    var small: String?,
    var large: String?
)

data class Review(
    var count: Int,
    var average: String
)
