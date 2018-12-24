package com.github.mgramin.sqlboot.sql.select.wrappers

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test

internal class PageWrapperTest {

    @Test
    fun select() {
        val select = PageWrapper(mock { on { query } doReturn "select 1" },
                1, 5)
        println(select.query)
    }

    @Test
    fun columns() {
    }

    @Test
    fun dbHealth() {
    }

    @Test
    fun getQuery() {
    }

}