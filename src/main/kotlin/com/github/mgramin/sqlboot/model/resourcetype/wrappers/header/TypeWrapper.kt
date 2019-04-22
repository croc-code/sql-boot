package com.github.mgramin.sqlboot.model.resourcetype.wrappers.header

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import reactor.core.publisher.Flux


class TypeWrapper(private val origin: ResourceType) : ResourceType {

    override fun aliases() = origin.aliases()

    override fun path() = origin.path()

    override fun metaData(uri: Uri) = origin.metaData(uri)

    override fun read(uri: Uri): Flux<DbResource> =
            origin
                    .read(uri)
                    .map {
                        return@map object : DbResource {
                            override fun name() = it.name()
                            override fun type() = it.type()
                            override fun dbUri() = it.dbUri()
                            override fun body() = it.body()
                            override fun headers(): Map<String, Any> {
                                val newHeaders = it.headers().toMutableMap()
                                metaData(uri).forEach { m ->
                                    val type = m.properties()["type"].toString()
                                    if (type.equals("number", ignoreCase = true)) {
                                        newHeaders[m.name()] = newHeaders[m.name()].toString().toInt()
                                    }
                                }
                                return newHeaders
                            }

                        }
                    }

}