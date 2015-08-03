package com.football;

import java.util.ArrayList;
import java.util.List;

public class Game
{
	int id;
	String week;
	String hometeam;
	String awayteam;
	double homeodds;
	String homeresult;
	
	public Game()
	{
		//blank
	}

	public Game(int idin, String weekin, String hometeamin, String awayteamin, double homeoddsin, String resultin)
	{
		super();
		
		this.id = idin;
		this.week = weekin;
		this.hometeam = hometeamin;
		this.awayteam = awayteamin;
		this.homeodds = homeoddsin;
		this.homeresult = resultin;
				
	}
	
	public void setWeek(String weekin)
	{
		this.week = weekin;
	}
	
	public void sethomeodds(double homeoddsin)
	{
		this.homeodds = homeoddsin;
	}
	
	public void setid(int idin)
	{
		this.id = idin;
	}
	
	public String getWeek()
	{
		return this.week;
	}
	
	public String getHomeTeam()
	{
		return this.hometeam;
	}
	
	public String getAwayTeam()
	{
		return this.awayteam;
	}
	
	public double getOdds()
	{
		return this.homeodds;
	}
	
	public String getResult()
	{
		return this.homeresult;
	}

}