package com.github.mgramin.sqlboot.model.connection

class MaskedEndpointListWrapper(private val origin: EndpointList) : EndpointList {

    override fun getAll(): List<Endpoint> =
            origin.getAll().map {
                return@map SimpleEndpoint(
                        it.name(), it.host(), it.confDir(),
                        it.properties().filterNot { itt -> itt.key.contains("password", true) })
            }

    override fun getByName(name: String): Endpoint {
        val origin = origin.getByName(name)
        return SimpleEndpoint(
                origin.name(), origin.host(), origin.confDir(),
                origin.properties().filterNot { it.key.contains("password", true) })
    }

    override fun getByMask(mask: String): List<Endpoint> =
            origin.getByMask(mask).map {
                SimpleEndpoint(
                        it.name(), it.host(), it.confDir(),
                        it.properties().filterNot { itt -> itt.key.contains("password", true) })
            }

}