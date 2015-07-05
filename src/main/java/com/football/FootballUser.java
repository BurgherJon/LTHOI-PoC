package com.football;

import java.util.ArrayList;
import java.util.List;

public class FootballUser
{
	String fname;
	String handle;
	String email;

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
}