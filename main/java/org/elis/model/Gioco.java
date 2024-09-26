package org.elis.model;

import java.time.LocalDateTime;

public class Gioco {
	
	private long id;
	private LocalDateTime data_creazione;
	private LocalDateTime data_ultima_modifica;
	private String nome;
	private LocalDateTime data_rilascio;
	private String descrzione;
	private String immagine;
	private boolean eliminato;
	private double prezzo;
	Offerta offerta;
	Utente utente;
	
	public Gioco(long id, LocalDateTime data_creazione, LocalDateTime data_ultima_modifica, String nome,
			LocalDateTime data_rilascio, String descrizione, String immagine, boolean eliminato, double prezzo,
			Offerta offerta, Utente utente) {
		
		
		this.id = id;
		this.data_creazione = data_creazione;
		this.data_ultima_modifica = data_ultima_modifica;
		this.nome = nome;
		this.data_rilascio = data_rilascio;
		this.descrzione = descrizione;
		this.immagine = immagine;
		this.eliminato = eliminato;
		this.prezzo = prezzo;
		this.offerta = offerta;
		this.utente = utente;
	}
	
	
	public Gioco() {
		
		
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public LocalDateTime getData_creazione() {
		return data_creazione;
	}


	public void setData_creazione(LocalDateTime data_creazione) {
		this.data_creazione = data_creazione;
	}


	public LocalDateTime getData_ultima_modifica() {
		return data_ultima_modifica;
	}


	public void setData_ultima_modifica(LocalDateTime data_ultima_modifica) {
		this.data_ultima_modifica = data_ultima_modifica;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public LocalDateTime getData_rilascio() {
		return data_rilascio;
	}


	public void setData_rilascio(LocalDateTime data_rilascio) {
		this.data_rilascio = data_rilascio;
	}


	public String getDescrzione() {
		return descrzione;
	}


	public void setDescrzione(String descrzione) {
		this.descrzione = descrzione;
	}


	public String getImmagine() {
		return immagine;
	}


	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}


	public boolean isEliminato() {
		return eliminato;
	}


	public void setEliminato(boolean eliminato) {
		this.eliminato = eliminato;
	}


	public double getPrezzo() {
		return prezzo;
	}


	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}


	public Offerta getOfferta() {
		return offerta;
	}


	public void setOfferta(Offerta offerta) {
		this.offerta = offerta;
	}


	public Utente getUtente() {
		return utente;
	}


	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	
	
	
	

}
