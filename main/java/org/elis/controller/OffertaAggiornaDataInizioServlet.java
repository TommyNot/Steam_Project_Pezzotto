package org.elis.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.elis.businesslogic.BusinessLogic;
import org.elis.model.Libreria;
import org.elis.model.Offerta;
import org.elis.model.Ruolo;
import org.elis.model.Utente;


@WebServlet("/OffertaAggiornaDataInizioServlet")
public class OffertaAggiornaDataInizioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public OffertaAggiornaDataInizioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if(session == null) {
			request.getRequestDispatcher("public-jsp/PaginaLogin.jsp");
			return;
		}
		
		
		
		String nuovaDataInizio = request.getParameter("nuovaDataInizio");
		String id = request.getParameter("id");
		

	      LocalDateTime nuovaData = null;
	        if (nuovaDataInizio != null && !nuovaDataInizio.isEmpty()) {
	            try {
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	                nuovaData = LocalDateTime.parse(nuovaDataInizio, formatter); 
	            } catch (DateTimeParseException e) {
	                request.setAttribute("errore", "Errore nella formattazione della data e ora: " + e.getMessage());
	                request.getRequestDispatcher("public-jsp/DashboardAdmin.jsp").forward(request, response);
	                System.out.println("Errore nella formattazione della data e ora");
	                return; 
	            }
	        }
	        
	    	if (id == null || id.isBlank()) {
	            String errore = "L'id  dell'offerta non può essere vuoto.";
	            request.setAttribute("errore", errore); 
	            request.getRequestDispatcher("WEB-INF/private-jsp/DashboardAdmin.jsp").forward(request, response);
	            return; 
	        }
		
	    long idOfferta = 0;
		  
			  try {
		        	
		        	idOfferta = Long.parseLong(id);
		        	
		        }catch(Exception e) {
		        	
		        	System.out.println("errore");
		        }

		Utente utente = (Utente) session.getAttribute("utenteLoggato");
		if(utente != null) {
			long idUtente = utente.getId();
			
			Utente u = BusinessLogic.UtenteFindById(idUtente);
			if(u != null) {
				boolean isAdmin = u.getRuolo() == Ruolo.ADMIN;
				if(isAdmin) {
					Offerta OffertaNuovaDataInizio = BusinessLogic.updateDataInizioOfferta(idOfferta, nuovaData);
					
					if(OffertaNuovaDataInizio != null) {
						System.out.println("L'inizio dell'offerta è stato aggiornato con successo.");
					}else {
						request.getRequestDispatcher("public-jsp/ErrorPage.jsp");
						return;
					}
				}else {
					System.out.println("L'utente non è un admin.");
				}
			}else {
				System.out.println("Utente non trovato con id " + idUtente);
			}
		}else {
			System.out.println("Nessun utente trovato nella sessione.");
		}
		
	}

}
