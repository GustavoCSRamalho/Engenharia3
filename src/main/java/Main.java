import model.Filme;
import model.Model;
//import model.Student;
//import model.Teacher;
import view.View;

public class Main {

	private static Model model;
	
	public static void main(String[] args) {

		model = Model.getInstance();
		initializeModel(model);
		View view = new View(model);
		model.registerObserver(view); //connection Model -> View
		view.receiveUsersMessages();

	}
	
	public static void initializeModel(Model model){
//		model.addStudent(new Student("joao", "111"));
//		model.addStudent(new Student("thomas", "222"));
//		
//		model.addTeacher(new Teacher("percy", "computing"));
		
		Filme filme = new Filme();
		filme.setNome("guerra infinita");
		filme.setSinopse("Uma jornada cinematográfica sem precedentes produzida por dez anos através todo o Universo Cinematográfico da Marvel, Vingadores: Guerra Infinita traz para as telas a batalha definitiva e mais mortal de todos os tempos. Os Vingadores e seus super-heróis aliados precisam estar dispostos a sacrificar tudo em uma tentativa de derrotar o poder de Thanos antes que sua onda de devastação e ruína coloque um fim no universo.");
		model.addFilme(filme);
	}

}