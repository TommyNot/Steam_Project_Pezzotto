package org.elis.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

public class Gioco {

    private long id;
    private LocalDateTime data_creazione;
    private LocalDateTime data_ultima_modifica;
    private String nome;
    private LocalDate data_rilascio;
    private String descrzione;
    private byte[] byteImmagine; 
    private boolean eliminato;
    private double prezzo;
    private Offerta offerta;
    private Genere genere;
    private long idUtente;

    // Costruttore
    public Gioco(long id, LocalDateTime data_creazione, LocalDateTime data_ultima_modifica, String nome,
                 LocalDate data_rilascio, String descrizione, byte[] byteImmagine, boolean eliminato, double prezzo,
                 Offerta offerta, long idUtente) {
        this.id = id;
        this.data_creazione = data_creazione;
        this.data_ultima_modifica = data_ultima_modifica;
        this.nome = nome;
        this.data_rilascio = data_rilascio;
        this.descrzione = descrizione;
        this.byteImmagine = byteImmagine;
        this.eliminato = eliminato;
        this.prezzo = prezzo;
        this.offerta = offerta;
        this.idUtente = idUtente;
    }

    public Gioco() {
        // Costruttore vuoto
    }

    // Getters e Setters
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

    public LocalDate getData_rilascio() {
        return data_rilascio;
    }

    public void setData_rilascio(LocalDate dataRilascio) {
        this.data_rilascio = dataRilascio;
    }

    public String getDescrzione() {
        return descrzione;
    }

    public void setDescrzione(String descrzione) {
        this.descrzione = descrzione;
    }

    public String getImmagine() {
        return Base64.getEncoder().encodeToString(byteImmagine);
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

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(long idUtente) {
        this.idUtente = idUtente;
    }

    public byte[] getByteImmagine() {
        return byteImmagine;
    }

    public void setByteImmagine(byte[] byteImmagine) {
        this.byteImmagine = byteImmagine;
    }
}
