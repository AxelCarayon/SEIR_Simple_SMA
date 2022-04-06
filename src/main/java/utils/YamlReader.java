package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import models.Parameters;

import java.io.File;
import java.io.IOException;

public class YamlReader {

    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static ObjectMapper om = new ObjectMapper(new YAMLFactory());

    public static Parameters readFile(String fileName) throws IOException {
        File file = new File(classLoader.getResource(fileName).getFile());
        return om.readValue(file,Parameters.class);
    }
}
