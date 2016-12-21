package com.github.mgramin.sqlboot.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;

/**
 * Created by MGramin on 28.11.2016.
 */
public class ZipHelperTest {

    @Test
    public void create() throws Exception {
        Map<String, byte[]> files = new HashMap<>();
        files.put("persons.sql", "create table persons ... ;".getBytes());
        files.put("jobs.sql", "create table jobs ... ;".getBytes());

        write(get("ddl_result.zip"), new ZipHelper().compress(files));
    }

}