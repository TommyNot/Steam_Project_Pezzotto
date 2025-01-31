package org.elis.jdbc;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.naming.java.javaURLContextFactory;
import org.elis.businesslogic.BusinessLogic;
import org.elis.dao.GiocoDao;
import org.elis.model.Genere;
import org.elis.model.Gioco;
import org.elis.model.Offerta;
import org.elis.model.Ruolo;
import org.elis.model.Utente;

public class GiocoDaoJDBC implements GiocoDao{
	
    private static GiocoDaoJDBC instance;
    
    
    private GiocoDaoJDBC() {}
    
   
    public static GiocoDaoJDBC getInstance() {
        if (instance == null) {
            instance = new GiocoDaoJDBC();
        }
        return instance;
    }
    
    


    @Override
    public Gioco add(String nome, LocalDate dataRilascio, String descrizione, String immagineBase64, double prezzo, Genere genere, Offerta offerta, Utente u) {
        String queryInsertGioco = "INSERT INTO gioco(nome, data_rilascio, descrizione, immagine, prezzo, id_offerta, id_casa_editrice) VALUES(?, ?, ?, ?, ?, ?, ?)";
        String querySelectUtente = "SELECT id, ruolo FROM utente WHERE id = ?";
        String queryInsertGenereGioco = "INSERT INTO genere_gioco(id_genere, id_gioco) VALUES(?, ?)";

        // Verifica se i parametri sono validi
        if (nome == null || nome.isEmpty() || descrizione == null || immagineBase64 == null || immagineBase64.isEmpty()) {
            System.out.println("Errore: Parametri invalidi forniti.");
            return null;
        }

        try (
            Connection c = JdbcDaoFactory.getConnection();
            PreparedStatement selectUtente = c.prepareStatement(querySelectUtente);
            PreparedStatement inserimentoGioco = c.prepareStatement(queryInsertGioco, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement inserimentoGenereGioco = c.prepareStatement(queryInsertGenereGioco);
        ) {
            // Verifica se l'utente esiste
            selectUtente.setLong(1, u.getId());
            ResultSet resultSetUtente = selectUtente.executeQuery();
            
            if (!resultSetUtente.next()) {
                System.out.println("Errore: Nessun utente trovato con ID: " + u.getId());
                return null;
            }

            // Inserimento gioco
            inserimentoGioco.setString(1, nome);
            inserimentoGioco.setDate(2, Date.valueOf(dataRilascio));
            inserimentoGioco.setString(3, descrizione);
            inserimentoGioco.setString(4, immagineBase64);
            inserimentoGioco.setDouble(5, prezzo);

            if (offerta != null) {
                inserimentoGioco.setLong(6, offerta.getId());
            } else {
                inserimentoGioco.setNull(6, Types.BIGINT);
            }

            inserimentoGioco.setLong(7, u.getId());

            int aggiornamento = inserimentoGioco.executeUpdate();
            long giocoId = 0;
            if (aggiornamento > 0) {
                ResultSet recuperIdGioco = inserimentoGioco.getGeneratedKeys();
                
                if (recuperIdGioco.next()) {
                    giocoId = recuperIdGioco.getLong(1);
                    
                    inserimentoGenereGioco.setLong(1, genere.getId());
                    inserimentoGenereGioco.setLong(2, giocoId);
                    inserimentoGenereGioco.executeUpdate();
                }
               

                Gioco nuovoGioco = new Gioco();
                nuovoGioco.setId(giocoId); 
                nuovoGioco.setNome(nome);
                nuovoGioco.setData_rilascio(dataRilascio);
                nuovoGioco.setDescrzione(descrizione);
                nuovoGioco.setByteImmagine(immagineBase64.getBytes()); 
                nuovoGioco.setPrezzo(prezzo);
                nuovoGioco.setGenere(genere);
                nuovoGioco.setOfferta(offerta);
                nuovoGioco.setIdUtente(u.getId());

                System.out.println("Gioco aggiunto con successo: " + nuovoGioco.getNome());
                return nuovoGioco;
            }

        } catch (SQLException e) {
            System.out.println("Errore SQL durante l'inserimento del gioco: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Errore generico: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }



    @Override
    public List<Gioco> findAll() {
        String query = "SELECT * FROM gioco";
        List<Gioco> giochi = new ArrayList<>();

        try (
            Connection c = JdbcDaoFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                Gioco g = new Gioco();

                String nome = rs.getString("nome");
                LocalDate dataRilascio = rs.getDate("data_rilascio").toLocalDate();
                String descrizione = rs.getString("descrizione");
                String immagine = rs.getString("immagine");  
                double prezzo = rs.getDouble("prezzo");
               
                g.setId(rs.getInt("id"));
                g.setNome(nome);
                g.setData_rilascio(dataRilascio);
                g.setDescrzione(descrizione);
                g.setByteImmagine(immagine.getBytes());  
                g.setIdUtente(rs.getInt("id_casa_editrice"));
                g.setPrezzo(prezzo);

                giochi.add(g);
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dei giochi: " + e.getMessage());
            e.printStackTrace();  
        } catch (Exception e) {
            e.printStackTrace();  
        }

        return giochi;
    }


	@Override
	public Gioco findByName(String nome) {
		
		String query = "SELECT * FROM gioco WHERE nome = ?";
		Gioco g = null;
		
		try(
				Connection c = JdbcDaoFactory.getConnection();
				PreparedStatement ps = c.prepareStatement(query);
				
				
			){
			
			ps.setString(1, nome);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				
		           g = new Gioco();
		          	g.setId(rs.getLong("id"));
	                g.setNome(rs.getString("nome"));
	                g.setData_rilascio(rs.getTimestamp("data_rilascio").toLocalDateTime().toLocalDate());
	                g.setDescrzione(rs.getString("descrizione"));
	                g.setByteImmagine(rs.getString("immagine").getBytes());
	                g.setPrezzo(rs.getDouble("prezzo"));
	                
	               System.out.println("gioco trovato");
	               
				
				
			}else {
				
				System.out.println("errore");
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g;
	}

	@Override
	public Gioco updateGiocoNome(long id, String nome) {
	    String query = "UPDATE gioco SET nome = ? WHERE id = ?";
	    
	    try (
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement ps = c.prepareStatement(query);
	    ) {
	        // Imposto i parametri
	        ps.setString(1, nome);
	        ps.setLong(2, id); 

	        int aggiornamento = ps.executeUpdate();
	        
	        if (aggiornamento > 0) {
	            System.out.println("Gioco aggiornato con successo");

	            Gioco giocoAggiornato = new Gioco();
	            giocoAggiornato.setId(id);
	            giocoAggiornato.setNome(nome); 

	            return giocoAggiornato; 
	        } else {
	            System.out.println("Errore nell'aggiornamento: nessun gioco trovato con l'ID specificato");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null; 
	}


	@Override
	public Gioco deleteGioco(long id) {
	    String findQuery = "SELECT * FROM gioco WHERE id = ?";
	    String deleteQuery = "DELETE FROM gioco WHERE id = ?";
	    String deleteGenereGioco = "DELETE FROM genere_gioco WHERE id_gioco = ?";
	    
	    try (
	    	
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement findPs = c.prepareStatement(findQuery);
	        PreparedStatement deletePs = c.prepareStatement(deleteQuery);
	    	PreparedStatement deleteGg = c.prepareStatement(deleteGenereGioco);
	    		
	    ) {
	        
	        findPs.setLong(1, id);
	        try (ResultSet rs = findPs.executeQuery()) {
	            if (rs.next()) {
	                
	                Gioco gioco = new Gioco();
	                gioco.setId(rs.getLong("id"));
	                gioco.setNome(rs.getString("nome"));
	                gioco.setData_rilascio(rs.getTimestamp("data_rilascio").toLocalDateTime().toLocalDate());
	                gioco.setDescrzione(rs.getString("descrizione"));
	                gioco.setByteImmagine(rs.getString("immagine").getBytes());
	                gioco.setEliminato(rs.getBoolean("eliminato"));
	                gioco.setPrezzo(rs.getDouble("prezzo"));
	                
	                
	                deleteGg.setLong(1, id);
	                int aggiornamentoGiocoGenere = deleteGg.executeUpdate();
	                
	                if(aggiornamentoGiocoGenere == 0) {
	                	
	                	System.out.println("Nessun gioco eliminato, nome non trovato");
	                    
	                }
	
	                
	                deletePs.setLong(1, id);
	                int aggiornamento = deletePs.executeUpdate();
	                
	                if (aggiornamento == 0) {
	                    System.out.println("Nessun gioco eliminato, nome non trovato");
	                    return null;
	                } else {
	                    System.out.println("Successo: gioco eliminato");
	                    return gioco;  
	                }
	            } else {
	                System.out.println("Nessun gioco trovato con id: " + id);
	                return null; 
	            }
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}



	@Override
	public Gioco findGiocoById(long id) {
	    
	    String query = "SELECT * FROM gioco WHERE id = ?";
	    
	    try (
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement ps = c.prepareStatement(query);
	    ) {
	        
	        ps.setLong(1, id);
	        
	        
	        ResultSet rs = ps.executeQuery();
	        
	        
	        if (rs.next()) {
	            
	            Gioco gioco = new Gioco();
	            gioco.setId(rs.getLong("id"));
	            gioco.setNome(rs.getString("nome"));
	            gioco.setData_rilascio(rs.getTimestamp("data_rilascio").toLocalDateTime().toLocalDate());
	            gioco.setDescrzione(rs.getString("descrizione"));
	            gioco.setByteImmagine(rs.getString("immagine").getBytes());
	            gioco.setEliminato(rs.getBoolean("eliminato"));
	            gioco.setPrezzo(rs.getDouble("prezzo"));
	            
	            
	            long idOfferta = rs.getLong("id_offerta");
	            if (!rs.wasNull()) {
	                
	                Offerta offerta = BusinessLogic.findOffertaById(idOfferta);
	                gioco.setOfferta(offerta);
	            }
	            
	            System.out.println("Gioco trovato: " + gioco.getNome());
	            return gioco;
	        } else {
	            System.out.println("Nessun gioco trovato con l'ID: " + id);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}


	@Override
	public Gioco updateGiocoDescrzione(long id,String descrzione) {
		
		String query = "UPDATE gioco SET descrizione = ? WHERE id = ?";
		
		try(
					Connection c = JdbcDaoFactory.getConnection();
					PreparedStatement ps = c.prepareStatement(query);
					
				
			){
			
			   ps.setString(1, descrzione); 
		       ps.setLong(2, id);
			
			int aggiornamento = ps.executeUpdate();
			
			if(aggiornamento > 0) {
				
				System.out.println("Descrzione success");
	            Gioco giocoAggiornato = findGiocoById(id);
	            return giocoAggiornato;
				
			}else {
				
				System.out.println("Oppss....error nell'aggiunta");
			}
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return null;
		
	}


	@Override
	public Gioco updateGiocoImmagine(long id, String immagine) {
	    
	    String query = "UPDATE gioco SET immagine = ? WHERE id = ?";
	    
	    try (
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement ps = c.prepareStatement(query);
	    ) {
	       
	        ps.setString(1, immagine);
	        ps.setLong(2, id);
	        
	        
	        int aggiornamento = ps.executeUpdate();
	        
	        if (aggiornamento > 0) {
	            System.out.println("Immagine aggiornata con successo.");
	            
	            
	            Gioco giocoAggiornato = findGiocoById(id);
	            return giocoAggiornato;
	            
	        } else {
	            System.out.println("Errore nell'aggiornamento dell'immagine.");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}


	@Override
	public Gioco updateGiocoPrezzo(long id, double prezzo) {
		
		String query = "UPDATE gioco SET nome = ?, prezzo=? WHERE id = ?";
		
		try(
				Connection c = JdbcDaoFactory.getConnection();
				PreparedStatement ps = c.prepareStatement(query);
				
			){
			
			ps.setDouble(1, prezzo);
			ps.setLong(2, id);
			
			int aggiornamento = ps.executeUpdate();
			
			if(aggiornamento > 0) {
				
				System.out.println("Success prezzo modificato");
			     Gioco giocoAggiornato = findGiocoById(id);
		         return giocoAggiornato;
			}else {
				
				System.out.println("Error modifica prezzo");
				
			}
			
			
			
		}catch(SQLException e) {
			
			e.printStackTrace();
		}catch(Exception e) {
			
			e.printStackTrace();
		}
		return null;
		
	}


	@Override
	public Gioco updateGiocoOfferta(long id, Offerta offerta) {
	    String query = "UPDATE gioco SET id_offerta = ? WHERE id = ?";
	    
	    try (Connection c = JdbcDaoFactory.getConnection(); 
	         PreparedStatement ps = c.prepareStatement(query)) {

	        if (offerta != null) {
	            ps.setLong(1, offerta.getId());
	        } else {
	            ps.setNull(1, Types.BIGINT); 
	        }
	        ps.setLong(2, id);
	        
	        int aggiornamento = ps.executeUpdate();
	        
	        if (aggiornamento > 0) {
	            System.out.println("Offerta aggiornata con successo.");
	            
	            return findGiocoById(id); 
	        } else {
	            System.out.println("Errore nell'offerta: ID non trovato.");
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return null; 
	}


	@Override
	public Gioco updateGiocoGenere(long id, Genere genere) {
	    String queryUpdateGenere = 
	        "UPDATE genere_gioco SET id_genere = ? WHERE id_gioco = ?";

	    String querySelectGioco = 
	        "SELECT g.*, u.Ruolo FROM gioco g " +
	        "JOIN utente u ON g.id_utente = u.id WHERE g.id = ?";

	    try (
	    	 Connection c = JdbcDaoFactory.getConnection();
	         PreparedStatement updateGenere = c.prepareStatement(queryUpdateGenere);
	         PreparedStatement selectGioco = c.prepareStatement(querySelectGioco)
	        ) {

	        
	        updateGenere.setLong(1, genere.getId());
	        updateGenere.setLong(2, id);
	        int rowsAffected = updateGenere.executeUpdate();

	        if (rowsAffected == 0) {
	            System.out.println("Nessun genere aggiornato, il gioco non esiste.");
	            return null; 
	        }

	        
	        selectGioco.setLong(1, id);
	        ResultSet rs = selectGioco.executeQuery();

	        if (!rs.next()) {
	            System.out.println("Il gioco non è stato trovato dopo l'aggiornamento.");
	            return null; 
	        }

	        
	        Gioco giocoAggiornato = new Gioco();
	        giocoAggiornato.setId(id);
	        giocoAggiornato.setNome(rs.getString("nome"));
	        giocoAggiornato.setData_rilascio(rs.getTimestamp("data_rilascio").toLocalDateTime().toLocalDate());
	        giocoAggiornato.setDescrzione(rs.getString("descrizione"));
	        giocoAggiornato.setByteImmagine(rs.getString("immagine").getBytes());
	        giocoAggiornato.setEliminato(rs.getBoolean("eliminato"));
	        giocoAggiornato.setPrezzo(rs.getDouble("prezzo"));

	       
	        List<Genere> generi = new ArrayList<>();
	        generi.add(genere);
	        //giocoAggiornato.setGenere(generi);

	        System.out.println("Genere aggiornato per il gioco con ID " + id);
	        return giocoAggiornato;

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null; 
	}


	@Override
	public List<Gioco> findGiocoGenereByGenere(long idGenere) {
	    
	    String query = "SELECT g.*, o.* FROM gioco g " +
	            "JOIN genere_gioco gg ON g.id = gg.id_gioco " +
	            "JOIN genere ge ON ge.id = gg.id_genere " +
	            "LEFT JOIN offerta o ON g.id_offerta = o.id " +
	            "WHERE ge.id = ?"; // Cambiato per utilizzare l'ID del genere
	    
	    List<Gioco> giochi = new ArrayList<>();
	    try (
	            Connection c = JdbcDaoFactory.getConnection();
	            PreparedStatement ps = c.prepareStatement(query);
	    ) {
	        ps.setLong(1, idGenere); // Usa l'ID del genere
	        
	        ResultSet rs = ps.executeQuery();
	        
	        while (rs.next()) {
	            Gioco gioco = new Gioco();
	            gioco.setId(rs.getLong("g.id"));
	            gioco.setNome(rs.getString("g.nome"));
	            gioco.setData_rilascio(rs.getTimestamp("g.data_rilascio").toLocalDateTime().toLocalDate());
	            gioco.setDescrzione(rs.getString("g.descrizione"));
	            gioco.setByteImmagine(rs.getString("g.immagine").getBytes());
	            gioco.setPrezzo(rs.getDouble("g.prezzo"));
	            
	            long idOfferta = rs.getLong("o.id");
	            if (!rs.wasNull()) {
	                Offerta offerta = new Offerta();
	                offerta.setId(idOfferta);
	                offerta.setNome(rs.getString("o.nome"));
	                offerta.setSconto(rs.getDouble("o.sconto"));
	                offerta.setData_inizio(rs.getTimestamp("o.data_inizio").toLocalDateTime());
	                offerta.setData_fine(rs.getTimestamp("o.data_fine").toLocalDateTime());
	                
	                gioco.setOfferta(offerta); // Assicurati di impostare l'offerta nel gioco
	            }
	            
	            giochi.add(gioco);
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return giochi;
	}


	@Override
	public List<Gioco> findGiocoOffertaByOfferta(Offerta offerta) {
	    String query = "SELECT g.*, o.* FROM gioco g JOIN offerta o ON g.id_offerta = o.id WHERE g.id_offerta = ?";
	    List<Gioco> giochi = new ArrayList<>();

	    try (
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement ps = c.prepareStatement(query);
	    ) {
	        
	        ps.setLong(1, offerta.getId());

	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Gioco gioco = new Gioco();
	            gioco.setId(rs.getLong("g.id"));
	            gioco.setNome(rs.getString("g.nome"));
	            gioco.setData_rilascio(rs.getTimestamp("g.data_rilascio").toLocalDateTime().toLocalDate());
	            gioco.setDescrzione(rs.getString("g.descrizione"));
	            gioco.setByteImmagine(rs.getString("g.immagine").getBytes());
	            gioco.setPrezzo(rs.getDouble("g.prezzo"));

	            
	            giochi.add(gioco);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    
	    return giochi;
	}


	@Override
	public Gioco updateGiocoDataRilascio(long id, LocalDate data) {
	    
	    String query = "UPDATE gioco SET data_rilascio = ? WHERE id = ?";

	    
	    try (Connection c = JdbcDaoFactory.getConnection();
	         PreparedStatement ps = c.prepareStatement(query)) {
	        
	        
	        ps.setTimestamp(1, java.sql.Timestamp.valueOf(data));
	        ps.setLong(2, id);
	        
	        
	        int aggiornamento = ps.executeUpdate();
	        
	        if (aggiornamento > 0) {
	            System.out.println("Data di rilascio aggiornata con successo per il gioco ID: " + id);
	            
	            Gioco giocoAggiornato = findGiocoById(id);
	            return giocoAggiornato;
	        } else {
	            System.out.println("Errore nell'aggiornamento della data di rilascio");
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public List<Gioco> VisualizzaGiochiPerUtente(long idUtente) {
	    List<Gioco> giochi = new ArrayList<>();
	    
	    String sql = "SELECT * FROM gioco WHERE id_casa_editrice = ?";

	    try (
	        Connection c = JdbcDaoFactory.getConnection();
	        PreparedStatement ps = c.prepareStatement(sql)) {
	        
	        ps.setLong(1, idUtente);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	        	Gioco g = new Gioco();
				
				String nome = rs.getString("nome");
				LocalDate dataRilascio = rs.getDate("data_rilascio").toLocalDate();
				String descrzione = rs.getString("descrizione");
				String immagine = rs.getString("immagine");
				
				double prezzo = rs.getDouble("prezzo");
				
				g.setId(rs.getInt("id"));
				g.setNome(nome);
				g.setData_rilascio(dataRilascio);
				g.setDescrzione(descrzione);
				g.setByteImmagine(immagine.getBytes());
				
				g.setPrezzo(prezzo);
				
				
				giochi.add(g);
				
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }

	    return giochi;
	}





}
