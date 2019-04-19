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

package com.github.mgramin.sqlboot.model.resourcetype.impl

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.model.connection.SimpleDbConnection
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import reactor.test.StepVerifier

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: 5231745cc5b9b08cedd762394593203cd13cbd25 $
 * @since 0.1
 */
@ExtendWith(SpringExtension::class)
@ContextConfiguration(locations = ["/test_config.xml"])
class FsResourceTypeTest {

    private val db = SimpleDbConnection()

    init {
        db.name = "unit_test_db"
        db.baseFolder = FileSystemResource("conf/h2/database")
        db.url = "jdbc:h2:mem:;INIT=RUNSCRIPT FROM 'classpath:schema.sql';"
        db.paginationQueryTemplate = "${'$'}{query} offset ${'$'}{uri.pageSize()*(uri.pageNumber()-1)} limit ${'$'}{uri.pageSize()}"
    }

    @ParameterizedTest
    @CsvSource(
            "prod/sessions,1",
            "prod/schema,4",
            "prod/s*,5",
            "prod/tab*/bookings,5",
            "prod/table/bookings,5",
            "prod/table/bookings.airports,1")
    fun read(uri: String, count: Long) {
        StepVerifier
                .create(FsResourceType(listOf(db), FakeUri()).read(DbUri(uri)))
                .expectNextCount(count)
                .verifyComplete()
    }

    @Test
    @Deprecated("Deprecated")
    fun resourceTypes() {
        assertEquals(sequenceOf("locks", "query", "sessions", "column", "index", "pk", "table", "schema").sorted().toList(),
                FsResourceType(listOf(db), FakeUri()).resourceTypes().map { it.name() }.sorted())
    }

    @Test
    fun aliases() {
        assertThrows(BootException::class.java) { FsResourceType(listOf(db), FakeUri()).aliases() }
    }

    @Test
    fun path() {
        assertThrows(BootException::class.java) { FsResourceType(listOf(db), FakeUri()).path() }
    }

    @ParameterizedTest
    @CsvSource(
            "prod/sessions,7",
            "prod/schema,9",
//            "prod/s*,5",
//            "prod/tab*/bookings,5",
            "prod/table/bookings,6",
            "prod/table/bookings.airports,6")
    fun metaData(uri: String, count: Int) {
        val metaData: List<Metadata> = FsResourceType(listOf(db), DbUri(uri)).metaData(DbUri(uri))
        assertEquals(count, metaData.count())
    }

}