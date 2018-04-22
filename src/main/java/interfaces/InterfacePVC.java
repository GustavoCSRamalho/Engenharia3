package interfaces;

import java.io.FileNotFoundException;

public interface InterfacePVC {

    abstract void makeParagraphVectors() throws Exception;

    abstract void checkUnlabeledData() throws FileNotFoundException;

    abstract String conteudo();

}
