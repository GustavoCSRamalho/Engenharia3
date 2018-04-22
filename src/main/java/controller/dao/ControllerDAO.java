package controller.dao;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ControllerDAO implements  interfaces.InterfaceDAO{
	private Runtime r;
	private String[] cmd;
	private Process p;
	private static Scanner scanner;
	private String mensagem;
	
	ControllerDAO(){
		r = Runtime.getRuntime();
	}
	public void comando(String comando) throws IOException {
		String[] configuracao = { "/bin/sh", "-c", comando };
		this.cmd = configuracao;
		p = r.exec(configuracao);
		scanner = new Scanner(p.getInputStream()).useDelimiter("$$");
		executa();
	}
	
	public void executa() {
		String mensagemEntrada = "";
		String mensagemErro = "";
		if (scanner.hasNext()) {
		    System.out.println("stdout");
		    mensagemEntrada = scanner.useDelimiter("$$").next();
//		    System.out.println(scanner.useDelimiter("$$").next());
		    
		    System.out.println(mensagemEntrada);
		}

		scanner = new Scanner(p.getErrorStream()).useDelimiter("$$");
		if (scanner.hasNext()) {
		    System.out.println("stdout");
		    mensagemErro = scanner.useDelimiter("$$").next();
		    System.out.println(mensagemErro);
		}
		
		System.out.println("Mensagem "+mensagemEntrada);
		System.out.println("Erro "+mensagemErro);
		if(!mensagemEntrada.isEmpty()) {
			this.mensagem = mensagemEntrada;
		}else {
			this.mensagem = mensagemErro;
		}
		
	}
	
	public String mensagem() {
		return this.mensagem;
	}

}