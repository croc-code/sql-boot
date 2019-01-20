package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.annotation.JsonAutoDetect
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class Metadata (
    private val name: String,
    private val type: String,
    private val description: String
) : Comparable<Metadata> {

    override fun compareTo(other: Metadata): Int {
        if (name > other.name()) {
            return -1
        }
        return 1
    }

    private val properties: MutableMap<String, Any>

    constructor(name: String, description: String) : this(name, "String", description)

    init {
        this.properties = HashMap()
        try {
            this.properties.putAll(JSONObject(description).toMap())
            this.properties["key"] = name.replace("@", "")
        } catch (ignored: JSONException) {
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