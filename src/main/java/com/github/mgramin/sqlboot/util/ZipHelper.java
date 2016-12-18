package com.github.mgramin.sqlboot.util;

import java.io.*;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by maksim on 06.07.16.
 */
public class ZipHelper {

    public byte[] create(Map<String, byte[]> files) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (Map.Entry<String, byte[]> stringEntry : files.entrySet()) {
                ZipEntry entry = new ZipEntry(stringEntry.getKey());
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(stringEntry.getValue());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            byteArrayOutputStream.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}