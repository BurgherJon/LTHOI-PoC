package com.football;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

import com.football.League;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.utils.SystemProperty;

import javax.inject.Named;





//DB Enabling Libraries
import java.io.*;
import java.sql.*;

import com.google.appengine.api.utils.SystemProperty;

@Api(name="leagueadmin",version="v1", description="An API to manage the leagues")

public class YourFirstAPI
{

	public static List<League> leagues = new ArrayList<League>();

	@ApiMethod(name="list")
	public List<League> getLeagues ()
	{
		return leagues;
	}
	


	@ApiMethod(name="getLeague")
	public League getLeague(@Named("id") Integer id) {
		String strurl=null;
		String strquery = "SELECT * from bedb1.Leagues WHERE id = " + id + ";";
		League returner = new League (0, "Nothing Found with that ID");
		
		try 
		{
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) 
			{
			    // Load the class that provides the new "jdbc:google:mysql://" prefix.
			    Class.forName("com.mysql.jdbc.GoogleDriver");
			    strurl = "jdbc:google:mysql://focal-acronym-94611:bedb?user=root";
			} 
			else 
			{
			    // Local MySQL instance to use during development.
			    Class.forName("com.mysql.jdbc.Driver");
			    strurl = "jdbc:mysql://127.0.0.1:3306/bedb?user=root";
			}
		} 
		catch (ClassNotFoundException e)
		{
			System.out.println("Failed on setting Class");
			e.printStackTrace();
			return new League(404, e.getMessage());
		}
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			//ResultSet rs = conn.createStatement().executeQuery("Select * From bedb1.Leagues WHERE id = 3;");
			
			//return new League(3, "Hells Yes");
			if (rs.next())
			{
				returner = new League(rs.getInt("id"), rs.getString("name"));
			}
						
			//return new League (100, "It's the query execution.");
			conn.close();
			return returner;
			
		} catch (SQLException e) {
			System.out.println("Fail on query exec");
			return new League(404, e.getMessage());
		}
		
		
	 
	}

	@ApiMethod(name="add")
	public League addLeague(@Named("id") Integer id, @Named("name") String name)
	{
		String strquery=null;
		String strurl=null;
				
		try 
		{
			
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) 
			{
			    // Load the class that provides the new "jdbc:google:mysql://" prefix.
			    Class.forName("com.mysql.jdbc.GoogleDriver");
			    strurl = "jdbc:google:mysql://focal-acronym-94611:bedb?user=root";
			} 
			else {
			    // Local MySQL instance to use during development.
			    Class.forName("com.mysql.jdbc.Driver");
			    strurl = "jdbc:mysql://127.0.0.1:3306/bedb?user=root";
			}
			
			
		} 
		catch (ClassNotFoundException e) {
			System.out.println("Failed on setting Class");
			e.printStackTrace();
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "INSERT INTO bedb1.Leagues (id, name) VALUES("+id+", '"+name+"');";
			conn.createStatement().executeUpdate(strquery);
	        conn.close();
			return new League(0, strquery);
		} 
		catch (SQLException e) 
		{
			System.out.println("Failed to set values");
			return new League(0, e.getMessage());
		}
		
	}

}