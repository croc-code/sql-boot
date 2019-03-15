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
import org.slf4j.LoggerFactory
import javax.sql.DataSource

class CheckedConnection(private val origin: DbConnection) : DbConnection {

    override fun getProperties() = origin.getProperties()

    private val health: String

    private val logger = LoggerFactory.getLogger(this::class.java)

    @JsonIgnore
    override fun getDataSource(): DataSource {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName() = origin.getName()

    override fun getHealth(): String {
        return health
    }

//    override fun getDataSource() = origin.getDataSource()

    override fun paginationQueryTemplate() = origin.paginationQueryTemplate()

    init {
        health = try {
            logger.info("Check connection...")
            origin.getDataSource().connection
            "Ok"
        } catch (e: Exception) {
            e.message.toString()
        }
    }

}