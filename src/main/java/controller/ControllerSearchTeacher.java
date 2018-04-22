package controller;

import com.pengrad.telegrambot.model.Update;
import interfaces.InterfaceSearch;
import model.Model;
import view.View;

public class ControllerSearchTeacher implements InterfaceSearch {
	
	private Model model;
	private View view;
	
	public ControllerSearchTeacher(Model model, View view){
		this.model = model; //connection Controller -> Model
		this.view = view; //connection Controller -> View
	}
	
	public void search(Update update){
		view.sendTypingMessage(update);
		model.searchTeacher(update);
	}

}
