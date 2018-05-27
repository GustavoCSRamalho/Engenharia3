package interfaces;

import java.io.IOException;

import com.pengrad.telegrambot.model.Update;

public interface InterfaceDAO {
	
//	public void comando(String name) throws IOException;
	
//	public void executa();
	
	public String mensagem();
	
	public void send(Update update);

}
