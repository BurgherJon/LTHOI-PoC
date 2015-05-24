package com.football;

import java.util.ArrayList;
import java.util.List;

public class League
{
	Integer id;
	String name;

	public League()
	{
		//blank
	}

	public League(Integer id)
	{
		super();
		this.id = id;
	}

	public League(Integer id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}