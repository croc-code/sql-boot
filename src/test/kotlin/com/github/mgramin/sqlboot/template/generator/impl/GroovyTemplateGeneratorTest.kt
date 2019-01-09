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

package com.github.mgramin.sqlboot.template.generator.impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by MGramin on 10.12.2016.
 */
class GroovyTemplateGeneratorTest {

    @Test
    fun process() {
        val txt = """... where lower(c.table_schema) like '${'$'}schema'
                    |      and lower(c.table_name) like '${'$'}table'
                    |      and lower(c.column_name) like '${'$'}column'""".trimMargin()

        val result = """... where lower(c.table_schema) like 'public'
                       |      and lower(c.table_name) like 'persons'
                       |      and lower(c.column_name) like 'id'""".trimMargin()

        val maps = hashMapOf("column" to "id", "table" to "persons", "schema" to "public")
        val templateGenerator = GroovyTemplateGenerator(txt)
        assertEquals(templateGenerator.generate(maps), result)
    }

    @Test
    fun processLoweCase() {
        val maps = hashMapOf("column" to "id", "table" to "persons", "schema" to "public")
        val templateGenerator = GroovyTemplateGenerator("create table \${table.toLowerCase()} ...")
        assertEquals(templateGenerator.generate(maps), "create table persons ...")
    }

}