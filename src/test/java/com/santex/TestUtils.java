package com.santex;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class TestUtils {

    private static Logger logger = LoggerFactory.getLogger(TestUtils.class);

    public static String getJsonString(String pathFile, String fileName) {
        String path = getPath(pathFile, fileName);
        String jsonString = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            jsonString = IOUtils.toString(fileInputStream, "UTF-8");
        } catch (IOException e) {
            logger.error("TestUtils fail: ", e);
        }

        return jsonString;
    }

    public static String getPath(String pathFile, String fileName) {
        pathFile = pathFile == null ? "" : pathFile;
        return String.format("src/test/resources%s/%s", pathFile, fileName);
    }
}
