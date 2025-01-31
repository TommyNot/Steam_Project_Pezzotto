package org.elis.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import org.elis.businesslogic.BusinessLogic;
import org.elis.model.Gioco;
import org.elis.model.Utente;

/**
 * Servlet implementation class GiocoListaCompletaServlet
 */
@WebServlet("/GiocoListaCompletaServlet")
public class GiocoListaCompletaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GiocoListaCompletaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     
		  HttpSession sessione = request.getSession(false);
	        if (sessione == null) {
	            response.sendRedirect("public-jsp/PaginaLogin.jsp");
	            return;
	        }
	        
	        Utente utente = (Utente) sessione.getAttribute("utenteLoggato");
	        
        List<Gioco> listaGiochi = BusinessLogic.VisualizzaTuttiGiochi();
        System.out.println(listaGiochi);
        
        if (listaGiochi == null || listaGiochi.isEmpty()) {
            
            request.getRequestDispatcher("public-jsp/PageGiochi.jsp").forward(request, response);
            return;
        }


      
        	
         request.setAttribute("giochi", listaGiochi);
         
         request.getRequestDispatcher("public-jsp/PageGiochi.jsp").forward(request, response);
        
        

        
       
	}

	

}
