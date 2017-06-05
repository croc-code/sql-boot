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

import static org.junit.Assert.assertEquals;

import com.github.mgramin.sqlboot.model.DbUri;
import com.github.mgramin.sqlboot.model.FakeDbResourceType;
import com.github.mgramin.sqlboot.model.Uri;
import com.github.mgramin.sqlboot.tools.files.file_system.FileSystem;
import com.github.mgramin.sqlboot.tools.files.file_system.impl.LocalFileSystem;
import java.io.IOException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Created by maksim on 21.05.17.
 */
public class FileResourceReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    java.io.File base_test_folder;

    @Before
    public void before() throws IOException {
        base_test_folder = folder.newFolder("base_test_folder");

        folder.newFile("base_test_folder/table.hr.persons.sql");
        folder.newFile("base_test_folder/table.hr.jobs.sql");
        folder.newFile("base_test_folder/table.hr.departments.sql");
        folder.newFile("base_test_folder/table.salary.payroll.sql");

        folder.newFile("base_test_folder/index.hr.persons.pk_persons_idx.sql");
        folder.newFile("base_test_folder/index.hr.jobs.pk_jobs_idx.sql");
        folder.newFile("base_test_folder/index.hr.departments.pk_dep_idx.sql");
        folder.newFile("base_test_folder/index.salary.payroll.pk_payroll_idx.sql");
    }

    @Test
    public void read() throws Exception {
        FileSystem fileSystem = new LocalFileSystem(base_test_folder.getAbsolutePath());
        FileResourceReader fileResourceReader = new FileResourceReader(fileSystem);
        Uri uri = new DbUri("index/hr.persons.*idx");
        assertEquals(1, fileResourceReader.read(uri, new FakeDbResourceType()).size());
    }

}