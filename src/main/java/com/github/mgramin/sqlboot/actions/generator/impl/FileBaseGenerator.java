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

package com.github.mgramin.sqlboot.actions.generator.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import com.github.mgramin.sqlboot.actions.generator.ActionGenerator;
import com.github.mgramin.sqlboot.exceptions.BootException;
import com.github.mgramin.sqlboot.model.IDbResourceCommand;
import org.apache.commons.io.FileUtils;

/**
 * Generate command from text-file.
 *
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public final class FileBaseGenerator implements ActionGenerator {

    /**
     * FileSystem.
     */
    private final File file;

    /**
     * Command.
     */
    private final IDbResourceCommand command;

    /**
     * Aggregators.
     */
    private final String aggregators;

    /**
     * FileSystem encoding.
     */
    private final Charset charset = StandardCharsets.UTF_8;

    /**
     * Ctor.
     *
     * @param file FileSystem
     * @param command Command
     * @param aggregators Aggregators
     */
    public FileBaseGenerator(final File file, final IDbResourceCommand command,
        final String aggregators) {
        this.file = file;
        this.command = command;
        this.aggregators = aggregators;
    }

    @Override
    public String generate(final Map<String, Object> variables) {
        return this.generate();
    }

    @Override
    public String generate(final List<Object> variables) {
        return this.generate();
    }

    @Override
    public IDbResourceCommand command() {
        return this.command;
    }

    @Override
    public String aggregators() {
        return this.aggregators;
    }

    /**
     * Common generate method.
     *
     * @return Command text
     */
    private String generate() {
        try {
            return FileUtils.readFileToString(this.file, this.charset);
        } catch (final IOException exception) {
            throw new BootException(exception);
        }
    }

}

