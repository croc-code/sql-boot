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

package com.github.mgramin.sqlboot.sql.select.impl

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.parser.SelectStatementParser
import com.github.mgramin.sqlboot.template.generator.TemplateGenerator
import org.apache.log4j.Logger
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

/**
 * Execute SQL-query through plain old Jdbc.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: f38638fde3d38f83edd4b8a03c570f845c856752 $
 * @since 0.1
 */
class JdbcSelectQuery(
    private val dataSource: DataSource,
    private val nullAlias: String,
    private val templateGenerator: TemplateGenerator
) : SelectQuery {

    constructor(dataSource: DataSource, templateGenerator: TemplateGenerator)
            : this(dataSource, "[NULL]", templateGenerator)

    private val logger = Logger.getLogger(JdbcSelectQuery::class.java)

    override fun execute(variables: Map<String, Any>): Sequence<Map<String, Any>> {
        return getMapStream(templateGenerator.generate(variables))
    }

    private fun getMapStream(sqlText: String): Sequence<Map<String, Any>> {
        logger.info(sqlText)
        val rowSet = JdbcTemplate(dataSource).queryForRowSet(sqlText)
        val iterator = object : Iterator<Map<String, Any>> {
            override fun hasNext(): Boolean {
                return rowSet.next()
            }

            override fun next(): Map<String, Any> {
                return rowSet
                        .metaData
                        .columnNames
                        .map { it.toLowerCase() to (rowSet.getObject(it) ?: nullAlias) }
                        .toMap()
            }
        }
        return iterator.asSequence()
    }

    override fun columns(): Map<String, String> {
        return SelectStatementParser(templateGenerator!!.template())
                .parse()
                .columns()
                .map { it.name() to it.comment() }
                .toMap()
    }

}
