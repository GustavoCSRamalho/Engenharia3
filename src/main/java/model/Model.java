package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.nd4j.linalg.primitives.Pair;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;
import com.pengrad.telegrambot.model.Update;

import controller.DP.ParagraphVectorsClassifierExample;
import controller.dao.ControllerDAO;
import interfaces.InterfacePVC;
import interfaces.Observer;
import interfaces.Subject;

public class Model implements Subject {

	private List<Observer> observers = new LinkedList<Observer>();

	private ControllerDAO controllerDAO;

	private static Model uniqueInstance;

	private ObjectContainer filmes;

	private ObjectContainer classificacoes;

	private Model() {
		filmes = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/filmes.db4o");
		classificacoes = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "bd/classificacoes.db4o");
	}

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObservers(long chatId, String studentsData) {
		for (Observer observer : observers) {
			observer.update(chatId, studentsData);
		}
	}
	
	public Boolean existeFilme(String nome) {
		Query query = filmes.query();
		query.constrain(Filme.class);
		ObjectSet<Filme> allFilmes = query.execute();
		for (Filme filme : allFilmes) {
			if (filme.getNome().equalsIgnoreCase(nome)) {
				return true;
			}
		}

		return false;
	}

	public Boolean existeClassificacao(String nome) {
		Query query = classificacoes.query();
		query.constrain(Classificacao.class);
		ObjectSet<Classificacao> allClassificacoes = query.execute();
		for (Classificacao filme : allClassificacoes) {
			if (filme.getNome().equalsIgnoreCase(nome)) {
				return true;
			}
		}

		return false;
	}

	public String pegaClassificacao(String nome) {

		String classificacaoTexto = null;
		Query query = classificacoes.query();
		query.constrain(Classificacao.class);
		ObjectSet<Classificacao> allClassificacoes = query.execute();
		for (Classificacao classificacao : allClassificacoes) {
			if (classificacao.getNome().equalsIgnoreCase(nome)) {
				classificacaoTexto = classificacao.getClassificacoes();
			}
		}
		return classificacaoTexto;
	}

	public Boolean verificaFilme(String nome) {
		Query query = filmes.query();
		query.constrain(Filme.class);
		ObjectSet<Filme> allFilmes = query.execute();
		for (Filme filme : allFilmes) {
			if (filme.getNome().equalsIgnoreCase(nome)) {
				return true;
			}
		}
		return false;
	}

	public String sinopseFilme(String nomeFilme) {
		String sinopse = null;
		Query query = filmes.query();
		query.constrain(Filme.class);
		ObjectSet<Filme> allFilmes = query.execute();
		for (Filme filme : allFilmes) {
			if (filme.getNome().equalsIgnoreCase(nomeFilme)) {
				sinopse = filme.getSinopse();
			}
		}
		return sinopse;
	}

	public void executa(Update update) {

		String nomeFilme = update.message().text();
		String classificacao = null;
		if (existeClassificacao(nomeFilme)) {
			classificacao = pegaClassificacao(nomeFilme);
		} else {
			if (existeFilme(nomeFilme)) {
				classificacao = getClassificacaoFilme(nomeFilme);
			}
		}

		if (classificacao == null) {
			this.notifyObservers(update.message().chat().id(),
					"Não foi possível prosseguir, pois, este filme não se" + "encontra no banco de dados!");
		} else {
			this.notifyObservers(update.message().chat().id(), classificacao);
		}

	}

	public String getClassificacaoFilme(String nomeFilme) {

		String sinopse = sinopseFilme(nomeFilme);
		ParagraphVectorsClassifierExample app = new ParagraphVectorsClassifierExample();
		List<Pair<String, Double>> scores = null;
		File file = new File(
				"/home/gustavo/Documents/fatec/Engenharia3/BotMVC/src/main/resources/paravec/unlabeled/science/f01.txt");
		FileWriter arq = null;
		BufferedWriter writer = null;
		try {
			arq = new FileWriter(file);
			try {
				writer = new BufferedWriter(arq);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.write(sinopse);
			writer.close();
			arq.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			app.makeParagraphVectors();
			scores = app.checkUnlabeledData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 1;
		String enviar = "Classificação : \n";
		for (Pair<String, Double> score : scores) {
			enviar = enviar + i + " : " + score.getFirst() + "\n";
			i++;
		}

		addClassificacao(new Classificacao(nomeFilme, enviar));

		return enviar;
	}

	public boolean addFilme(Filme filme) {
		if (isFilmeAvaiable(filme.getNome())) {
			filmes.store(filme);
			filmes.commit();
			return true;
		}
		return false;
	}

	public boolean addClassificacao(Classificacao classificacao) {
		classificacoes.store(classificacao);
		classificacoes.commit();
		return true;
	}

	public Boolean isFilmeAvaiable(String nomeFilme) {
		Query query = filmes.query();
		query.constrain(Filme.class);
		ObjectSet<Filme> allFilmes = query.execute();
		for (Filme filme : allFilmes) {
			if (filme.getNome().equalsIgnoreCase(nomeFilme)) {
				return false;
			}
		}
		return true;
	}
}
