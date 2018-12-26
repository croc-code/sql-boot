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

package com.github.mgramin.sqlboot.tools.files.file_system.impl

import org.junit.Assert.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File

/**
 * @author Maksim Gramin (mgramin@gmail.com)
 * @version $Id: b484c589855e2338f8682969e186711e49968a89 $
 * @since 0.1
 */
class LocalFileSystemTest {

    @ParameterizedTest
    @CsvSource( "3,**/table.hr.*.sql",
            "1,**/table.hr.p*.sql",
            "1,**/table.salary.*.sql",
            "1,**/index.hr.persons.*idx.sql",
            "4,**/index.*.sql",
            "3,**/index.hr.*.sql",
            "1,**/index.salary.*.sql")
    fun listFiles(size: Int, path: String) {
        val file = File("base_test_folder")
        file.mkdirs()

        File("base_test_folder/table.hr.persons.sql").createNewFile()
        File("base_test_folder/table.hr.jobs.sql").createNewFile()
        File("base_test_folder/table.hr.departments.sql").createNewFile()
        File("base_test_folder/table.salary.payroll.sql").createNewFile()
        File("base_test_folder/index.hr.persons.pk_persons_idx.sql").createNewFile()
        File("base_test_folder/index.hr.jobs.pk_jobs_idx.sql").createNewFile()
        File("base_test_folder/index.hr.departments.pk_dep_idx.sql").createNewFile()
        File("base_test_folder/index.salary.payroll.pk_payroll_idx.sql").createNewFile()

        assertEquals(size, LocalFileSystem(file.absolutePath).listFiles(path).size)

        file.delete()
    }

}

