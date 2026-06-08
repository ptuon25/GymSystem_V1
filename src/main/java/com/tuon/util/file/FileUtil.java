package com.tuon.util.file;

import java.io.File;

public class FileUtil {

    public static void createDirectoryIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
