package com.github.mgramin.sqlboot.model.connection

interface EndpointList {

    fun getAll(): List<Endpoint>

    fun getByName(name: String): Endpoint

    fun getByMask(mask: String): List<Endpoint>

}