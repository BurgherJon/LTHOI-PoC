package com.football;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

import com.football.User;
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

@Api(name="leagueadmin",version="v1",description="An API to manage the leagues")

public class YourFirstAPI
{

	/*********************************************************************************************/
	/********************************** User Based API Methods ***********************************/
	/*********************************************************************************************/

	
	@ApiMethod(name="getUser")
	public List<User> getUser ()
	{
		List<User> users = new ArrayList<User>();
		users.add(new User(1, "Jonathan", "BurgherJon", "test"));
		
		return users;
	}
	
	//The authenticate method takes the users first name and password and returns the user, without password.
	//If the user's password is invalid, a -1 is returned as userid.
	//If the user doesn't exist, a -2 is returned as the userid
	//If there are two users with the same fname a -3 is returned as userid.
	
	//If an error is caught forming the connection string to the database, a -101 is returned as userid and the exceptions is included as fname.
	//If an error is caught executing the query to retrieve the user's password, a -102 is returned as userid and the exception is included as fname.
	@ApiMethod(name="authenticate")
	public List<User> authenticate(@Named("fname") String fname, @Named("pass") String pass)
	{
		List<User> users = new ArrayList<User>();
		User user = new User(0, null, null, null);
						
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
			user = new User(-101, e.getMessage(), null, null);
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT * FROM bedb1.Users WHERE fname = '" + fname + "';";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			if (rs.next()) //Anything in the result set?
			{
				if (!rs.isLast())  //A second record in the result set?
				{
					user = new User(-3, null, null, null);
				}
				if (rs.getString("password").equals(pass)) //A valid password?
				{
					user = new User(rs.getInt("user_id"), rs.getString("fname"), rs.getString("handle"), null);
				}
				else 
				{
					user = new User(-1, null, null, null);
				}
			}
			else // Nothing was in the result set.
			{
				user = new User(-2, null, null, null);
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			user = new User(-102, e.getMessage(), null, null);
		}
		
		users.add(user);
		return users;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*********************************************************************************************/
	/***** These Are The Old League Admin API Methods That I Used to Test The Initial Setup ******/
	/*********************************************************************************************/
	
	
	
	@ApiMethod(name="list")
	public List<League> getLeagues ()
	{
		List<League> leagues = new ArrayList<League>();
		leagues.add(new League(0, "Working"));
		
		
		
		User test = new User(1, "test", "test", "test");
		leagues.add(new League(1, test.getHandle()));
		
		
		
		return leagues;
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
			System.out.println("Failed on setting Class - Remote Modification 2");
			e.printStackTrace();
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "INSERT INTO bedb1.Leagues (id, name) VALUES("+id+", '"+name+"');";
			conn.createStatement().executeUpdate(strquery);
	        conn.close();
			return new League(1000, "Success");
		} 
		catch (SQLException e) 
		{
			System.out.println("Failed to set values");
			return new League(0, e.getMessage());
		}
		
	}

}