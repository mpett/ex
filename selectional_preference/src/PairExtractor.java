import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by martinpettersson on 02/06/15.
 */
public class PairExtractor {
    private String[] parsedWords;
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
                parsedWords = line.split("] ");
                continue;
            }
            if (line.contains(argument)) {
                System.err.println(line);
                line = line.substring(argument.length());
                String[] stringPair = line.replaceAll("[^0-9-]", "").substring(1).split("-");
                int[] wordIndexPair = new int[2];

                try {
                    wordIndexPair[0] = Integer.parseInt(stringPair[0]); wordIndexPair[1] = Integer.parseInt(stringPair[1]);
                    try {
                        String headWord = parsedWords[wordIndexPair[0] - 1]; String argumentWord = parsedWords[wordIndexPair[1] - 1];
                        String[] splitHead = headWord.split(" "); headWord = splitHead[4].substring(6);
                        String[] splitArgument = argumentWord.split(" "); argumentWord = splitArgument[4].substring(6);
                        System.err.println("RESULT: " + headWord + " " + argumentWord);
                    } catch (Exception e) {}
                } catch (NumberFormatException e) { continue; }
            }
        }
        // Close buffered input stream.
        br.close();
    }
}