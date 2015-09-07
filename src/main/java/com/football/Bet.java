package com.football;

import java.util.ArrayList;
import java.util.List;

public class Bet
{
	String week;
	String pickteam;
	String oppteam;
	double odds;
	String result;
	
	//The againstyou field and the net_bet are extra for house bets and shows you who picked the other side of the bet you were forced to take and what your net bet is.
	String againstyou;
	double netbet;
	
	
	public Bet()
	{
		//blank
	}

	public Bet(String weekin, String pickteamin, String oppteamin, double oddsin, String resultin, String againstyouin, double netbetin)
	{
		super();
		
		this.week = weekin;
		this.pickteam = pickteamin;
		this.oppteam = oppteamin;
		this.odds = oddsin;
		this.result = resultin;
		this.againstyou = againstyouin;
		this.netbet = netbetin;
				
	}
	
	public double getNetbet()
	{
		return this.netbet;
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
	
	public double getOdds()
	{
		return this.odds;
	}
	
	public String getResult()
	{
		return this.result;
	}
	
	public String getAgainstyou()
	{
		return this.againstyou;
	}
	
	//This constructor is designed to take the results of the query that loads regular bets and convert them in to the expected data.
	//If a game has yet to be played (in the db the result is null), please pass -1132.
	public Bet(String week, int result, int picked_home, String home_team, String away_team, double home_line, double bet_size)
	{
		super();
		
		this.week = week;
		if (picked_home == 1)
		{
			this.pickteam = home_team;
			this.oppteam = away_team;
			this.odds = home_line;
			
			if (result == -1132)
			{
				this.result = "Good Luck!";
			}
			else if (result < home_line)
			{
				this.result = "Win";
			}
			else if (result == home_line)
			{
				this.result = "Push";
			}
			else if (result > home_line)
			{
				this.result = "Loss";
			}
			else
			{
				this.result = "No Result" + result;
			}
		}
		else
		{
			this.pickteam = away_team;
			this.oppteam = home_team;
			this.odds = -1 * home_line;
			
			if (result == -1132)
			{
				this.result = "Good Luck!";
			}
			else if (result < home_line)
			{
				this.result = "Loss";
			}
			else if (result == home_line)
			{
				this.result = "Push";
			}
			else if (result > home_line)
			{
				this.result = "Win";
			}
			else
			{
				this.result = "No Result" + result;
			}
		}
		
		
		this.againstyou = "Your Bet!";
		this.netbet = ((double)bet_size);
	}
	
	//This constructor is designed to take the results of the query that loads house bets and convert them in to the expected data.
	//If a game has yet to be played (in the db the result is null), please pass -1132.
	public Bet(String week, int result, int picked_home, String home_team, String away_team, double home_line, String against, double netbet)
	{
		super();
		
		if (picked_home == 1)
		{
			this.pickteam = home_team;
			this.oppteam = away_team;
			this.odds = home_line;

			if (result == -1132)
			{
				this.result = "Good Luck!";
			}
			else if (result < home_line)
			{
				this.result = "Win";
			}
			else if (result == home_line)
			{
				this.result = "Push";
			}
			else if (result > home_line)
			{
				this.result = "Loss";
			}
			else
			{
				this.result = "No Result" + result;
			}
		}
		else
		{
			this.pickteam = away_team;
			this.oppteam = home_team;
			this.odds = -1 * home_line;
			
			if (result == -1132)
			{
				this.result = "Good Luck!";
			}
			else if (result < home_line)
			{
				this.result = "Loss";
			}
			else if (result == home_line)
			{
				this.result = "Push";
			}
			else if (result > home_line)
			{
				this.result = "Win";
			}
			else
			{
				this.result = "No Result" + result;
			}
		}
			
		this.week = week;
		this.againstyou = against;
		this.netbet = netbet;
	}
	

}