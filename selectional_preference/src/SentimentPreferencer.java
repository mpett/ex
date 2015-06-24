import static org.grep4j.core.Grep4j.grep;
import static org.grep4j.core.Grep4j.constantExpression;
import static org.grep4j.core.fluent.Dictionary.on;
import org.grep4j.core.model.ProfileBuilder;
import org.grep4j.core.model.Profile;
import org.grep4j.core.result.GrepResults;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Various methods used for calculating sentiment-level selectional preference.
 *
 * Author: Martin Pettersson
 */
public class SentimentPreferencer {
    private static String relationshipType = "dobj";
    private static Kattio io = new Kattio(System.in);

    public static void main(String[] args) throws IOException {
        String[] input = io.getWord().split(",");
        String word = input[0]; relationshipType = input[1]; String position = input[2];
        performExtraction();
        double result = naiveSentimentPreference(grepPairs(word), position, word);
        System.out.println(result);
    }

    /**
     * Takes a grammatical relationship, word position and a word.
     * Returns an average sentiment value.
     *
     * @param sentimentPairs
     * @param position
     * @param word
     * @return
     */
    private static double naiveSentimentPreference(GrepResults sentimentPairs, String position, String word) {
        //processing the single grep result for each profile
        String[] grepResults = sentimentPairs.toString().split("\n"); int wordPosition = 0;
        if (position.equals("head")) wordPosition = 0; else if (position.equals("argument")) wordPosition = 1;
        else System.err.println("Illegal argument");
        String[] tmp; ArrayList<String> results = new ArrayList<String>();
        double average = 0; double sentiment = 0; int numPos = 0; int numNeg = 0;
        for (String result : grepResults) {
            tmp = result.split(" ");
            if (tmp[wordPosition].equals(word)) {
                results.add(result);
                sentiment = Double.parseDouble(tmp[5-wordPosition]);
                average += sentiment;
                if (sentiment >= 0)
                    numPos++;
                else numNeg++;
            }
        }
        for (String result : results)
            System.out.println(result);
        double averageSum = average;
        System.err.println("Hits: " + results.size() + " Average Sum " + averageSum + "num pos:" + numPos + " num neg: " + numNeg );
        return average/results.size();
    }

    /**
     * Used for searching in the dataset.
     *
     * @param word
     * @return
     */
    private static GrepResults grepPairs(String word) {
        Profile localProfile = ProfileBuilder.newBuilder()
                .name("Sentiment pair lexicon")
                .filePath("sentiment_pairs_" + relationshipType + ".txt")
                .onLocalhost()
                .build();
        //Obtaining the global result
        GrepResults results = grep(constantExpression(word), on(localProfile));
        return results;
    }

    /**
     * Extracts a dataset depending on what kind of relationship is passed in as argument.
     *
     * @throws IOException
     */
    private static void performExtraction() throws IOException {
        // Do not build resources if they already exist.
        if (new File("sentiment_pairs_" + relationshipType + ".txt").isFile())
            return;
        // Build resources from corpus.
        System.out.println("Building resources...");
        PairExtractor extractor = new PairExtractor(relationshipType);
        extractor.extract();
        extractor.addTakamuraSentimentValues(relationshipType);
        System.out.println("Resources built for relationship type: " + relationshipType);
    }
}