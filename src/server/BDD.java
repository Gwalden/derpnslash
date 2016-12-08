package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.Attack;

public class BDD {
	public BDD()
	{
		//initDb();
	}
	
	public void initDb()
	{
		try
		{
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Test","postgres","MDP");
			if (con!=null)
				System.out.println("Connected");
			Statement st = con.createStatement();
			st.execute("create table player (pid integer, pname text, pmdp text)");
			st.execute("create table spell (sid integer, sname text, degat integer,cooldown integer, effet text, traverse integer)");
			st.execute("insert into spell values(1,'fireball',5,3,'zone',1)");
			st.execute("insert into spell values(2,'frostBall',5,3,'zonegele',1)");
			st.execute("insert into spell values(3,'fireArrow',8,7,'zone',1)");
			st.execute("insert into spell values(4,'iceArrow',5,6,'gele',1)");
			st.execute("insert into spell values(5,'lightningArrow',4,8,'stun',1)");
		}
		catch(Exception ee)
		{
			ee.printStackTrace();
		}
	}
	
	public void addPlayer(String name, String mdp) {
		try {
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Test","postgres","MDP");
			Statement st = con.createStatement();
			st.execute("insert into player values(1,'"+name+"','"+mdp+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public Attack getAtt(String name)
	{
		Attack att = new Attack();
		try {
			Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Test","postgres","MDP");
			Statement st = con.createStatement();
			 ResultSet result = st.executeQuery("select * from spell where sname = '"+name+"'");
			 result.next();
			 att.setId(result.getInt(1));
			 att.setName(result.getString(2));
			 att.setDamage(result.getInt(3));
			 att.setCd(result.getInt(4));
			 att.setEffect(result.getString(5));
			 att.setTraverse(result.getInt(6));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return att;
	}
	
	
	public Attack createAtt(String name) {
		Attack att = new Attack();

		att.setId(1);
		att.setName(name);
		att.setDamage(5);
		att.setCd(5);
		att.setEffect("");
		att.setTraverse(1);
		return att;
	}
}
