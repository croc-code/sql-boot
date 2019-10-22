package com.github.mgramin.sqlboot.model.resource.wrappers

import com.github.mgramin.sqlboot.model.resource.impl.FakeDbResource
import com.github.mgramin.sqlboot.model.resourcetype.impl.FakeResourceType
import com.github.mgramin.sqlboot.model.uri.impl.FakeUri
import com.github.mgramin.sqlboot.model.uri.wrappers.JsonWrapper
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class DbResourceBodyWrapperTest {

    private val dbResource = DbResourceBodyWrapper(FakeDbResource(FakeUri()), "BODY")

    @Test
    fun name() = assertEquals("FAKE_TBL", dbResource.name())

    @Test
    fun type() = assertEquals(FakeResourceType().name(), dbResource.type().name())

    @Test
    fun dbUri() {
        assertEquals(JsonWrapper(FakeUri()).toString(), JsonWrapper(dbResource.dbUri()).toString())
    }

    @Test
    fun headers() = assertEquals(4, dbResource.headers().size)

    @Test
    fun body() = assertEquals("BODY", dbResource.body())

}