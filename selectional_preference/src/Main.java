import java.io.IOException;

/**
 * Created by martinpettersson on 03/06/15.
 */
public class Main {
    private static final String RELATIONSHIP_TYPE = "aux";

    public static void main(String[] args) throws IOException {
       performExtraction();
    }

    private static void performExtraction() throws IOException {
        PairExtractor extractor = new PairExtractor(RELATIONSHIP_TYPE);
        extractor.extract();
    }
}
