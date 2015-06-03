import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Extracts word pairs from the ClueWeb12 corpus for selectional preference.
 *
 * Author: Martin Pettersson
 */
public class PairExtractor {
    private String[] parsedWords; private String argumentType;

    /**
     * Pair Extractor Constructor
     * @param argumentType
     * @throws IOException
     */
    public PairExtractor(String argumentType) {
        this.argumentType = argumentType;
    }

    /**
     * Perform the extraction.
     */
    public void extract() {
        try {
            handleInputData(argumentType);
        } catch (IOException e){
            System.err.println("I/O Exception caught");
        }

    }

    /**
     * Takes as input a grammatical argument (see clueweb corpus for a list) and outputs all word pair lemmas in a file.
     * @param argument
     * @throws IOException
     */
    private void handleInputData(String argument) throws IOException {
        String fileName = "clueweb12_parsed.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        PrintWriter writer = new PrintWriter("extracted_pairs.txt", "UTF-8");
        while ((line = br.readLine()) != null) {
            // Saves parsed string in array and continues.
            if (line.contains("[Text=")) {
                parsedWords = line.split("] ");
                continue;
            }
            // Following lines takes the argument we're looking for and finds the corresponding lemma.
            if (line.contains(argument)) {
                line = line.substring(argument.length());
                String[] stringPair = line.replaceAll("[^0-9-]", "").substring(1).split("-");
                int[] wordIndexPair = new int[2];
                try {
                    wordIndexPair[0] = Integer.parseInt(stringPair[0]); wordIndexPair[1] = Integer.parseInt(stringPair[1]);
                    try {
                        String headWord = parsedWords[wordIndexPair[0] - 1]; String argumentWord = parsedWords[wordIndexPair[1] - 1];
                        String[] splitHead = headWord.split(" "); headWord = splitHead[4].substring(6);
                        String[] splitArgument = argumentWord.split(" "); argumentWord = splitArgument[4].substring(6);
                        writer.println(headWord.toLowerCase() + " " + argumentWord.toLowerCase());
                    } catch (Exception e) {}
                } catch (NumberFormatException e) { continue; }
            }
        }
        // Close buffered input stream.
        br.close();
    }
}