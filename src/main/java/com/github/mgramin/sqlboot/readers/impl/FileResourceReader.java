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

package com.github.mgramin.sqlboot.readers.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.github.mgramin.sqlboot.model.DbResource;
import com.github.mgramin.sqlboot.model.DbResourceBodyWrapper;
import com.github.mgramin.sqlboot.model.DbResourceThin;
import com.github.mgramin.sqlboot.model.ResourceType;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.readers.DbResourceReader;
import com.github.mgramin.sqlboot.tools.files.file.File;
import com.github.mgramin.sqlboot.tools.files.file_system.FileSystem;

import static java.util.stream.Collectors.joining;

/**
 * Created by mgramin on 31.10.2016.
 */
public final class FileResourceReader implements DbResourceReader {

    private final FileSystem fileSystem;

    public FileResourceReader(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public List<DbResource> read(final Uri uri, final ResourceType type) {
        final String mask =
            "**/" + uri.type() + "." + uri.objects().stream().map(v -> v.replace("%", "*"))
                .collect(joining(".")) + ".sql";
        List<DbResource> result = new ArrayList<>();
        for (File file : fileSystem.listFiles(mask)) {
            result.add(
                new DbResourceBodyWrapper(
                    new DbResourceThin(file.name(), type, null, null),
                    new String(file.content())));
        }
        return result;
    }

}
