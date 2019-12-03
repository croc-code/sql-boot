package com.github.mgramin.sqlboot.model.resourcetype

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ResourceTypeJsonSerializer : JsonSerializer<ResourceType>() {

    override fun serialize(resourceType: ResourceType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeEmbeddedObject(resourceType.toJson())
    }

}