package view;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;
//import controller.ControllerSearchSudent;
//import controller.ControllerSearchTeacher;
import controller.dao.ControllerDAO;
import interfaces.InterfaceDAO;
import interfaces.InterfaceSearch;
import interfaces.Observer;
import model.Model;

public class View implements Observer {

	TelegramBot bot = TelegramBotAdapter.build("407669081:AAGdqgQ8_yA3nudOLBt6pw_kdI5flOWgpfE");

	// Object that receives messages
	GetUpdatesResponse updatesResponse;
	// Object that send responses
	SendResponse sendResponse;
	// Object that manage chat actions like "typing action"
	BaseResponse baseResponse;

	Boolean ok = false;

	int queuesIndex = 0;

	InterfaceSearch controllerSearch; // Strategy Pattern -- connection View -> Controller

	InterfaceDAO controllerDAO;

	boolean searchBehaviour = false;

	private Model model;

	public View(Model model) {

		this.model = model;
		try {
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(queuesIndex).timeout(0));
		} catch (Exception e) {
			System.out.println("\n!!!EERO NO updateResponse fora do while(true)!!!\n" + e.getMessage());

		}

		List<Update> updates = updatesResponse.updates();

		for (Update update : updates) {
			queuesIndex += update.updateId();
		}
	}

	// public void setControllerSearch(InterfaceSearch controllerSearch){ //Strategy
	// Pattern
	// this.controllerSearch = controllerSearch;
	// }

	public void setControllerDAO(InterfaceDAO controllerDAO) { // Strategy Pattern
		this.controllerDAO = controllerDAO;
	}

	public void receiveUsersMessages() {

		// infinity loop
		while (true) {

			// taking the Queue of Messages
			try {
				updatesResponse = bot.execute(new GetUpdates().limit(100).offset(queuesIndex).timeout(0));
			} catch (Exception e) {
				System.out.println("\n!!!ERRO NO updateResponse dentro do while(true)!!!\n" + e.getMessage());
				continue;
			}
			// Queue of messages
			List<Update> updates = updatesResponse.updates();
//			bot.execute(new SendMessage(update.message().chat().id(), "Digite o nome do filme : "));

			// taking each message in the Queue
			for (Update update : updates) {

				// updating queue's index
				queuesIndex = update.updateId() + 1;

				if ((update.message() != null || (!update.message().equals(""))) && ok) {
					setControllerDAO(new ControllerDAO(model, this));
					this.searchBehaviour = true;
					sendTypingMessage(update);
					this.callController(update);
					ok = false;
				}
				
					sendResponse = bot
							.execute(new SendMessage(update.message().chat().id(), "Digite o nome do filme : "));
					ok = true;
				

			}

		}

	}

	public void callController(Update update) {
		this.controllerDAO.send(update);
	}

	public void update(long chatId, String studentsData) {
		sendResponse = bot.execute(new SendMessage(chatId, studentsData));
		this.searchBehaviour = false;
	}

	public void sendTypingMessage(Update update) {
		baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}
}