import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.*;
import java.util.Collection;

/**
 * Created by martinpettersson on 24/06/15.
 */
public class ModelEvaluator {
    private String fileName;
    public ModelEvaluator(String fileName) {
        System.out.println(collapsedParse("This is an easy sentence."));
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
        Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed(); System.out.println(tdl);
        return tdl;
    }
}
