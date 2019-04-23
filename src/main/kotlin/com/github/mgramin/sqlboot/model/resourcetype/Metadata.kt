package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.HashMap

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Metadata(
        private val name: String,
        private var type: String,
        private val description: String
) : Comparable<Metadata> {

    private val properties: MutableMap<String, Any>

    constructor(name: String, description: String) : this(name, "String", description)

    init {
        this.properties = HashMap()
        try {
            val map: Map<String, Any> = Gson().fromJson(description, object : TypeToken<Map<String, Any>>() {}.type)
            this.properties.putAll(map)
            this.properties["key"] = name.replace("@", "")
            if (map.containsKey("type")) {
                this.type = map["type"].toString()
            } else {
                this.type = "String"
            }
        } catch (ignored: Exception) {
            this.properties.clear()
            val prop = HashMap<String, Any>()
            prop["key"] = name.replace("@", "")
            prop["label"] = name.replace("@", "")
            prop["description"] = description
            prop["type"] = type
            this.properties.putAll(prop)
        }
    }

    fun name(): String = name

    fun description(): String = description

    fun properties(): Map<String, Any> = properties

    /**
     * Get as JSON
     */
    fun toJson(): JsonObject {
        val jsonObject = JsonObject()
        val toJson: JsonElement = Gson().toJsonTree (properties)
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("description", description)
        jsonObject.add("properties", toJson)
        return jsonObject
    }

    override fun compareTo(other: Metadata): Int = if (name > other.name()) -1 else 1

}