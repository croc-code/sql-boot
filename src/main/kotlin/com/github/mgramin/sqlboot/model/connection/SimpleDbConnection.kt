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

package com.github.mgramin.sqlboot.model.connection

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.tomcat.jdbc.pool.DataSource
import org.json.JSONObject
import org.springframework.core.io.Resource

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: f221782080d430e77aed80ef8446745687c350f4 $
 * @since 0.1
 */
open class SimpleDbConnection : DbConnection {

    override fun paginationQueryTemplate() = this.paginationQueryTemplate!!

    override fun getName(): String {
        return name!!
    }

    fun setName(name: String) {
        this.name = name
    }

    private var name: String? = null


    @JsonIgnore // TODO fix json serialization for Resource class
    var baseFolder: Resource? = null
    var url: String? = null
    var user: String? = null
    @JsonIgnore
    var password: String? = null
    var driverClassName: String? = null
    private var properties: String? = null

    var paginationQueryTemplate: String? = null


    private var dataSource: DataSource? = null

    override fun getHealth() = "UKNOWN"

    constructor()
    constructor(name: String? = null, baseFolder: Resource? = null, url: String? = null,
                user: String? = null, password: String? = null, driverClassName: String? = null,
                properties: String? = null, paginationQueryTemplate: String? = null) {
        this.name = name
        this.baseFolder = baseFolder
        this.url = url
        this.user = user
        this.password = password
        this.driverClassName = driverClassName
        this.properties = properties
        this.paginationQueryTemplate = paginationQueryTemplate
    }

    override fun getProperties(): Map<String, Any> {
        return JSONObject(properties).toMap()
    }

    fun setProperties(properties: String) {
        this.properties = properties
    }

    @JsonIgnore
    override fun getDataSource(): DataSource {
        if (dataSource != null) {
            return dataSource!!
        } else {
            val dataSourceNew = DataSource()
            if (driverClassName != null) {
                dataSourceNew.driverClassName = driverClassName
            }
            if (url != null) {
                dataSourceNew.url = url
            }
            if (user != null) {
                dataSourceNew.username = user
            }
            if (password != null) {
                dataSourceNew.password = password
            }
            dataSource = dataSourceNew
            return dataSourceNew
        }
    }

}
