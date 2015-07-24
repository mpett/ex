import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by martinpettersson on 24/06/15.
 */
public class Main {
    private static Kattio io = new Kattio(System.in);

    public static void main(String[] args) throws IOException {
        test();
        //sentimentPreference();
        //evaluateModel();
        //resolvePronouns("devset.txt");
        io.close();
    }

    private static void test() throws IOException {
        PairExtractor p = new PairExtractor("dobj");
        p.addWarrinerValenceValues("dobj");
    }

    private static void resolvePronouns(String document) {
        PronounResolver p = new PronounResolver();
        p.getStanfordCoref(document);
    }

    private static void evaluateModel() throws IOException {
        ModelEvaluator me = new ModelEvaluator("wsc_sentiment_problems");
        me.parseFromInput();
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

    private static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
