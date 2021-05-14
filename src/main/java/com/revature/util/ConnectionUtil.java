package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.mariadb.jdbc.Driver;

public class ConnectionUtil {


	public static Connection getConnection() throws SQLException{
		DriverManager.registerDriver(new Driver());
		
		String username = System.getenv("mariadb_username");
		String password = System.getenv("mariadb_password");
		
		String url = "jdbc:mariadb://localhost:3306/reimbursement";
		
		return DriverManager.getConnection(url, username, password);
	}
}
