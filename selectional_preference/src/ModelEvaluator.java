import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;

import java.util.Collection;

/**
 * Created by martinpettersson on 24/06/15.
 */
public class ModelEvaluator {
    public static void main(String[] args) {
        LexicalizedParser lp = LexicalizedParser.loadModel(
                "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
                "-maxLength", "80", "-retainTmpSubcategories");
        TreebankLanguagePack tlp = new PennTreebankLanguagePack();
        // Uncomment the following line to obtain original Stanford Dependencies
        tlp.setGenerateOriginalDependencies(true);

        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        String sentence = "This is an easy sentence";

        String[] sent = sentence.split(" ");
        Tree parse = lp.apply(Sentence.toWordList(sent)); GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        Collection<TypedDependency> tdl = gs.typedDependenciesCCprocessed(); System.out.println(tdl);
    }
}
