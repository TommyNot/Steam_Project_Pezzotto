package org.elis.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.elis.businesslogic.BusinessLogic;
import org.elis.model.Gioco;
import org.elis.model.Ruolo;
import org.elis.model.Utente;

/**
 * Servlet implementation class EliminaGioco
 */
public class GiocoEliminaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GiocoEliminaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String eliminaGiocoNome = request.getParameter("productId");
        
        
        
        HttpSession sessione = request.getSession(false);
        if (sessione == null) {
            response.sendRedirect("public-jsp/PaginaLogin.jsp");
            return;
        }
        
        


        Utente utente = (Utente) sessione.getAttribute("utenteLoggato");
        
        System.out.println(utente.getRuolo());
        if (utente.getRuolo() == Ruolo.UTENTE_BASE) {
            response.sendRedirect("public-jsp/ErrorAccessoNegatoPage.jsp");
            return;
        }
     
        
        
        if (eliminaGiocoNome == null || eliminaGiocoNome.isBlank()) {
            String errore = "Il nome del gioco non può essere vuoto.";
            request.setAttribute("errore", errore); 
            
            request.getRequestDispatcher("WEB-INF/private-jsp/DashboardPublisher.jsp").forward(request, response);
            return; 
        }
        
        long idGioco = 0;
        try {
        	
        	idGioco = Long.parseLong(eliminaGiocoNome);
        	
        }catch(NumberFormatException e) {
        	
        	e.printStackTrace();
        }
       
        	
        	
        	System.out.println(idGioco);
    
        
        	Gioco giocoEliminato = BusinessLogic.eliminaGioco(idGioco);
            
        
        
        
			
			switch(utente.getRuolo()){
			
			case PUBLISHER:
				if (giocoEliminato != null ) {
        	
		            String successo = "Il gioco '" + eliminaGiocoNome + "' è stato eliminato con successo.";
		            request.setAttribute("successo", successo);
		            request.getRequestDispatcher("WEB-INF/private-jsp/DashboardPublisher.jsp").forward(request, response);
				} else {
		            String errore = "Il gioco con nome '" + eliminaGiocoNome + "' non è stato trovato.";
		            request.setAttribute("errore", errore);
		            request.getRequestDispatcher("public-jsp/ErrorPage.jsp").forward(request, response);
		        }
				break;
			case ADMIN:
				if (giocoEliminato != null) {
			        response.getWriter().write("Gioco eliminato con successo.");
			    } else {
			        response.getWriter().write("Errore: Gioco non trovato o eliminazione fallita.");
			    }
						
				break;
				
			default:
				request.getRequestDispatcher("WEB-INF/public-jsp/ErrorPage.jsp").forward(request, response);
					break;
			}
     
        
    }
}
