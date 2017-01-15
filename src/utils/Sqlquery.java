package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sqlquery {

	private final String SQLURL = "jdbc:postgresql://localhost/derpnslash";
	@SuppressWarnings("unused")
	private final String SQLLOG = "derpnslash";
	@SuppressWarnings("unused")
	private final String SQLPAS = "azerty123";
	private Connection dbco;
	
	public Sqlquery() {
		try {
			Class.forName("org.postgresql.Driver");
			this.dbco = DriverManager.getConnection(SQLURL);
		} catch (ClassNotFoundException e) {
			System.err.println("enable to load postgresqldriver");
			e.printStackTrace();
			System.exit(1);
		} catch (SQLException e) {
			System.err.println("enable to connect to BDD");
			e.printStackTrace();
			System.exit(1);
		}	
	}
	
	public boolean isUser(String login, String pass){
		String password = new String(pass);
		String sqlquery = "Select * from users where password = \'"+ password+"\' AND login = \'"+ login+"\'";
		try {
			Statement statement = dbco.createStatement();
			ResultSet res = statement.executeQuery(sqlquery);
			if (res.next())
				return true;
		} catch (SQLException e) {
			return false;
		}
		return false;
	}
	
}
