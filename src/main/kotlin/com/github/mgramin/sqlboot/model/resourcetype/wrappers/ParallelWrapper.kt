package com.github.mgramin.sqlboot.model.resourcetype.wrappers

import com.fasterxml.jackson.databind.JsonNode
import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import reactor.core.publisher.Flux

class ParallelWrapper(private val origins: List<ResourceType>) : ResourceType {

    override fun aliases(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun path(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun metaData(uri: Uri): List<Metadata> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(uri: Uri): Flux<DbResource> =
            Flux.merge(
                    origins
                            .filter { it.name().matches(wildcardToRegex(uri)) }
                            .map { it.read(uri) })

    override fun toJson(): JsonNode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun wildcardToRegex(uri: Uri) = uri.type().replace("?", ".?").replace("*", ".*?").toRegex()

}