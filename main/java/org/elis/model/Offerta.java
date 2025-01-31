package org.elis.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity()
@Table(name="Offerta")
public class Offerta {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@CreationTimestamp
    @Column(name = "data_creazione", nullable = false)
	private LocalDateTime data_creazione;
	
	@UpdateTimestamp
    @Column(name = "data_ultima_modifica")
	private LocalDateTime data_ultima_modifica;
	
	@Column(name = "nome", nullable = false, unique = true,length = 255)
	private String nome;
	
	@Column(name = "sconto", nullable = false)
	private double sconto;
	
	@Column(name = "data_inizio", nullable = false)
	private LocalDate data_inizio;
	
	@Column(name = "data_fine", nullable = false)
	private LocalDate data_fine;
	
    
    @OneToMany(mappedBy = "offertaGioco")
	List<Gioco> gioco;
    
    @OneToMany(mappedBy = "offertaGenere")
	List<Genere> genere;
	
	
	public Offerta(long id, LocalDateTime data_creazione, LocalDateTime data_ultima_modifica, String nome,
			double sconto, LocalDate data_inizio, LocalDate data_fine) {
		super();
		this.id = id;
		this.data_creazione = data_creazione;
		this.data_ultima_modifica = data_ultima_modifica;
		this.nome = nome;
		this.sconto = sconto;
		this.data_inizio = data_inizio;
		this.data_fine = data_fine;
	}
	
	public Offerta() {
		
		
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

	public double getSconto() {
		return sconto;
	}

	public void setSconto(double sconto) {
		this.sconto = sconto;
	}

	public LocalDate getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(LocalDate data_inizio) {
		this.data_inizio = data_inizio;
	}

	public LocalDate getData_fine() {
		return data_fine;
	}

	public void setData_fine(LocalDate data_fine) {
		this.data_fine = data_fine;
	}
	
	
	
	
	

}
