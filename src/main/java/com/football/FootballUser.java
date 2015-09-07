package com.football;

import java.util.ArrayList;
import java.util.List;

public class FootballUser
{
	String fname;
	String handle;
	String email;
	int betting_closed;
	int wins;
	int losses;
	int pushes;
	String gains;

	public FootballUser()
	{
		//blank
	}

	public FootballUser(String fnamein, String handlein, String emailin)
	{
		super();
		
		System.out.println("test the log");
				
		this.fname = fnamein;
		this.handle = handlein;
		this.email = emailin;
	}
	
	public FootballUser(String fnamein, String handlein, String emailin, int betting_closedin)
	{
		super();
		
		System.out.println("test the log");
				
		this.fname = fnamein;
		this.handle = handlein;
		this.email = emailin;
		this.betting_closed = betting_closedin;
	}

	public String getHandle()
	{
		return this.handle;
	}

	public String getName()
	{
		return this.fname;
	}
	
	public String getEmail()
	{
		return this.email;
	}
	
	public int getBetting_closed()
	{
		return this.betting_closed;
	}
	
	public int getWins()
	{
		return this.wins;
	}
	
	public int getLosses()
	{
		return this.losses;
	}
	
	public int getPushes()
	{
		return this.pushes;
	}
	
	public String getGains()
	{
		return this.gains;
	}
	
	public void setGains(String gainsin)
	{
		this.gains = gainsin;
	}
	
	public void setRecord(int winsin, int lossesin, int pushesin)
	{
		this.wins = winsin;
		this.losses = lossesin;
		this.pushes = pushesin;
	}
}