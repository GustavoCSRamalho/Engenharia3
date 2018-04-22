package interfaces;

import java.io.IOException;

public interface InterfaceDAO {
	
	public void comando(String name) throws IOException;
	
	public void executa();
	
	public String mensagem();

}
