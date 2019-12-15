package com.github.mgramin.sqlboot.model.resourcetypelist

import com.github.mgramin.sqlboot.model.resourcetype.impl.FakeResourceType
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Test

internal class CacheWrapperTest {

    @Test
    fun types() {
        val mock = mock<ResourceTypeList> { on { types() } doReturn listOf(FakeResourceType()) }
        val cacheWrapper = CacheWrapper(mock)
        cacheWrapper.types()
        cacheWrapper.types()
        cacheWrapper.types()
        verify(mock, times(1)).types()
    }

}