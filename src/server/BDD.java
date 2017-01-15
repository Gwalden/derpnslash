package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.Attack;
import utils.Effect;


/**
 * Class which will do every changes on the database and which can manage the server to use it
 * @author Léo
 *
 */
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
	
	/**
	 * This method connect to the database and create a new player in the database
	 * @param name
	 * @param mdp
	 */
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
	
	/**
	 * This method connect to the database and search for the attack with the name of the argument, if it exist, it will return a new attack with all the capabilities store in the database
	 * @param name
	 * @return
	 */
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
	
	/**
	 * This will create a new attack according to the name with the id given.
	 * @param name
	 * @param id
	 * @return 
	 */
	public Attack createAtt(String name, int id, int team) {
		Attack att = new Attack();
		switch (name) {
		case "fireBall" :	
			att.setId(id);
			att.setTeam(team);
			att.setName(name);
			att.setDamage(5);
			att.setCd(5);
			att.setEffect("");
			att.setTraverse(1);
			break;
		case "shadowBall" :
			att.setTeam(team);

			att.setId(id);
			att.setName(name);
			att.setDamage(10);
			att.setCd(8);
			att.setEffect("root");
			att.setTraverse(1);
			break;
		case "lightBall" :
			att.setId(id);
			att.setName(name);
			att.setDamage(7);
			att.setCd(12);
			att.setEffect("stun");
			att.setTraverse(1);
			att.setTeam(team);

			break;
		case "pinkArrow" :
			att.setId(id);
			att.setName(name);
			att.setDamage(15);
			att.setCd(15);
			att.setEffect("stun");
			att.setTraverse(1);
			att.setTeam(team);

			break;
		case "frostArrow" :
			att.setId(id);
			att.setName(name);
			att.setDamage(6);
			att.setCd(4);
			att.setEffect("frost");
			att.setTraverse(1);
			att.setTeam(team);

			break;
		case "circleArrow" :
			att.setId(id);
			att.setName(name);
			att.setDamage(8);
			att.setCd(15);
			att.setEffect("root");
			att.setTraverse(1);
			att.setTeam(team);

			break;
		}
		return att;
	}
}
