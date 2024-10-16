package org.elis.dao;

import java.util.List;

import org.elis.model.Gioco;
import org.elis.model.Libreria;
import org.elis.model.Utente;

public interface LibreriaDao {
	
	Libreria add(Libreria l);
	List<Libreria> findAll();
	Libreria findByName(String nome);
	List<Libreria> findByIdUtente(long id_utente);
	Libreria updateNome(long id, String nome);
	Libreria deleteById(long id);
	List<Gioco> findGiochiByIdLibreria(long id_libreria);
	List<Gioco> aggiungiGiocoALibreria(long id_libreria, Gioco gioco);
}
