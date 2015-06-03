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
                String[] stringPair = line.replaceAll("[^0-9-]", "").substring(1).split("-");
                int[] wordIndexPair = new int[2];

                try {
                    wordIndexPair[0] = Integer.parseInt(stringPair[0]); wordIndexPair[1] = Integer.parseInt(stringPair[1]);
                    System.err.println(wordIndexPair[0] + " " + wordIndexPair[1]);
                } catch (NumberFormatException e) {
                    continue;
                }


            }

        }
        br.close();
    }
}