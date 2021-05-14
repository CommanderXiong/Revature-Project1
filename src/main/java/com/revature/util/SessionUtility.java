package com.revature.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionUtility {
	private static SessionFactory sessionFactory;
	
	public synchronized static SessionFactory getSessionFactory() {
		if(sessionFactory == null) {
			sessionFactory = new Configuration()
					.setProperty("hibernate.connection.username", System.getenv("mariadb_username"))
					.setProperty("hibernate.connection.password", System.getenv("mariadb_password"))
					.configure("hibernate.cfg.xml")
					.buildSessionFactory();
		}
		return sessionFactory;
	}
}
