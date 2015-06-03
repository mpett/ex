import java.io.IOException;

/**
 * Created by martinpettersson on 03/06/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        PairExtractor extractor = new PairExtractor("dobj");
        extractor.extract();
    }
}
