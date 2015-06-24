import java.io.IOException;

/**
 * Created by martinpettersson on 24/06/15.
 */
public class Main {
    private static Kattio io = new Kattio(System.in);

    public static void main(String[] args) {
        sentimentPreference();
        io.close();
    }

    private static void sentimentPreference() {
        String[] input = io.getWord().split(",");
        String word = input[0]; String relationshipType = input[1]; String position = input[2];
        SentimentPreferencer sp = new SentimentPreferencer(relationshipType);
        try {
            double result = sp.naiveSentimentPreference(position, word);
            System.out.println(result);
        } catch (IOException e) {
            System.err.println("IO Exception");
        }
    }
}
