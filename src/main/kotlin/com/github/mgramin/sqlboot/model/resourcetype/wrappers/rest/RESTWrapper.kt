package com.github.mgramin.sqlboot.model.resourcetype.wrappers.rest

import com.github.mgramin.sqlboot.model.connection.SimpleEndpointList
import com.github.mgramin.sqlboot.model.dialect.DbDialectList
import com.github.mgramin.sqlboot.model.resourcetype.impl.FsResourceType
import com.google.gson.JsonArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@EnableAutoConfiguration
@CrossOrigin
class RESTWrapper {

    @Autowired
    private lateinit var endpointList: SimpleEndpointList

    @Autowired
    private lateinit var dbDialectList: DbDialectList


    @RequestMapping(value = ["/api/{connectionName}/types"])
    fun types(@PathVariable connectionName: String): String {
        val jsonArray = JsonArray()
        FsResourceType(endpointList.getByMask(connectionName), dbDialectList.dialects)
                .resourceTypes()
                .forEach { jsonArray.add(it.toJson()) }
        return jsonArray.toString()
    }

    @RequestMapping(value = ["/api/{connectionName}/types/{typeMask}"])
    fun typesByMask(@PathVariable connectionName: String, @PathVariable typeMask: String): String {
        val jsonArray = JsonArray()
        FsResourceType(endpointList.getByMask(connectionName), dbDialectList.dialects)
                .resourceTypes()
                .filter { it.name().matches(wildcardToRegex(typeMask)) }
                .forEach { jsonArray.add(it.toJson()) }
        return jsonArray.toString()
    }

    private fun wildcardToRegex(name: String) = name.replace("?", ".?").replace("*", ".*?").toRegex()

}