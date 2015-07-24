import org.grep4j.core.model.Profile;
import org.grep4j.core.model.ProfileBuilder;
import org.grep4j.core.result.GrepResult;
import org.grep4j.core.result.GrepResults;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.grep4j.core.Grep4j.constantExpression;
import static org.grep4j.core.Grep4j.grep;
import static org.grep4j.core.fluent.Dictionary.on;

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
     * Takes as input a grammatical relation (see clueweb corpus for a list) and
     * outputs all word pair lemmas and their pos tags in a file named extracted_pairs_relation.txt.
     * @param relation
     * @throws IOException
     */
    private void handleInputData(String relation) throws IOException {
        String fileName = "clueweb12_parsed.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        PrintWriter writer = new PrintWriter("extracted_pairs/extracted_pairs_" + argumentType + ".txt", "UTF-8");
        while ((line = br.readLine()) != null) {
            // Saves parsed string in array and continues.
            if (line.contains("[Text=")) {
                parsedWords = line.split("] ");
                continue;
            }
            // Following lines takes the relation we're looking for and finds the corresponding lemma.
            if (line.contains(relation + "(")) {
                try {
                    line = line.substring(relation.length());
                    String[] stringPair = line.replaceAll("[^0-9-]", "").substring(1).split("-");
                    int[] wordIndexPair = new int[2];
                    wordIndexPair[0] = Integer.parseInt(stringPair[0]); wordIndexPair[1] = Integer.parseInt(stringPair[1]);
                    String headWord = parsedWords[wordIndexPair[0] - 1]; String argumentWord = parsedWords[wordIndexPair[1] - 1];
                    String[] splitHead = headWord.split(" "); headWord = splitHead[4].substring(6);
                    String[] splitArgument = argumentWord.split(" "); argumentWord = splitArgument[4].substring(6);
                    String headPartOfSpeech = splitHead[3].substring(13); String argumentPartOfSpeech = splitArgument[3].substring(13);
                    writer.println(headWord.toLowerCase() + " " + argumentWord.toLowerCase() +
                            " " + headPartOfSpeech.toLowerCase() + " " + argumentPartOfSpeech.toLowerCase());
                } catch (Exception e) { continue; }
            }
        }
        // Close buffered input stream.
        br.close();
    }

    /**
     * Reads file with extracted pairs according to relation, then attempts to write
     * sentiment values for each word by finding them in the pn_en.dic lexicon.
     * The resulting data is written to a file named sentiment_pairs_relation.txt
     *
     * @param relation
     * @throws IOException
     */
    public void addTakamuraSentimentValues(String relation) throws IOException {
        String verb = "vb vbd vbg vbn vbp vbz";
        String noun = "nn nns nnp nnps";
        String adjective = "jj jjr jjs";
        String adverb = "rb rbr rbs wrb";
        // Initialize files, readers and writers.
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Double> sentimentValues = new ArrayList<Double>();
        String lexiconName = "pn_en.dic.txt";
        Scanner lexiconScanner = new Scanner(new FileReader(lexiconName));
        BufferedReader pairReader = new BufferedReader(new FileReader("extracted_pairs/extracted_pairs_" + relation + ".txt"));
        PrintWriter writer = new PrintWriter("sentiment_pairs/sentiment_pairs_" + relation + ".txt", "UTF-8");
        // Handle input from the sentiment lexicon.
        while (lexiconScanner.hasNextLine()) {
            String lexiconEntry = lexiconScanner.nextLine();
            String[] splitEntry = lexiconEntry.split(":");
            String word = splitEntry[0] + ":" + splitEntry[1];
            Double sentimentValue = Double.parseDouble(splitEntry[2]);
            words.add(word); sentimentValues.add(sentimentValue);
        }
        String line;
        while ((line = pairReader.readLine()) != null) {
            String result = "";
            String[] splitEntry = line.split(" ");
            if (splitEntry.length != 4) continue;
            if (verb.contains(splitEntry[2])) {
                if (words.contains(splitEntry[0] + ":v")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[0] + ":v"));
                    result = line + " " + sentiment;
                }

            } else if (noun.contains(splitEntry[2])) {
                if (words.contains(splitEntry[0] + ":n")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[0] + ":n"));
                    result = line + " " + sentiment;
                }

            } else if (adjective.contains(splitEntry[2])) {
                if (words.contains(splitEntry[0] + ":a")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[0] + ":a"));
                    result = line + " " + sentiment;
                }

            } else if (adverb.contains(splitEntry[2])) {
                if (words.contains(splitEntry[0] + ":r")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[0] + ":r"));
                    result = line + " " + sentiment;
                }
            }
            if (verb.contains(splitEntry[3])) {
                if (words.contains(splitEntry[1] + ":v")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[1] + ":v"));
                    result = result + " " + sentiment;
                }

            } else if (noun.contains(splitEntry[3])) {
                if (words.contains(splitEntry[1] + ":n")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[1] + ":n"));
                    result = result + " " + sentiment;
                }

            } else if (adjective.contains(splitEntry[3])) {
                if (words.contains(splitEntry[1] + ":a")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[1] + ":a"));
                    result = result + " " + sentiment;
                }

            } else if (adverb.contains(splitEntry[3])) {
                if (words.contains(splitEntry[1] + ":r")) {
                    double sentiment = sentimentValues.get(words.indexOf(splitEntry[1] + ":r"));
                    result = result + " " + sentiment;
                }
            }
            // Throw the result away if it contains less than six words. Looks ugly but works?
            if (result.split(" ").length == 6)
                writer.println(result);
        }
    }

    public void addWarrinerValenceValues(String dependency) throws IOException {
        BufferedReader pairReader = new BufferedReader(new FileReader("extracted_pairs/extracted_pairs_" + dependency + ".txt"));
        PrintWriter writer = new PrintWriter("sentiment_pairs_warriner_valence/sentiment_pairs_warriner_valence_" + dependency + ".txt", "UTF-8");
        String line;
        System.err.println("running");
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<Double> sentimentValues = new ArrayList<Double>();
        String lexiconName = "sentiment_lexicons/Ratings_Warriner_et_al.csv";
        double depValue = 0; double govValue = 0;
        Scanner lexiconScanner = new Scanner(new FileReader(lexiconName));
        while (lexiconScanner.hasNextLine()) {
            String lexiconEntry = lexiconScanner.nextLine();
            String[] splitEntry = lexiconEntry.split(",");
            String word = splitEntry[1];
            Double sentimentValue = Double.parseDouble(splitEntry[2]);
            words.add(word); sentimentValues.add(sentimentValue);
        }
        while ((line = pairReader.readLine()) != null) {
            String result = line;
            String[] splitEntry = line.split(" ");

            if (words.contains(splitEntry[0]) && words.contains(splitEntry[1])) {
                depValue = sentimentValues.get(words.indexOf(splitEntry[0]));
                govValue = sentimentValues.get(words.indexOf(splitEntry[1]));
            } else continue;
            System.err.println(line + " " + depValue + " " + govValue);

        }

    }

    private GrepResults grepPairs(String word) {
        Profile localProfile = ProfileBuilder.newBuilder()
                .name("SentiWordNet")
                .filePath("sentiment_lexicons/Ratings_Warriner_et_al.csv")
                .onLocalhost()
                .build();
        //Obtaining the global result
        GrepResults results = grep(constantExpression(word), on(localProfile));
        return results;
    }


}