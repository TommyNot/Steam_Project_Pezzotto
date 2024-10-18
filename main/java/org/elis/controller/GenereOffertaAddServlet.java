package org.elis.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import org.elis.businesslogic.BusinessLogic;
import org.elis.model.Genere;
import org.elis.model.Gioco;
import org.elis.model.Offerta;
import org.elis.model.Recensione;


@WebServlet("/GenereOffertaAddServlet")
public class GenereOffertaAddServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GenereOffertaAddServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("idOfferta");
		String idGenere = request.getParameter("idGioco");
		
		  if (id == null || id.isEmpty() || idGenere == null || idGenere.isEmpty()) {
	            request.setAttribute("Error", "Campi idOfferta e idGenere obbligatorio.");
	            request.getRequestDispatcher("public-jsp/ErrorPage.jsp").forward(request, response);
	            return;               
	        }
	        long idOfferta;
	        
	        try {
	            idOfferta = Long.parseLong(id);
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); 
	            request.setAttribute("Error", "Errore nel formato dell'id selezionato.");
	            request.getRequestDispatcher("public-jsp/ErrorPage.jsp").forward(request, response);
	            return;
	        }
	        
	        Offerta offerta = BusinessLogic.findOffertaById(idOfferta);
	        if (offerta == null) {
	            request.setAttribute("Error", "Offerta non trovata.");
	            request.getRequestDispatcher("public-jsp/ErrorPage.jsp").forward(request, response);
	            return;
	        }
	        
            long idGenereLong;
	        
	        try {
	            idGenereLong = Long.parseLong(idGenere);
	        } catch (NumberFormatException e) {
	            e.printStackTrace(); 
	            request.setAttribute("Error", "Errore nel formato dell'id del genere selezionato.");
	            request.getRequestDispatcher("public-jsp/ErrorPage.jsp").forward(request, response);
	            return;
	        }
	        List<Genere> genereOfferta = BusinessLogic.genereOffertaAdd(idGenereLong, idOfferta);

	        if (genereOfferta == null || genereOfferta.isEmpty()) {
	            request.setAttribute("Error", "Nessun genere trovato per l'offerta selezionata.");
	            request.getRequestDispatcher("public-jsp/DashboardUtente.jsp").forward(request, response);
	            return;
	        }
	       
	}
	}


