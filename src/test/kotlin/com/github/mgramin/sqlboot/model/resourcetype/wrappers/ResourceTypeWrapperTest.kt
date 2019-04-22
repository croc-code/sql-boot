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

package com.github.mgramin.sqlboot.model.resourcetype.wrappers

import com.github.mgramin.sqlboot.model.connection.FakeDbConnection
import com.github.mgramin.sqlboot.model.resourcetype.Metadata
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.resourcetype.impl.FakeResourceType
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.body.BodyWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.DbNameWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.SelectWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.header.TypeWrapper
import com.github.mgramin.sqlboot.model.resourcetype.wrappers.list.*
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.github.mgramin.sqlboot.template.generator.impl.FakeTemplateGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import reactor.test.StepVerifier

class ResourceTypeWrapperTest {

    @Nested
    internal inner class BodyWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = BodyWrapper(FakeResourceType(), FakeTemplateGenerator("Hello World!"))

        @Test
        fun read() {
            assertEquals(arrayListOf("Hello World!", "Hello World!", "Hello World!"),
                    w.read(FakeUri()).map { it.body() }.collectList().block())
        }
    }

    @Nested
    internal inner class SelectWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = SelectWrapper(FakeResourceType())

        @Test
        fun read() {
            val resources = w.read(DbUri("table/hr.persons?select=schema")).collectList().block().iterator().asSequence().toList()
            resources.forEach { resource -> assertEquals(1, resource.headers().size) }
            assertEquals(3, resources.size)
        }
    }

    @Nested
    internal inner class PageWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = PageWrapper(FakeResourceType())

        @ParameterizedTest
        @CsvSource("persons;prod/table/hr?page=1,1",
                "users;prod/table/hr?page=2,1",
                "jobs;prod/table/hr?page=3,1", delimiter = ';')
        fun read(name: String, uri: String) {
            assertEquals(name, w.read(DbUri(uri)).blockFirst().name())
        }

        @ParameterizedTest
        @CsvSource("1;prod/table/hr?page=1,1",
                "2;prod/table/hr?page=1,2",
                "3;prod/table/hr?page=1,3",
                "3;prod/table/hr?page=1",
                "0;prod/table/hr?page=2", delimiter = ';')
        fun read2(count: Long, uri: String) {
            assertEquals(count, w.read(DbUri(uri)).count().block())
        }
    }


    @Nested
    internal inner class WhereWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = WhereWrapper(FakeResourceType())

        @ParameterizedTest
        @CsvSource("1;prod/table/hr.persons",
                "3;prod/table/hr.s", delimiter = ';')
        fun read(count: Long, uri: String) {
            assertEquals(count, w.read(DbUri(uri)).count().block())
        }
    }


    @Nested
    internal inner class LimitWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = LimitWrapper(FakeResourceType())

        @ParameterizedTest
        @CsvSource("3;table/hr.persons",
                "3;table/hr.persons?limit=3",
                "2;table/hr.persons?limit=2",
                "1;table/hr.persons?limit=1", delimiter = ';')
        fun read(count: Long, uri: String) {
            assertEquals(count, w.read(DbUri(uri)).count().block())
        }
    }


    @Nested
    internal inner class CacheWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper(): ResourceType {
            return CacheWrapper(FakeResourceType())
        }

        @Test
        fun read() {
            assertAll("Should return same objects",
                    { assertEquals(3, w.read(FakeUri()).count().block()) },
                    { assertEquals(3, w.read(FakeUri()).count().block()) },
                    { assertEquals(3, w.read(FakeUri()).count().block()) }
            )
//            verify(originType, times(1)).read(any())
        }

    }


    @Nested
    internal inner class DbNameWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = DbNameWrapper(FakeResourceType(), FakeDbConnection())

        @Test
        fun read() {
//            w.read(FakeUri()).forEach { println(it.headers()) }
        }

        @Test
        override fun metaData() {
            assertEquals(
                    arrayListOf(
                            Metadata("@schema", "String", "Schema name"),
                            Metadata("@table", "String", "Table name"),
                            Metadata("@index", "String", "Index name"),
                            Metadata("size", "Number", "Table size"),
                            Metadata("database", "String", "Database name")).sorted(),
                    w.metaData(FakeUri()).sorted())
        }
    }


    @Nested
    internal inner class TypeWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = TypeWrapper(FakeResourceType())

        @Test
        fun read() {
            StepVerifier
                    .create(w.read(FakeUri()))
                    .expectNextMatches { v -> v.headers().count() == 4 && v.headers()["size"] is Int }
                    .expectNextCount(2)
                    .verifyComplete()
        }

    }


    @Nested
    internal inner class SortWrapperTest : BaseResourceWrapperTest() {

        override fun getWrapper() = SortWrapper(FakeResourceType())

        @Test
        fun read() {
            w.read(FakeUri())
                    .doOnNext { println(it.headers()) }
                    .subscribe()
        }

    }


    internal abstract inner class BaseResourceWrapperTest {

        val w = getWrapper()

        protected abstract fun getWrapper(): ResourceType

        @Test
        fun name() = assertEquals("fake_resource_type", w.name())

        @Test
        fun aliases() = assertEquals("[fake_resource_type, fake_type, frt, f]", w.aliases().toString())

        @Test
        fun path() = assertEquals(arrayListOf("schema", "table", "index"), w.path())

        @Test
        open fun metaData() {
            assertEquals(
                    arrayListOf(
                            Metadata("@schema", "String", "Schema name"),
                            Metadata("@table", "String", "Table name"),
                            Metadata("@index", "String", "Index name"),
                            Metadata("size", "Number", "Table size")
                    ).sorted(),
                    w.metaData(FakeUri()).sorted())
        }

    }

}