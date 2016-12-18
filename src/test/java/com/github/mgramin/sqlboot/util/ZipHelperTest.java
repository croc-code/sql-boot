package com.github.mgramin.sqlboot.util;

import org.junit.Test;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by MGramin on 28.11.2016.
 */
public class ZipHelperTest {

    @Test
    public void create() throws Exception {
        Map<String, byte[]> files = new HashMap<>();
        files.put("test.txt", "new text file".getBytes());
        files.put("test_1.txt", "new new text file".getBytes());

        byte[] bytes = new ZipHelper().create(files);

        FileOutputStream stream = new FileOutputStream("test.zip");
        try {
            stream.write(bytes);
        } finally {
            stream.close();
        }
    }

}