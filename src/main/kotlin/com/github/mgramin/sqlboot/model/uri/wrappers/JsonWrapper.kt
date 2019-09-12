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

package com.github.mgramin.sqlboot.model.uri.wrappers

import com.github.mgramin.sqlboot.model.uri.Uri
import java.nio.charset.StandardCharsets

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: c225f8da4e169e4c72a64aeca1d201cbb46fffa0 $
 * @since 0.1
 */
class JsonWrapper(private val origin: Uri) : Uri {

    override fun type() = origin.type()

    override fun path() = origin.path()

    override fun path(index: Int) = origin.path(index)

    override fun params() = origin.params()

    override fun action() = origin.action()

    override fun connection() = origin.connection()

    override fun toString(): String {
        var s = "DbUri{" +
                "type='" + origin.type() + '\''.toString() +
                ", path=" + origin.path() +
                ", params=" + origin.params() +
                "}"
        s = java.net.URLDecoder.decode(s, StandardCharsets.UTF_8.name()).replace("%", "*")
        return s
    }
}
