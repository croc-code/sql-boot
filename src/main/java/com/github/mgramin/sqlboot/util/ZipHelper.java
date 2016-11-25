package com.github.mgramin.sqlboot.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by maksim on 06.07.16.
 */
public class ZipHelper {

    FileOutputStream fos;
    ByteArrayOutputStream baos;
    ZipOutputStream zos;

    public ZipHelper(File zipFile) {
        this.zipFile = zipFile;
        try {
            fos = new FileOutputStream (new File("test.zip"));
            baos = new ByteArrayOutputStream();
            zos = new ZipOutputStream(baos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addFile(File file, String text) {
        try {
            ZipEntry entry = new ZipEntry(String.valueOf(file));
            zos.putNextEntry(entry);
            zos.write(text.getBytes());
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void create() {
        try {
            baos.writeTo(fos);
            fos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    File zipFile;

}