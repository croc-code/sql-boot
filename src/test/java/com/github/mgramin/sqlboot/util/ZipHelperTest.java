package com.github.mgramin.sqlboot.util;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MGramin on 28.11.2016.
 */
public class ZipHelperTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void create() throws Exception {
        final File tempFile = temporaryFolder.newFile("ddl_result.zip");

        Map<String, byte[]> files = new HashMap<>();
        files.put("persons.sql", "create table persons ... ;".getBytes());
        files.put("jobs.sql", "create table jobs ... ;".getBytes());

        FileUtils.writeByteArrayToFile(tempFile, ZipHelper.compress(files));
    }

}