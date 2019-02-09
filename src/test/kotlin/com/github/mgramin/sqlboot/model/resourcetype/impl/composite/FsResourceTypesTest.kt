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

package com.github.mgramin.sqlboot.model.resourcetype.impl.composite

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 5231745cc5b9b08cedd762394593203cd13cbd25 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class FsResourceTypesTest {

    private val db = SimpleDbConnection()

    init {
        db.baseFolder = FileSystemResource("conf/h2/database")
        db.url = "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';"
        db.paginationQueryTemplate = "${'$'}{query} offset ${'$'}{uri.pageSize()*(uri.pageNumber()-1)} limit ${'$'}{uri.pageSize()}"
    }

    @Test
    fun read() {
        val types = FsResourceTypes(db, FakeUri())
        assertEquals(5, types.read(DbUri("table/bookings")).count())
        assertEquals(1, types.read(DbUri("table/bookings.airports")).count())
    }

    @Test
    @Deprecated("Deprecated")
    fun resourceTypes() {
        assertEquals(sequenceOf("locks", "query", "sessions", "column", "index", "pk", "table", "schema").sorted().toList(),
                FsResourceTypes(db, FakeUri()).resourceTypes().map { it.name() }.sorted())
    }

    @Test
    fun aliases() {
        assertThrows(BootException::class.java) { FsResourceTypes(db, FakeUri()).aliases() }
    }

    @Test
    fun path() {
        assertThrows(BootException::class.java) { FsResourceTypes(db, FakeUri()).path() }
    }

    @Test
    fun metaData() {
        assertThrows(BootException::class.java) { FsResourceTypes(db, FakeUri()).metaData() }
    }

}