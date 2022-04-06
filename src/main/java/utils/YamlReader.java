package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import models.Parameters;

import java.io.File;

public class YamlReader {

    private static ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private static ObjectMapper om = new ObjectMapper(new YAMLFactory());
    private static Parameters parameters;

    public static final String FILE_PATH = "parameters.yaml";

    public static Parameters getParams() {
        if (parameters == null) {
            System.out.println("Getting parameters in parameters.yaml file ...");
            File file = new File(classLoader.getResource(FILE_PATH).getFile());
            try {
                parameters = om.readValue(file,Parameters.class);
            } catch (Exception e) {
                System.out.println("file " + FILE_PATH + "not found");
                System.exit(1);
            }
        }
        return parameters;
    }
}
