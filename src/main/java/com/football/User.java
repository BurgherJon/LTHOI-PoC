package com.football;

import java.util.ArrayList;
import java.util.List;

public class User
{
	Integer id;
	String fname;
	String handle;
	String password;

	public User()
	{
		//blank
	}

	public User(Integer idin, String fnamein, String handlein, String passwordin)
	{
		super();
		
		System.out.println("test the log");
				
		this.id = idin;
		this.fname = fnamein;
		this.handle = handlein;
		this.password = passwordin;
	}

	public String getHandle()
	{
		return this.handle;
	}

	public String getName()
	{
		return this.fname;
	}
	
	public Integer getID()
	{
		return this.id;
	}
	
	public boolean authenticate(String passwordin)
	{
		return (this.password.equals(passwordin));
	}
}