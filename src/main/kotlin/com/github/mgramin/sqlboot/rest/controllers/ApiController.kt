/*
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2019 Maksim Gramin
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.rest.controllers

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.EndpointList
import com.github.mgramin.sqlboot.model.dialect.DbDialectList
import com.github.mgramin.sqlboot.model.resourcetype.impl.FsResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper
import com.google.gson.JsonArray
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 69e9609bd238163b6b97346900c17bb6efd0c037 $
 * @since 0.1
 */
@RestController
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@EnableAutoConfiguration
@CrossOrigin
class ApiController {

    @Autowired
    private lateinit var endpointList: EndpointList

    @Autowired
    private lateinit var dbDialectList: DbDialectList


    @RequestMapping(value = ["/api/{connectionName}/types"])
    fun types(@PathVariable connectionName: String): String {
        val jsonArray = JsonArray()
        FsResourceType(endpointList.getByMask(connectionName), emptyList())
                .resourceTypes()
                .forEach { jsonArray.add(it.toJson()) }
        return jsonArray.toString()
    }

    @RequestMapping(value = ["/api/{connectionName}/types/{typeMask}"])
    fun typesByMask(@PathVariable connectionName: String, @PathVariable typeMask: String): String {
        val jsonArray = JsonArray()
        FsResourceType(endpointList.getByMask(connectionName), emptyList())
                .resourceTypes()
                .filter { it.name().matches(wildcardToRegex(typeMask)) }
                .forEach { jsonArray.add(it.toJson()) }
        return jsonArray.toString()
    }

    private fun wildcardToRegex(name: String) = name.replace("?", ".?").replace("*", ".*?").toRegex()

    @RequestMapping(value = ["/api/meta/{connection}/{type}"], method = [GET, POST])
    fun getResourceMetadata(@PathVariable connection: String,
                            @PathVariable type: String,
                            request: HttpServletRequest): ResponseEntity<String> {
        val jsonArray = JsonArray()
        val uri = SqlPlaceholdersWrapper(
                DbUri("$connection/$type"))
        FsResourceType(listOf(endpointList.getByName(uri.connection())), emptyList())
                .resourceTypes()
                .asSequence()
                .filter { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .map { it.metaData(uri) }
                .first()
                .forEach { jsonArray.add(it.toJson()) }
        return ResponseEntity(jsonArray.toString(), HttpStatus.OK)
    }

    @RequestMapping(value = ["/api/meta/{connection}/{type}/{path}"], method = [GET, POST])
    fun getResourceMetadata(@PathVariable connection: String,
                            @PathVariable type: String,
                            @PathVariable path: String,
                            request: HttpServletRequest): ResponseEntity<String> {
        val jsonArray = JsonArray()
        val uri = SqlPlaceholdersWrapper(
                DbUri("$connection/$type/$path"))
        FsResourceType(listOf(endpointList.getByName(uri.connection())), emptyList())
                .resourceTypes()
                .asSequence()
                .filter { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .map { it.metaData(uri) }
                .first()
                .forEach { jsonArray.add(it.toJson()) }
        return ResponseEntity(jsonArray.toString(), HttpStatus.OK)
    }


    @RequestMapping(value = ["/api/{connection}/{type}"], method = [GET, POST])
    fun getResourcesHeadersJson(@PathVariable connection: String,
                                @PathVariable type: String,
                                request: HttpServletRequest) =
            getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri("$connection/$type")))

    @RequestMapping(value = ["/api/{connection}/{type}/{path}"], method = [GET, POST])
    fun getResourcesHeadersJson(@PathVariable connection: String,
                                @PathVariable type: String,
                                @PathVariable path: String,
                                request: HttpServletRequest) =
            getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri("$connection/$type/$path")))

    private fun getListResponseEntityHeaders(uri: Uri): ResponseEntity<List<Map<String, Any>>> {
        val connections = endpointList.getByMask(uri.connection())
        try {
            val headers = FsResourceType(connections, dbDialectList.dialects)
                    .read(uri)
                    .map { it.headers() }
                    .collectList()
                    .block()
            return if (headers.isEmpty()) {
                ResponseEntity(headers, HttpStatus.NO_CONTENT)
            } else {
                ResponseEntity(headers, HttpStatus.OK)
            }
        } catch (e: BootException) {
            if (e.errorCode == 404) {
                return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
            }
            return ResponseEntity(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}
