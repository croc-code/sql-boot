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
import com.github.mgramin.sqlboot.model.connection.DbConnectionList
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.composite.FsResourceTypes
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper
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
import java.util.ArrayList
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
    private lateinit var dbConnectionList: DbConnectionList


    @RequestMapping(value = ["/api/{connectionName}/types"])
    fun types(@PathVariable connectionName: String): List<ResourceType>? {
        return FsResourceTypes(dbConnectionList.getConnectionsByMask(connectionName), FakeUri()).resourceTypes()
    }


    @RequestMapping(value = ["/api/{connectionName}/**"], method = [GET, POST])
    fun getResourcesHeadersJson(
            request: HttpServletRequest,
            @PathVariable connectionName: String
    ): ResponseEntity<List<Map<String, Any>>> {
        val uriString = request.servletPath
                .split("/")
                .asSequence()
                .filter { it.isNotEmpty() }
                .filter { it != "api" }
                .filter { it != "headers" }
                .joinToString(separator = "/") { it }
        val uri: Uri = SqlPlaceholdersWrapper(DbUri(parseUri(uriString, request)))
        return getListResponseEntityHeaders(uri, connectionName)
    }

    private fun getListResponseEntityHeaders(
            uri: Uri,
            connectionName: String
    ): ResponseEntity<List<Map<String, Any>>> {
        val connections = dbConnectionList.getConnectionsByMask(connectionName)
        try {
            val headers = FsResourceTypes(connections, uri)
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


    @RequestMapping(value = ["/api/meta/{connectionName}/**"], method = [GET, POST])
    fun getResourceMetadata(
            request: HttpServletRequest,
            @PathVariable connectionName: String
    ): ResponseEntity<List<Metadata>> {
        val uriString = request.servletPath
                .split("/")
                .filter { it.isNotEmpty() }
                .filter { it != "api" }
                .filter { it != "meta" }
                .joinToString(separator = "/") { it }
        val uri = SqlPlaceholdersWrapper(DbUri(parseUri(uriString, request)))
        return responseEntity(connectionName, uri)
    }


    private fun responseEntity(connectionName: String, uri: Uri): ResponseEntity<List<Metadata>> {
        val fsResourceTypes = FsResourceTypes(
                listOf(dbConnectionList.getConnectionByName(connectionName)), uri)
        val resourceType = fsResourceTypes
                .resourceTypes()
                .stream()
                .filter { v -> v.name().equals(uri.type(), ignoreCase = true) }
                .findAny()
                .orElse(null) ?: return ResponseEntity(ArrayList(), HttpStatus.NO_CONTENT)
        return ResponseEntity(resourceType.metaData(uri), HttpStatus.OK)
    }


    private fun parseUri(path: String, request: HttpServletRequest): String {
        return if (request.queryString == null || request.queryString.isEmpty()) {
            path
        } else {
            path + "?" + request.queryString
        }
    }

}
