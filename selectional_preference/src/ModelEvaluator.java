import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * Created by martinpettersson on 24/06/15.
 */
public class ModelEvaluator {
    private String inputFile;
    private String acceptedDependencies = "dobj nsubj iobj";
    public ModelEvaluator(String inputFile) {
        this.inputFile = inputFile;
    }

    public void parseFromInput() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        String line;
        PrintWriter writer = new PrintWriter("wsc_sentiment_problems_parsed.txt", "UTF-8");
        while ((line = br.readLine()) != null) {
            writer.println(line);
            Collection<TypedDependency> tdl = collapsedParse(line);
            for (TypedDependency td : tdl) {
                if (acceptedDependencies.contains(td.reln().toString()) || td.reln().toString().contains("prep")) {
                    SentimentPreferencer sp = new SentimentPreferencer(td.reln().toString());
                    String governor = wordLemma(td.gov().word()).toLowerCase();
                    double govSentiment = sp.naiveSentimentPreference("head", governor);
                    String dependant = wordLemma(td.dep().word()).toLowerCase();
                    double depSentiment = sp.naiveSentimentPreference("argument", dependant);
                    writer.println(td.reln().toString() + " " + governor + " " + dependant + " " + govSentiment + " " + depSentiment);
                }
            }
            writer.println("\n");
        }
        writer.flush(); writer.close();
        System.err.println("File successfully parsed.");
    }

    private Collection<TypedDependency> collapsedParse(String sentence) {
        LexicalizedParser lp = LexicalizedParser.loadModel(
                "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                "-maxLength", "80", "-retainTmpSubcategories");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        tlp.setGenerateOriginalDependencies(true);
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        String[] sent = sentence.split(" ");
        Tree parse = lp.apply(Sentence.toWordList(sent)); GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
        return tdl;
    }

    private String wordLemma(String word) {
        StanfordLemmatizer lemmatizer = new StanfordLemmatizer();
        if (word == null)
            return "null";
        return lemmatizer.lemmatize(word).get(0);
    }
}
