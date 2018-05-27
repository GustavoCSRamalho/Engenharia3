package model;

import java.util.List;

public class Classificacao {
	
	private String nome;
	
	private String classificacoes;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getClassificacoes() {
		return classificacoes;
	}

	public void setClassificacoes(String classificacoes) {
		this.classificacoes = classificacoes;
	}
	
	public Classificacao() {}
	
	public Classificacao(String nome, String classificacoes) {
		this.nome = nome;
		this.classificacoes = classificacoes;
	}

	

}
