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

package com.github.mgramin.sqlboot.sql.select.wrappers

import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import com.github.mgramin.sqlboot.sql.select.impl.FakeSelectQuery
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PaginatedSelectQueryTest {

    @ParameterizedTest
    @CsvSource(
            "offset 0 limit 1,1,1",
            "offset 0 limit 2,1,2",
            "offset 0 limit 3,1,3",
            "offset 1 limit 1,2,1",
            "offset 2 limit 2,2,2",
            "offset 2 limit 1,3,1",
            "offset 3 limit 1,4,1",
            "offset 0 limit 100,1,100")
    fun query(expectedQuery: String, pageNumber: Int, pageSize: Int) {
        val paginatedSelectQuery =
                PaginatedSelectQuery(
                        origin = FakeSelectQuery(),
                        uri = DbUri("table/hr.persons?page=$pageNumber,$pageSize"),
                        template = "${"$"}{query} offset ${"$"}offset limit ${"$"}limit")
        println(paginatedSelectQuery.query())
        assertTrue(paginatedSelectQuery.query().contains(expectedQuery))
    }

    @Test
    fun columns() {
    }

    @Test
    fun execute() {

    }

}