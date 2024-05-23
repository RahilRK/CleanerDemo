package com.base.hilt.model.response


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class MovieListResponse(
    @JsonProperty("movie")
    var movieList: ArrayList<Movie>? = null
)

data class Movie(
    @JsonProperty("category")
    var category: String? = null,
    @JsonProperty("desc")
    var desc: String? = null,
    @JsonProperty("imageUrl")
    var imageUrl: String? = null,
    @JsonProperty("name")
    var name: String? = null
)