package interfaces;

import java.io.FileNotFoundException;
import java.util.List;

import org.nd4j.linalg.primitives.Pair;

public interface InterfacePVC {

    abstract void makeParagraphVectors() throws Exception;

    abstract List<Pair<String, Double>> checkUnlabeledData() throws FileNotFoundException;

    abstract String conteudo();

}
