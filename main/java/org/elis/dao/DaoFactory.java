package org.elis.dao;


import javax.management.InstanceAlreadyExistsException;

import org.elis.jdbc.JdbcDaoFactory;
import org.elis.jdbc.UtenteDaoJDBC;
import org.elis.jpa.DaoFactoryJpa;

public abstract class DaoFactory {
	
	private static DaoFactory instance;
	
	public abstract UtenteDao getUtenteDao();
	
	public abstract GiocoDao getGiocoDao();
	
	public abstract LibreriaDao getLibreriaDao();
	
	public abstract GenereDao getGenereDao();
	
	public abstract OffertaDao getOffertaDao();
	
	public abstract RecensioneDao getRecensioneDao();
	
	
	public static DaoFactory getDaoFactory(String s) {
		
		System.out.println(s);
		System.out.println(instance);
		if(instance == null) {
			
			switch(s) {
			
			case"JDBC":
				instance = new JdbcDaoFactory();
				break;
			case "JPA":
				instance = new DaoFactoryJpa();
			}
		}
		System.out.println(instance);
		return instance;
	}
}
