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

package com.github.mgramin.sqlboot.sql.select.impl

import com.github.mgramin.sqlboot.sql.select.SelectQuery
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTBaseVisitor
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTLexer
import com.github.mgramin.sqlboot.sql.select.impl.parser.SELECTParser
import com.github.mgramin.sqlboot.template.generator.TemplateGenerator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

class SimpleSelectQuery(private val templateGenerator: TemplateGenerator) : SelectQuery {

    override fun query() = templateGenerator.template()

    override fun properties(): Map<String, String> {
        val comment: String = SelectStatementParser(templateGenerator.template()).comment().replace("/*", "").replace("*/", "")
        return try {
            Gson().fromJson(comment, object : TypeToken<Map<String, String>>() {}.type)
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override fun columns() =
            SelectStatementParser(templateGenerator.template())
                    .parse()
                    .map { it.name() to it.comment() }
                    .toMap()

    override fun execute(variables: Map<String, Any>) = throw RuntimeException("Not allow here")


    private class SelectStatementParser(sql: String) {

        val parser = SELECTParser(CommonTokenStream(SELECTLexer(ANTLRInputStream(sql))))
        val selectVisitorCustom = SelectVisitorCustom()
        val visit = selectVisitorCustom.visit(parser.select_statement())

        fun comment() = selectVisitorCustom.queryComment

        fun parse(): List<SelectQuery.Column> {
            return visit as List<SelectQuery.Column>
        }
    }


    private class SelectVisitorCustom : SELECTBaseVisitor<Any>() {

        var queryComment: String = ""

        override fun visitSelect_statement(ctx: SELECTParser.Select_statementContext): Any {
            queryComment = ctx.query_comment().text
            return ctx.select_row()
                    .map { v ->
                        SelectQuery.Column(
                                v.column_alias()?.ID()?.text
                                        ?: (v.column_name()?.ID()?.text ?: "NULL"),
                                v.column_comment()?.let { v1 ->
                                    v1.MULTIPLE_LINE_COMMENT().text.replace("/*", "").replace("*/", "").trim { it <= ' ' }
                                } ?: (""), hashMapOf())
                    }
                    .toList()
        }
    }

}