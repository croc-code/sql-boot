/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Maksim Gramin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.mgramin.sqlboot.model.resource_type.impl.composite.md;

import org.commonmark.node.*;

public class CustomVisitor extends AbstractVisitor {

    String currentTag;

    @Override
    public void visit(Text text) {
        if (((Heading)text.getParent()).getLevel()>=3) {
            currentTag = text.getLiteral();
        }
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        System.out.println(currentTag);
        System.out.println(fencedCodeBlock.getLiteral());
        System.out.println();
        System.out.println();
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        /*if (!htmlBlock.getLiteral().startsWith("</")) {
            System.out.println("*********");
            Node next = htmlBlock;
            while (!((next  = next.getNext()) instanceof HtmlBlock
                    && ((HtmlBlock)next).getLiteral().startsWith("</"))) {
                if (next instanceof FencedCodeBlock) {
                    FencedCodeBlock fencedCodeBlock = (FencedCodeBlock) next;
                    System.out.println(fencedCodeBlock.getFenceLength());
                    System.out.println(fencedCodeBlock.getLiteral());
                }
            }
            System.out.println("*********");
        }*/
        visitChildren(htmlBlock);
    }

}
