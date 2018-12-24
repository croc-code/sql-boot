package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.sql.select.SelectQuery
import java.util.stream.Stream

class PageWrapper
/**
 * Ctor.
 *
 * @param origin
 * @param pageNumber
 * @param pageSize
 */
(private val origin: SelectQuery,
 private val pageNumber: Int,
 private val pageSize: Int
) : SelectQuery {

    val sql = """select *
                |  from (${origin.query})""".trimMargin()

    @Throws(BootException::class)
    override fun select(): Stream<Map<String, Any>>? {
        val sqlQuery = origin.query
        println(sql)
        return null
    }

    override fun columns(): Map<String, String>? {
        return origin.columns()
    }

    @Throws(BootException::class)
    override fun dbHealth() {
        origin.dbHealth()
    }

    override fun getQuery(): String {
        return sql
    }

}
