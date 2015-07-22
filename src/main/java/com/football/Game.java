package com.football;

import java.util.ArrayList;
import java.util.List;

public class Game
{
	String week;
	String hometeam;
	String awayteam;
	long homeodds;
	String homeresult;
	
	public Game()
	{
		//blank
	}

	public Game(String weekin, String hometeamin, String awayteamin, long homeoddsin, String resultin)
	{
		super();
		
		this.week = weekin;
		this.hometeam = hometeamin;
		this.awayteam = awayteamin;
		this.homeodds = homeoddsin;
		this.homeresult = resultin;
				
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
	
	public long getOdds()
	{
		return this.homeodds;
	}
	
	public String getResult()
	{
		return this.homeresult;
	}

}