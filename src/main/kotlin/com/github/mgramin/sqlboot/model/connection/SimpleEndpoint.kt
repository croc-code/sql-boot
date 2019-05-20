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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.tomcat.jdbc.pool.DataSource

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: f221782080d430e77aed80ef8446745687c350f4 $
 * @since 0.1
 */
open class SimpleEndpoint(
        var name: String? = null,
        var host: String? = null,
        var properties: String? = null
) : Endpoint {

    override fun name() = name!!

    override fun host() = host!!

    override fun properties(): Map<String, Any> =
            Gson().fromJson(properties, object : TypeToken<Map<String, Any>>() {}.type)

    fun getProperties(): Map<String, Any> =
            Gson().fromJson(properties, object : TypeToken<Map<String, Any>>() {}.type)

    private var dataSource: DataSource? = null

    @JsonIgnore
    override fun getDataSource(): DataSource {
        return if (dataSource != null) {
            dataSource!!
        } else {
            val dataSourceNew = DataSource()
            dataSourceNew.driverClassName = properties()["jdbc.driver.class.name"].toString()
            dataSourceNew.url = properties()["jdbc.url"].toString()
            dataSourceNew.username = properties()["jdbc.user"].toString()
            dataSourceNew.password = properties()["jdbc.password"].toString()
            dataSource = dataSourceNew
            dataSourceNew
        }
    }

}
