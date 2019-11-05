/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, CROC Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.mgramin.sqlboot.rest.controllers

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleEndpointList
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

    @RequestMapping(value = ["/api/{connection}/{type}"], method = [GET, POST])
    fun getResourcesHeadersJson(@PathVariable connection: String,
                                @PathVariable type: String,
                                request: HttpServletRequest): ResponseEntity<List<Map<String, Any>>> {
        val uriString = if (request.queryString != null) {
            "$connection/$type?${request.queryString}"
        } else {
            "$connection/$type"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }

    @RequestMapping(value = ["/api/{connection}/{type}/{path}"], method = [GET, POST])
    fun getResourcesHeadersJson(@PathVariable connection: String,
                                @PathVariable type: String,
                                @PathVariable path: String,
                                request: HttpServletRequest): ResponseEntity<List<Map<String, Any>>> {
        val uriString = if (request.queryString != null) {
            "$connection/$type/$path?${request.queryString}"
        } else {
            "$connection/$type/$path"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }

    private fun getListResponseEntityHeaders(uri: Uri): ResponseEntity<List<Map<String, Any>>> {
        try {
            val connections = endpointList.getByMask(uri.connection())
            val headers = FsResourceType(connections, dbDialectList.dialects)
                    .read(uri)
                    .map { it.headers() }
                    .collectList()
                    .block()
            return ResponseEntity(headers, HttpStatus.OK)
        } catch (e: BootException) {
            if (e.errorCode == 404) {
                return ResponseEntity(emptyList(), HttpStatus.NOT_FOUND)
            }
            return ResponseEntity(emptyList(), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}
