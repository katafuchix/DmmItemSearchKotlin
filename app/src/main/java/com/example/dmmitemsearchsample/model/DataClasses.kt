package com.example.dmmitemsearchsample.model

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

data class DmmApiResultItem(
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

data class DmmApiResultActress(
    val result: ActressesData
)

data class ActressesData(
    val actress: List<Actress>?,
    val status: Int,
    val result_count: Int,          // API では String
    val total_count: Int,
    val first_position: Int         // API では String
)

data class Actress (
    var id: String,
    var name: String,
    var ruby: String,
    var bust: String?,
    var waist: String?,
    var hip: String?,
    var height: String?,
    var birthday: String?,
    var blood_type: String?,
    var hobby: String?,
    var prefectures: String?,
    var listURL: ListURL?,
    var imageURL: ImageURL?
)

data class ListURL(
    var digital: String?,
    var monthly: String?,
    var ppm: String?,
    var mono: String?,
    var rental: String?
)
