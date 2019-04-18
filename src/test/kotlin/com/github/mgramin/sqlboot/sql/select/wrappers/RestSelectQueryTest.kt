package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $
 * @since 0.1
 */
internal class RestSelectQueryTest {

    @Test
    fun query() {
    }

    @Test
    fun columns() {
    }

    @Test
    fun execute() {
        val mockQuery = mock<SelectQuery> {
            on { query() } doReturn "select * from processes limit 10"
        }
        val rows = RestSelectQuery(mockQuery, "http://5.8.181.165:8082")
                .execute(hashMapOf())
                .collectList()
                .block()
        assertEquals(10, rows.size)
    }

}