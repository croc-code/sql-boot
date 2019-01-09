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

package com.github.mgramin.sqlboot.tools.files.file.wrappers

import com.github.mgramin.sqlboot.exceptions.BootException
import com.github.mgramin.sqlboot.tools.files.file.File
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: e75136852b77a880369962b347c9d130e52eac4b $
 * @since 0.1
 */
class ZippedFile(private val name: String, private val origins: List<File>) : File {
    private val content: ByteArray

    init {
        this.content = unzip()
    }

    override fun name(): String {
        return this.name
    }

    override fun content(): ByteArray {
        return content
    }

    @Throws(BootException::class)
    private fun unzip(): ByteArray {
        try {
            ByteArrayOutputStream().use { bytes ->
                ZipOutputStream(bytes).use { zip ->
                    for (ent in this.origins) {
                        zip.putNextEntry(ZipEntry(ent.name()))
                        zip.write(ent.content())
                        zip.closeEntry()
                    }
                    zip.close()
                    bytes.close()
                    return bytes.toByteArray()
                }
            }
        } catch (exception: IOException) {
            throw BootException(exception)
        }

    }

}

