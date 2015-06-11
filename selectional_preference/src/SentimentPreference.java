import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A bunch of methods used for calculating selectional preference.
 */
public class SentimentPreference {
    private String fileName = "extracted_pairs.txt";

    /**
     * Constructor.
     */
    public SentimentPreference() {}

    /**
     * A very naive method for calculating Selectional Preference based on the extracted word pairs.
     * @param headWord
     * @param argument
     * @return
     * @throws IOException
     */
    public int naiveSelectionalPreference(String headWord, String argument) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName)); String line;
        int score = 0;
        while ((line = br.readLine()) != null) {
            if (line.equals(headWord + " " + argument))
                score++;
        }
        br.close();
        return score;
    }
}
