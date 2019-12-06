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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers

import com.github.mgramin.sqlboot.model.connection.SimpleEndpointList
import com.github.mgramin.sqlboot.model.dialect.DbDialectList
import com.github.mgramin.sqlboot.model.resourcetypelist.impl.FsResourceTypeList
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.wrappers.SqlPlaceholdersWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import javax.servlet.http.HttpServletRequest

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 69e9609bd238163b6b97346900c17bb6efd0c037 $
 * @since 0.1
 */
@RestController("DbResourceRestWrapper")
@ComponentScan(basePackages = ["com.github.mgramin.sqlboot"])
@EnableAutoConfiguration
@CrossOrigin
class RestWrapper {

    @Autowired
    private lateinit var endpointList: SimpleEndpointList

    @Autowired
    private lateinit var dbDialectList: DbDialectList

    @RequestMapping(
            value = ["/api/{connection}/{type}"],
            method = [GET, POST],
            consumes = [MediaType.TEXT_EVENT_STREAM_VALUE],
            produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getResourcesFlux(@PathVariable connection: String,
                         @PathVariable type: String,
                         request: HttpServletRequest): Flux<Map<String, Any>> {
        val uriString = if (request.queryString != null) {
            "$connection/$type?${request.queryString}"
        } else {
            "$connection/$type"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }


    @RequestMapping(
            value = ["/api/{connection}/{type}/{path}"],
            method = [GET, POST],
            consumes = [MediaType.TEXT_EVENT_STREAM_VALUE],
            produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getResourcesFlux(@PathVariable connection: String,
                         @PathVariable type: String,
                         @PathVariable path: String,
                         request: HttpServletRequest): Flux<Map<String, Any>> {
        val uriString = if (request.queryString != null) {
            "$connection/$type/$path?${request.queryString}"
        } else {
            "$connection/$type/$path"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString)))
    }


    @RequestMapping(value = ["/api/{connection}/{type}"], method = [GET, POST])
    fun getResourcesList(@PathVariable connection: String,
                         @PathVariable type: String,
                         request: HttpServletRequest): List<Map<String, Any>>? {
        val uriString = if (request.queryString != null) {
            "$connection/$type?${request.queryString}"
        } else {
            "$connection/$type"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString))).collectList().block()
    }

    @RequestMapping(value = ["/api/{connection}/{type}/{path}"], method = [GET, POST])
    fun getResourcesList(@PathVariable connection: String,
                         @PathVariable type: String,
                         @PathVariable path: String,
                         request: HttpServletRequest): List<Map<String, Any>>? {
        val uriString = if (request.queryString != null) {
            "$connection/$type/$path?${request.queryString}"
        } else {
            "$connection/$type/$path"
        }
        return getListResponseEntityHeaders(SqlPlaceholdersWrapper(DbUri(uriString))).collectList().block()
    }

    private fun getListResponseEntityHeaders(uri: Uri): Flux<Map<String, Any>> {
        val connections = endpointList.getByMask(uri.connection())
        return ParallelWrapper(FsResourceTypeList(connections, dbDialectList.dialects).types())
                .read(uri)
                .map { it.headers() }
    }

}
