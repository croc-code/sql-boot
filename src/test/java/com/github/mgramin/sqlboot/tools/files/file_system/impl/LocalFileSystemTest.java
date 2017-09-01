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

package com.github.mgramin.sqlboot.tools.files.file_system.impl;

import com.github.mgramin.sqlboot.tools.files.file_system.FileSystem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id$
 * @since 0.1
 */
public class LocalFileSystemTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before() {
    }

    @Test
    public void listFiles() throws Exception {
        java.io.File base_test_folder = folder.newFolder("base_test_folder");

        folder.newFile("base_test_folder/table.hr.persons.sql");
        folder.newFile("base_test_folder/table.hr.jobs.sql");
        folder.newFile("base_test_folder/table.hr.departments.sql");
        folder.newFile("base_test_folder/table.salary.payroll.sql");

        folder.newFile("base_test_folder/index.hr.persons.pk_persons_idx.sql");
        folder.newFile("base_test_folder/index.hr.jobs.pk_jobs_idx.sql");
        folder.newFile("base_test_folder/index.hr.departments.pk_dep_idx.sql");
        folder.newFile("base_test_folder/index.salary.payroll.pk_payroll_idx.sql");

        FileSystem fileSystem = new LocalFileSystem(base_test_folder.getAbsolutePath());
        assertEquals(3, fileSystem.listFiles("**/table.hr.*.sql").size());
        assertEquals(3, fileSystem.listFiles("**/table.hr.*s.sql").size());
        assertEquals(1, fileSystem.listFiles("**/table.hr.p*.sql").size());
        assertEquals(1, fileSystem.listFiles("**/table.salary.*.sql").size());
        assertEquals(1, fileSystem.listFiles("**/table.salary.*.sql").size());

        assertEquals(1, fileSystem.listFiles("**/index.hr.persons.*idx.sql").size());
        assertEquals(4, fileSystem.listFiles("**/index.*.sql").size());
        assertEquals(3, fileSystem.listFiles("**/index.hr.*.sql").size());
        assertEquals(1, fileSystem.listFiles("**/index.salary.*.sql").size());
    }

}

