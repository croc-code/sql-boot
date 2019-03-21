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

package com.github.mgramin.sqlboot.model.resourcetype.impl

import com.github.mgramin.sqlboot.model.resource.DbResource
import com.github.mgramin.sqlboot.model.resource.impl.FakeDbResource
import com.github.mgramin.sqlboot.model.resourcetype.ResourceType
import com.github.mgramin.sqlboot.model.uri.Uri
import com.github.mgramin.sqlboot.model.uri.impl.DbUri
import reactor.core.publisher.Flux
import reactor.core.publisher.toFlux
import java.util.Arrays.asList

/**
 * Created by maksim on 22.05.17.
 */
class FakeDbResourceType : ResourceType {

    override fun aliases(): List<String> {
        return asList("fake_resource_type", "fake_type", "frt", "f")
    }

    override fun path(): List<String> {
        return arrayListOf("schema", "table", "index")
    }

    override fun read(uri: Uri): Flux<DbResource> {
        return sequenceOf(
                FakeDbResource(DbUri("prod/table/hr.persons")),
                FakeDbResource(DbUri("prod/table/hr.users")),
                FakeDbResource(DbUri("prod/table/hr.jobs"))).toFlux()
    }

    override fun metaData(): Map<String, String> {
        return hashMapOf("@schema" to "Schema name", "@table" to "Table name", "@index" to "Index name")
    }
}
