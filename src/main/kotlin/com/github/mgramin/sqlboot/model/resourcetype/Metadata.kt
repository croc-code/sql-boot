package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.HashMap

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Metadata(
        private val name: String,
        private val type: String,
        private val description: String
) : Comparable<Metadata> {

    override fun compareTo(other: Metadata): Int = if (name > other.name()) -1 else 1

    private val properties: MutableMap<String, Any>

    constructor(name: String, description: String) : this(name, "String", description)

    init {
        this.properties = HashMap()
        try {
            val map: Map<String, Any> = Gson().fromJson(description, object : TypeToken<Map<String, Any>>() {}.type)
            this.properties.putAll(map)
            this.properties["key"] = name.replace("@", "")
        } catch (ignored: Exception) {
            this.properties.clear()
            val prop = HashMap<String, Any>()
            prop["key"] = name.replace("@", "")
            prop["label"] = name.replace("@", "")
            prop["description"] = description
            this.properties.putAll(prop)
        }
    }

    fun name(): String {
        return name
    }

    fun description(): String {
        return description
    }

    fun properties(): Map<String, Any> {
        return properties
    }
}