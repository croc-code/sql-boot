package com.github.mgramin.sqlboot.model;

import com.github.mgramin.sqlboot.model.resource_type.ResourceType;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MGramin on 11.07.2017.
 */
public class ResourceTypesImpl implements ResourceTypes {

    // Files and directories statistics
    private static int allElementsCount = 0;
    private static int directoriesCount = 0;

    @Override
    public List<ResourceType> load() {

        // Scanned directory
        String directoryPath = "conf/h2/database";

        // Display directory name
        System.out.println(directoryPath);
        // Go recursively down
        listDirectory(directoryPath);


        final List<String> list = new ArrayList<>();

        try {
            Files.walkFileTree(Paths.get("conf/h2/database"), new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isDirectory()) {
                        return FileVisitResult.CONTINUE;
                    }
                    String id = file.getFileName().toString();
                    list.add(id);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(list.size());

        return null;
    }



    public void listDirectory(String directoryPath) {
        // List all the directory contents
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(directoryPath).getFile());

        for (String contents: file.list()) {

            allElementsCount++;

            // Directory path for files and directories
            contents = directoryPath+"/"+contents;

            // Display full path names
            System.out.println(contents);

            // For directories go recursively down
            if (new File(contents).isDirectory()) {

                directoriesCount++;

                // Go down to next directory
                listDirectory(contents);
            }
        }
    }


}
