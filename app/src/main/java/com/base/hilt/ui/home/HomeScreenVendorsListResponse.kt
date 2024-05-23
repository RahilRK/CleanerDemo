package com.base.hilt.ui.home

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class HomeScreenVendorsListResponse {

    @JsonProperty("total")
    val total: Int = 0

    @JsonProperty("vendors")
    val vendors: ArrayList<Vendor>? = null
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Vendor : Serializable {
    @JsonProperty("vendor_name")
    var vendorName: String? = null

    @JsonProperty("vendor_image")
    var vendorImage: String? = null

    @JsonProperty("uuid")
    val uuid: String? = null

    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("rating")
    val rating: Int = 0

    @JsonProperty("cuisine")
    private val cuisine: List<Cuisine>? = null

    @JsonProperty("menu_list")
    private val menuList: List<MenuItem>? = null

    @JsonProperty("image_url")
    var imageUrl: String? = null

    fun getFinalRating(): String {
        return rating.toString() ?: "0"
    }

    fun getFullImageUrl(): String {
        return if (imageUrl != null && vendorImage != null)
            return imageUrl + vendorImage
        else
            ""
    }

    fun getCuisineDetails(): String {
        var data = ""
        return if (cuisine != null) {
            for (int in cuisine.indices) {
                if (cuisine[int].cuisineName != null && cuisine[int].cuisineName?.trim()
                        ?.isNotEmpty()!!
                ) {
                    data = if (data.isNotEmpty())
                        data + ", " + cuisine[int].cuisineName?.trim().toString()
                    else
                        cuisine[int].cuisineName?.trim().toString()
                }
            }
            data
        } else data
    }

    fun getMenuDetails(): String {
        var data = ""
        return if (menuList != null) {
            for (int in menuList.indices) {
                if (menuList[int].menuName != null && menuList[int].menuName?.trim()
                        ?.isNotEmpty()!!
                ) {
                    data = if (data.isNotEmpty())
                        data + ", " + menuList[int].menuName?.trim().toString()
                    else
                        menuList[int].menuName?.trim().toString()
                }
            }
            data
        } else data
    }

    var isLikeView = true

    var vendorType = ""

}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class Cuisine : Serializable {

    @JsonProperty("cuisine_name")
    val cuisineName: String? = null


    @JsonProperty("cuisine_id")
    val cuisineId: String? = null

    @JsonProperty("description")
    val description: String? = null
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class MenuItem : Serializable {

    @JsonProperty("id")
    val id: String? = null


    @JsonProperty("menu_name")
    val menuName: String? = null

    @JsonProperty("sort_number")
    val sort_number: String? = null
}