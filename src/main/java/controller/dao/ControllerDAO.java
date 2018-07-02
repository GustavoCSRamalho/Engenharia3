package controller.dao;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import view.View;

public class ControllerDAO implements  interfaces.InterfaceDAO{

	private Process p;
	private String mensagem;
	
	Model model;
	
	View view;
	
	public ControllerDAO(Model model, View view){
		this.model = model;
		this.view = view;
	}
	
	public String mensagem() {
		return this.mensagem;
	}
	
	public void send(Update update){
		view.sendTypingMessage(update);
		model.executa(update);
	}

}
