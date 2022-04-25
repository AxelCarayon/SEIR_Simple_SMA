package utils;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;

public class StatsRecorder {

    private static int nbOfCycles = 0;

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
            String str = "";
            for (int i = 0; i < keys.size(); i++) {
                str+=keys.get(i);
                if (i != keys.size()-1){
                    str+=",";
                }
            }
            bw.append(str+'\n');
        }

        String line = "";
        for (String title : keys) {
            line += data.get(title);
            if (title != keys.get(keys.size()-1)) {
                line+=",";
            }
        }
        bw.append(line+'\n');
        bw.close();
        nbOfCycles++;
    }

}
