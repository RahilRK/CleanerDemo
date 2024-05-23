package com.base.hilt.network

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.annotations.Nullable
import org.json.JSONObject

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
open class ResponseData<T> {
    @JsonProperty("meta")
    val meta: Meta? = null

    @Nullable
    @JsonProperty("errors")
    var jsonError: JSONObject? = null

    var data: T? = null

    var listOfData: List<T>? = null


    /**
    * JsonAnySetter : This Annotation is used to handle some json properties manually
     * if you have not define any property of your json string as @JsonProperty("key"), you will get it in this method
     * if the value of that property is JsonArray, you will get List<*> here
     * and if the value of that property is JsonObject, you will get HashMap here
     * we have used this annotation to overcome the issue of getting JsonObject in body property and also sometime it gives JsonArray too*/
    @JsonAnySetter
    fun setData(key: String?, value: Any?) {
        if (key == "data" || key == "Data") {
            when (value) {
                is List<*> -> {
                    listOfData = ObjectMapper().convertValue<List<T>>(value, object : TypeReference<List<T>>(){})
                }
                else -> {
                    data = ObjectMapper().convertValue<T>(value, object : TypeReference<T>(){})
                }
            }
        }
    }


    override fun toString(): String {
        return "ResponseData(data=$data, listOfData=$listOfData, head=$meta)"
    }

}
