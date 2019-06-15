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

import com.github.mgramin.sqlboot.model.connection.FakeEndpoint
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
import java.math.BigDecimal

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

        override fun getWrapper() = DbNameWrapper(FakeResourceType(), FakeEndpoint())

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
                            Metadata("endpoint", "String", "Endpoint name")).sorted(),
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
                    .expectNextMatches { v -> v.headers().count() == 4 && v.headers()["size"] is BigDecimal }
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

        @Test
        fun toJson() {
            assertEquals("fake_resource_type", w.toJson().get("name").asString)
        }

    }

}