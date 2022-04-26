package utils;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StatsRecorder {

    private static int nbOfCycles = 0;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeToCSV(HashMap<String,Integer> data, String outputFile) throws IOException {

        if (!outputFile.endsWith(".csv")) {
            throw new InvalidParameterException("outputFile is not a .csv file.");
        }
        File file = new File(outputFile);
        file.createNewFile();

        if (nbOfCycles == 0) {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }

        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);

        List<String> keys = data.keySet().stream().toList();
        if (nbOfCycles == 0) {
            bw.flush();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                str.append(keys.get(i));
                if (i != keys.size()-1){
                    str.append(",");
                }
            }
            bw.append(str.toString()).append(String.valueOf('\n'));
        }

        StringBuilder line = new StringBuilder();
        for (String title : keys) {
            line.append(data.get(title));
            if (!Objects.equals(title, keys.get(keys.size() - 1))) {
                line.append(",");
            }
        }
        bw.append(line.toString()).append(String.valueOf('\n'));
        bw.close();
        nbOfCycles++;
    }

}
