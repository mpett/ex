import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by martinpettersson on 02/06/15.
 */
public class PairExtractor {
    public static void main(String[] args) throws IOException{
        new PairExtractor("dobj");
    }

    public PairExtractor(String argumentType) throws IOException{
        handleInputData(argumentType);
    }

    private void handleInputData(String argument) throws IOException {
        String fileName = "clueweb12_parsed.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("[Text=")) {
                //System.err.println(line);
                continue;
            }
            if (line.contains(argument)) {
                line = line.substring(argument.length());
                line = line.toLowerCase();
                line = line.replaceAll("[^A-Za-z0-9]", "").replaceAll("^[0-9]", " ").replaceAll("  ", " ");
                System.err.println(line);
                System.err.println("lol?");
            }

        }
        br.close();
    }
}