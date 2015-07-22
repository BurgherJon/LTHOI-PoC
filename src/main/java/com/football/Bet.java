package com.football;

import java.util.ArrayList;
import java.util.List;

public class Bet
{
	String week;
	String pickteam;
	String oppteam;
	long odds;
	String result;
	
	public Bet()
	{
		//blank
	}

	public Bet(String weekin, String pickteamin, String oppteamin, long oddsin, String resultin)
	{
		super();
		
		this.week = weekin;
		this.pickteam = pickteamin;
		this.oppteam = oppteamin;
		this.odds = oddsin;
		this.result = resultin;
				
	}
	
	public String getWeek()
	{
		return this.week;
	}
	
	public String getPickTeam()
	{
		return this.pickteam;
	}
	
	public String getOppTeam()
	{
		return this.oppteam;
	}
	
	public long getOdds()
	{
		return this.odds;
	}
	
	public String getResult()
	{
		return this.result;
	}
	
	//This constructor is designed to take the results of the query and convert them in to the expected data.
	//If a game has yet to be played (in the db the result is null), please pass -1132.
	public Bet(String week, int result, int picked_home, String home_team, String away_team, long home_line)
	{
		super();
		
		this.week = week;
		if (picked_home == 1)
		{
			this.pickteam = home_team;
			this.oppteam = away_team;
			this.odds = home_line;
		}
		else
		{
			this.pickteam = away_team;
			this.oppteam = home_team;
			this.odds = -1 * home_line;
		}
		if (result == 1)
		{
			this.result = "Win";
		}
		else if (result == -1132)
		{
			this.result = "Good Luck!";
		}
		else if (result == 0)
		{
			this.result = "Push";
		}
		else if (result == -1)
		{
			this.result = "Loss";
		}
	}
	

}