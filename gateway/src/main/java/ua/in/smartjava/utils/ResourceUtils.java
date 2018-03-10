package ua.in.smartjava.utils;

import java.io.InputStream;
import java.util.Scanner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceUtils {

    private ResourceUtils() {
        throw new RuntimeException("Utility class.");
    }

    public static String loadDataFromFile(String fileName, String delimiter) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = loader.getResourceAsStream(fileName);
        String data = new Scanner(resourceAsStream).useDelimiter(delimiter).next();
        return data;
    }
}
