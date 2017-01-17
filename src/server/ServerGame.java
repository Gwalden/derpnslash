package server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.newdawn.slick.Input;

import client.MainClient;
import utils.Attack;
import utils.Effect;
import utils.Event;
import utils.Network.addPlayer;
import utils.Network.attackPlayer;
import utils.Network.movePlayer;
import utils.Network.removePlayer;
import utils.Network.removeSpell;
import utils.Network.updatep;
import utils.Player;

public class ServerGame implements Runnable {
	
	/**
	 * Game state running
	 */
	public boolean run = true;
	/**
	 * Game iteration by second wanted
	 */
	public int TICK = 60;
	/**
	 * Optimal time for one iteration of the game
	 */
	public long OPTIMAL_TIME = 1000000000 / TICK;
	/**
	 * list of player
	 */
	public ArrayList<Player> playerl;
	/**
	 * list of Event that the game want send  
	 */
	private	LinkedList<Event> gameEventSend;
	/**
	 * list of Event receive to use
	 */
	private	LinkedList<Event> gameEventReceive;
	/**
	 * shared list of event received by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistReceive;
	/**
	 * shared list of event to send by the server
	 */
	public ConcurrentLinkedQueue<Event> ElistSend;
	/*
	 * Server
	 */
	
	public NetworkServer server;
	
	public BDD database;
	
	
	private int idSpell;
	private int idEff;
	private int team = 0;
	
	private ArrayList<Attack> spell;

	public ServerGame() {
		this.idSpell = 0;
		this.gameEventSend = new LinkedList<>();
		this.gameEventReceive = new LinkedList<>();
		this.ElistReceive = new ConcurrentLinkedQueue<>();
		this.ElistSend = new ConcurrentLinkedQueue<>();
		this.playerl = new ArrayList<>();
		this.spell = new ArrayList<>();
		this.database = new BDD();
	}
	

	public int init(){
		this.server = new NetworkServer(this);
		return this.server.init();
	}
	
	public void run() {
		new Thread(this.server).start();
		double update = System.nanoTime();
		while (run){ 
			double lastUpdateTime = System.nanoTime();
			this.getEventReceive();
			this.treatEvent();
			this.update(update);
			update = System.nanoTime();
			this.setEventSend();
			try {
				while (System.nanoTime() - lastUpdateTime < OPTIMAL_TIME)
					Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method that will change all the spells position and will send to the  client the spells with new coordinates
	 * This will also remove a spell if it reached the end of it's range
	 */
	private void spellUpdate() {
		if (spell.size() != 0) {
			for (int i = 0 ; i < spell.size(); i++) {
				Attack att = spell.get(i);
				if ((att.getDirection() == 3) && (att.getXbeg() + 1 <= att.getXend()))
				{
					att.setXbeg(att.getXbeg() + 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if ((att.getDirection() == 2) && (att.getYbeg() <= att.getYend())) {
					att.setYbeg(att.getYbeg() + 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if (att.getDirection() == 1 && att.getXbeg() >= att.getXend()) {
					att.setXbeg(att.getXbeg() - 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if (att.getDirection() == 0 && att.getYbeg() > att.getYend()) {
					att.setYbeg(att.getYbeg() - 1);
					this.gameEventSend.add(new Event(null,att));
				}
				else if (att.getDirection() == 4 && (att.getYbeg() > att.getYend() && att.getXbeg() < att.getXend())) {
					att.setYbeg(att.getYbeg() - 1);
					att.setXbeg(att.getXbeg() + 1);
					this.gameEventSend.add(new Event(null,att));
					}
				
				else if (att.getDirection() == 5 && (att.getYbeg() > att.getYend() && att.getXbeg() > att.getXend())) {

					att.setYbeg(att.getYbeg() - 1);
					att.setXbeg(att.getXbeg() - 1);
					this.gameEventSend.add(new Event(null,att));

				}
				else if (att.getDirection() == 6 && (att.getYbeg() < att.getYend() &&  att.getXbeg() < att.getXend())) {
					att.setYbeg(att.getYbeg() + 1);
					att.setXbeg(att.getXbeg() + 1);
					this.gameEventSend.add(new Event(null,att));

				}
				else if (att.getDirection() == 7 && (att.getYbeg() < att.getYend() && att.getXbeg() > att.getXend())) {
					att.setYbeg(att.getYbeg() + 1);
					att.setXbeg(att.getXbeg() - 1);
					this.gameEventSend.add(new Event(null,att));
				}
		else {
					removeSpell rs = new removeSpell();
					rs.id = att.getId();
					this.gameEventSend.add(new Event(null, rs));
					spell.remove(att);	
				}
			}
		}
	}
	
	
	/**
	 * This method will update all the players movement and will check all the things that changed on the window
	 * @param lastupdate
	 */
	private void update(double lastupdate){
		for (int i = 0; i < this.playerl.size(); i++) {
			this.playerl.get(i).sl.setA(this.playerl.get(i).sl.getA() -(int)((System.nanoTime() - lastupdate)/1000000) );
			this.playerl.get(i).sl.setE(this.playerl.get(i).sl.getE() -(int)((System.nanoTime() - lastupdate)/1000000) );
			this.playerl.get(i).sl.setR(this.playerl.get(i).sl.getR() -(int)((System.nanoTime() - lastupdate)/1000000) );
		}
		this.spellUpdate();
		for (Player p : playerl) {
			p.update(null, (int)((System.nanoTime() - lastupdate)/1000000));
		}
		for (Player p : playerl){
			updatep pup = new updatep();
			for (int i = 0; i < p.effectL.size(); i++) {
				if (p.effectL.get(i).getTime() > 0) {
					p.effectL.get(i).setTime(p.effectL.get(i).getTime() - (int)((System.nanoTime() - lastupdate)/1000000));
					this.gameEventSend.add(new Event(null, p.effectL.get(i)));
				}
				else {
					p.effectL.remove(i);
					p.reput();
				}
			}
			pup.direction = p.getDirection();
			pup.name = p.getName();
			pup.move = p.isMoving();
			pup.direction = p.getDirection();
			pup.x = (int)p.getX();
			pup.y = (int)p.getY();
			for (Player player : playerl) {
				checkHit(player);
				pup.life = p.getLife();
				this.gameEventSend.add(new Event(player.c, pup));
			}
		}
	}
	
	/**
	 * Method that will check if the player was hit by one a the spells. If the player get hit,
	 *  the spell will be remove and the player will lose life and the effect of the spell will apply to the player
	 * @param player
	 * @return
	 */
	private boolean checkHit(Player player) {
		if (spell.size() != 0) {
			for (int i = 0 ; i < spell.size(); i++) {
				Attack att = spell.get(i); 
				if ((att.getTeam() != player.getTeam())) {
				if ((att.getXbeg() >= player.getX() - 20 && att.getXbeg() <= player.getX()+20) && ((att.getYbeg() >= player.getY() - 20 && att.getYbeg() <= player.getY()+20)))
				{
					if (att.getEffect().equals("stun")) {
						Effect eff = new Effect(att.getEffect(),5000);
						eff.setId(idEff);
						idEff++;
						player.addEffect(eff);
						player.effChange(eff);
						this.gameEventSend.add(new Event(null,eff));
					}
					else if (att.getEffect().equals("frost")) {
						Effect eff = new Effect(att.getEffect(),5000);
						eff.setId(idEff);
						idEff++;
						player.effChange(eff);
						player.effectL.add(eff);
						eff.setNameP(player.getName());
						this.gameEventSend.add(new Event(null,eff));
					}
					else if (att.getEffect().equals("root")) {
						Effect eff = new Effect(att.getEffect(),5000);
						eff.setId(idEff);
						idEff++;
						player.effChange(eff);
						player.effectL.add(eff);
						eff.setNameP(player.getName());
						this.gameEventSend.add(new Event(null,eff));
					}
					player.setLife(player.getLife() - att.getDamage());
					spell.remove(att);
					System.out.println(player.getName() + "perd de la vie" + player.getLife());
					if (player.getLife() <= 0)
					{
						removePlayer rp = new removePlayer();
						rp.name = player.name;
						this.gameEventSend.add(new Event(null,player));
						this.playerl.remove(player);
					}
					removeSpell rs = new removeSpell();
					rs.id = att.getId();
					this.gameEventSend.add(new Event(null, rs));
					spell.remove(att);
					return true;
				}
			}
		}
		}
		return false;
	}
	
	/**
	 * This method manage all the object that the client sent to him and will treat them.
	 */
	private void treatEvent(){

		while (!gameEventReceive.isEmpty()) {
			Event e = gameEventReceive.poll();
				if (e.object instanceof addPlayer){
					addPlayer ptoadd = (addPlayer)e.object;
					Player p = new Player();
					p.c = e.c;
					p.setType(ptoadd.type);
					p.setName(ptoadd.name);
					p.setTeam(team);
					team++;
					p.setX(ptoadd.x);
					p.setY(ptoadd.y);
					p.setMoving(false);
					p.setDirection(0);
					for (Player pla : playerl) {
						addPlayer add = new addPlayer();
						add.name = pla.getName();
						add.x = (int)pla.getX();
						add.y = (int)pla.getY();
						this.gameEventSend.add(new Event(p.c,add));
					}
					this.playerl.add(p);
					addPlayer pto = new addPlayer();
					pto.name = p.getName();
					pto.team = p.getTeam();
					pto.type = p.getType();
					pto.x = (int)p.getX();
					pto.y = (int)p.getY();
					Event event = new Event(null, pto);
					this.gameEventSend.add(event);
				}	
			else if (e.object instanceof movePlayer){
				movePlayer ptomove = (movePlayer)e.object;
				for (Player player : this.playerl) {
					if (player.name.equals(ptomove.name)){
						player.setDirection(ptomove.direction);
						player.setMoving(ptomove.move);
						break;
					}	
				}
			}
			else if (e.object instanceof attackPlayer) {
				attackPlayer attack = (attackPlayer) e.object;
				if (attack.type.equals("wizard")) {
					if (((attackPlayer) e.object).pushed == Input.KEY_A) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getA() <= 0) {
									Attack att = database.createAtt("fireBall",idSpell,this.playerl.get(i).getTeam());
									idSpell++;
									if (((attackPlayer) e.object).direction == 0) {
										att.setXbeg(((attackPlayer) e.object).x  - 10);
										att.setYbeg(((attackPlayer) e .object).y - 100);
										att.setYend(att.getYbeg() - 120);
									}
									else if (((attackPlayer) e.object).direction == 1) {
										att.setXbeg(((attackPlayer) e.object).x - 70 );
										att.setYbeg(((attackPlayer) e .object).y - 30);
										att.setXend(att.getXbeg() - 100);
									}
									else if (((attackPlayer) e.object).direction == 2) {
										att.setXbeg(((attackPlayer) e.object).x - 10 );
										att.setYbeg(((attackPlayer) e .object).y - 23);
										att.setYend(att.getYbeg() + 100);
									}
									else if (((attackPlayer) e.object).direction == 3) {	
										att.setXbeg(((attackPlayer) e.object).x + 20 );
										att.setYbeg(((attackPlayer) e .object).y - 30);
										att.setXend(att.getXbeg() + 100);
									}
									att.setDirection(((attackPlayer) e.object).direction);
									this.spell.add(att);
									this.playerl.get(i).sl.setA(5000);
								}
							}
						}
					}
					else if (((attackPlayer) e.object).pushed == Input.KEY_E) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getE() <= 0) {
									Attack att = database.createAtt("shadowBall", idSpell,this.playerl.get(i).getTeam());
									idSpell++;
									if (((attackPlayer) e.object).direction == 0) {
										att.setXbeg(((attackPlayer) e.object).x  - 10);
										att.setYbeg(((attackPlayer) e .object).y - 100);
										att.setYend(att.getYbeg() - 120);
									}
									else if (((attackPlayer) e.object).direction == 1) {
										att.setXbeg(((attackPlayer) e.object).x - 70 );
										att.setYbeg(((attackPlayer) e .object).y - 30);
										att.setXend(att.getXbeg() - 100);
									}
									else if (((attackPlayer) e.object).direction == 2) {
										att.setXbeg(((attackPlayer) e.object).x - 10 );
										att.setYbeg(((attackPlayer) e .object).y - 23);
										att.setYend(att.getYbeg() + 100);
									}
									else if (((attackPlayer) e.object).direction == 3) {	
										att.setXbeg(((attackPlayer) e.object).x + 20 );
										att.setYbeg(((attackPlayer) e .object).y - 30);
										att.setXend(att.getXbeg() + 100);
									}
									att.setDirection(((attackPlayer) e.object).direction);
									this.spell.add(att);
									this.playerl.get(i).sl.setE(5000);
								}
							}
						}
					}
					else if (((attackPlayer) e.object).pushed == Input.KEY_R) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getR() <= 0) {
								Attack att = database.createAtt("lightBall", idSpell,this.playerl.get(i).getTeam());
								idSpell++;
								if (((attackPlayer) e.object).direction == 0) {
									att.setXbeg(((attackPlayer) e.object).x  - 10);
									att.setYbeg(((attackPlayer) e .object).y - 100);
									att.setYend(att.getYbeg() - 120);
								}
								else if (((attackPlayer) e.object).direction == 1) {
									att.setXbeg(((attackPlayer) e.object).x - 70 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() - 100);
								}
								else if (((attackPlayer) e.object).direction == 2) {
									att.setXbeg(((attackPlayer) e.object).x - 10 );
									att.setYbeg(((attackPlayer) e .object).y - 23);
									att.setYend(att.getYbeg() + 100);
								}
								else if (((attackPlayer) e.object).direction == 3) {	
									att.setXbeg(((attackPlayer) e.object).x + 20 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() + 100);
								}
								att.setDirection(((attackPlayer) e.object).direction);
								this.spell.add(att);
								this.playerl.get(i).sl.setR(5000);
								}
							}
						}
					}}
				else {
					if (((attackPlayer) e.object).pushed == Input.KEY_A) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getA() <= 0) {

								Attack att = database.createAtt("frostArrow",idSpell,this.playerl.get(i).getTeam());
								idSpell++;
								if (((attackPlayer) e.object).direction == 0) {
									att.setXbeg(((attackPlayer) e.object).x  - 10);
									att.setYbeg(((attackPlayer) e .object).y - 100);
									att.setYend(att.getYbeg() - 120);
								}
								else if (((attackPlayer) e.object).direction == 1) {
									att.setXbeg(((attackPlayer) e.object).x - 70 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() - 100);
								}
								else if (((attackPlayer) e.object).direction == 2) {
									att.setXbeg(((attackPlayer) e.object).x - 10 );
									att.setYbeg(((attackPlayer) e .object).y - 23);
									att.setYend(att.getYbeg() + 100);
								}
								else if (((attackPlayer) e.object).direction == 3) {	
									att.setXbeg(((attackPlayer) e.object).x + 20 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() + 100);
								}
								att.setDirection(((attackPlayer) e.object).direction);
								this.spell.add(att);
								this.playerl.get(i).sl.setA(5000);
								}
							}
						}
					}
					else if (((attackPlayer) e.object).pushed == Input.KEY_E) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getE() <= 0) {

								Attack att = database.createAtt("pinkArrow", idSpell,this.playerl.get(i).getTeam());
								idSpell++;
								if (((attackPlayer) e.object).direction == 0) {
									att.setXbeg(((attackPlayer) e.object).x  - 10);
									att.setYbeg(((attackPlayer) e .object).y - 100);
									att.setYend(att.getYbeg() - 120);
								}
								else if (((attackPlayer) e.object).direction == 1) {
									att.setXbeg(((attackPlayer) e.object).x - 70 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() - 100);
								}
								else if (((attackPlayer) e.object).direction == 2) {
									att.setXbeg(((attackPlayer) e.object).x - 10 );
									att.setYbeg(((attackPlayer) e .object).y - 23);
									att.setYend(att.getYbeg() + 100);
								}
								else if (((attackPlayer) e.object).direction == 3) {	
									att.setXbeg(((attackPlayer) e.object).x + 20 );
									att.setYbeg(((attackPlayer) e .object).y - 30);
									att.setXend(att.getXbeg() + 100);
								}
								att.setDirection(((attackPlayer) e.object).direction);
								this.spell.add(att);
								this.playerl.get(i).sl.setE(5000);
							}
						}
					}
					}
					else if (((attackPlayer) e.object).pushed == Input.KEY_R) {
						for (int i = 0; i < this.playerl.size(); i++)
						{
							if (attack.name.equals(this.playerl.get(i).getName()))
							{
								if (this.playerl.get(i).getSl().getR() <= 0) {

								this.circleArrow((attackPlayer) e.object, this.playerl.get(i).getTeam());
								this.playerl.get(i).sl.setR(5000);
								}
							}
						}
					}
				}
			}
		}
	}
	private void circleArrow(attackPlayer attacker, int team) {
		Attack att = database.createAtt("circleArrow", idSpell, team);
		idSpell++;
		att.setXbeg(attacker.x - 13);
		att.setYbeg(attacker.y - 150);
		att.setYend(att.getYbeg() - 200);
		att.setDirection(0);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x-100);
		att.setYbeg(attacker.y - 23);
		att.setXend(att.getXbeg() - 200);
		att.setDirection(1);
		this.spell.add(att);

		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x - 13);
		att.setYbeg(attacker.y + 10);
		att.setYend(att.getYbeg() + 200);
		att.setDirection(2);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x + 17 );
		att.setYbeg(attacker.y - 23);
		att.setXend(att.getXbeg() + 200);
		att.setDirection(3);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x + 15 );
		att.setYbeg(attacker.y - 105);
		att.setXend(att.getXbeg() + 200);
		att.setYend(att.getYbeg() - 200);
		att.setDirection(4);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x - 80 );
		att.setYbeg(attacker.y - 105);
		att.setXend(att.getXbeg() - 200);
		att.setYend(att.getYbeg() - 200);
		att.setDirection(5);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x + 10 );
		att.setYbeg(attacker.y - 23);
		att.setXend(att.getXbeg() + 200);
		att.setYend(att.getYbeg() + 200);
		att.setDirection(6);
		this.spell.add(att);
		
		att = database.createAtt("circleArrow", idSpell,team);
		idSpell++;
		att.setXbeg(attacker.x - 80 );
		att.setYbeg(attacker.y - 23);
		att.setXend(att.getXbeg() - 200);
		att.setYend(att.getYbeg() + 200);
		att.setDirection(7);
		this.spell.add(att);
		
	}
	/**
	 * set all the event to send at the list shared
	 */
	private void setEventSend(){
		while (!gameEventSend.isEmpty()) {
			this.ElistSend.add(gameEventSend.poll());
		}
	}
	
	/**
	 * get all the event receiv by the server in shared list
	 */
	private void getEventReceive(){
		while (!ElistReceive.isEmpty()) {
			this.gameEventReceive.add(ElistReceive.poll());
		}
	}
	
	public static void main(String[] args) {
		ServerGame game = new ServerGame();
		new Thread(game).start();
	}
}
