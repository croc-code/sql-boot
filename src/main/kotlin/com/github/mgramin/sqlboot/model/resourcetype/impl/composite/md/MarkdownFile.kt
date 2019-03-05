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

package com.github.mgramin.sqlboot.model.resourcetype.impl.composite.md

import com.github.mgramin.sqlboot.tools.files.file.File
import org.commonmark.node.AbstractVisitor
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.Heading
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import java.util.LinkedHashMap

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: d5d9fbccca9519bf74e3b6add53e46104ffa5931 $
 * @since 0.1
 */
class MarkdownFile(private val name: String, private val text: String) : File {

    override fun name(): String {
        return name
    }

    override fun content(): ByteArray {
        val visitor = CustomVisitor()
        Parser.builder().build().parse(text).accept(visitor)
        return visitor.getMap().iterator().next().value.toByteArray()
    }

    @Deprecated("")
    fun parse(): Map<String, String> {
        val visitor = CustomVisitor()
        Parser.builder().build().parse(text).accept(visitor)
        return visitor.getMap()
    }

    class CustomVisitor : AbstractVisitor() {

        private var currentTag: String = ""
        private val map = LinkedHashMap<String, String>()

        override fun visit(text: Text) {
            if (text.parent is Heading && (text.parent as Heading).level >= 3) {
                currentTag = text.literal
            }
        }

        override fun visit(fencedCodeBlock: FencedCodeBlock) {
            if (fencedCodeBlock.fenceLength == 4) {
                map[currentTag] = fencedCodeBlock.literal.trim()
            }
        }

        fun getMap(): Map<String, String> {
            return map
        }
    }

}
