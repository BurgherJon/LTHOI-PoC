package com.football;

/**
  * Add your first API methods in this class, or you may create another class. In that case, please
  * update your web.xml accordingly.
 **/

import com.football.FootballUser;
import com.football.League;
import com.football.Bet;
import com.football.Game;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.utils.SystemProperty;
import com.google.appengine.api.users.User;
import com.google.api.server.spi.response.UnauthorizedException;

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

	
	@ApiMethod(name="getUser", scopes = {Constants.EMAIL_SCOPE}, clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID})
	public List<FootballUser> getUser (User guser) throws UnauthorizedException 
	{
		List<FootballUser> users = new ArrayList<FootballUser>();
		FootballUser user = new FootballUser(null, null, null);
						
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
			user = new FootballUser("", "ERROR: Failed Forming Connection String", "");
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT * FROM bedb1.Users WHERE email = '" + guser.getEmail() + "';";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			if (rs.next()) //Anything in the result set?
			{
				if (!rs.isLast())  //A second record in the result set?
				{
					user = new FootballUser("Error", "Error: Multiple Users With This Email Address!", null);
				}
				else 
				{
					user = new FootballUser(rs.getString("fname"), rs.getString("handle"), rs.getString("email"));
				}
			}
			else // Nothing was in the result set.
			{
				user = new FootballUser("Error", "Error: You're Not A Registered User", null);
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			user = new FootballUser("Error", ("Error: " + e.getMessage()), null);
		}
		
		users.add(user);
		return users;
	}
	
	
	//Get a particular users bet history.
	@ApiMethod(name="getCSHistory", scopes = {Constants.EMAIL_SCOPE}, clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID})
	public List<Bet> getCSHistory (User guser) throws UnauthorizedException
	{
		List<Bet> bets = new ArrayList<Bet>();
		Bet bet = new Bet();
						
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
			bet = new Bet("Error",e.getMessage(),"",0,"");
			bets.add(bet);
			return bets;
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "Select Weeks.name_long AS game_week, Bets.result AS result, Bets.home AS picked_home, t1.team AS home_team, t2.team AS away_team, Games.home_line AS home_line FROM bedb1.Bets As Bets LEFT JOIN bedb1.Games As Games ON Bets.game_id = Games.id LEFT JOIN bedb1.Teams AS t1 ON Games.home = t1.id LEFT JOIN bedb1.Teams AS t2 ON Games.away = t2.id LEFT JOIN bedb1.Weeks AS Weeks ON Games.week_id = Weeks.id WHERE Bets.email = '" + guser.getEmail() + "';";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			int betcount = 0;
			int res;
			while (rs.next())
			{
				//Since Java autocorrects a null result (i.e. the game hasn't been played yet) to a 0 it can't be distinguished from a push.  This code sets it to the value that the constructor expects for games that haven't been played (-1132).
				res = rs.getInt("result");
				if (rs.wasNull())
				{
					res = -1132;
				}
				
				
				bet = new Bet(rs.getString("game_week"),res,rs.getInt("picked_home"),rs.getString("home_team"),rs.getString("away_team"),rs.getLong("home_line"));
				
				bets.add(bet);
				
				betcount++;
			}
			
			if (betcount == 0)
			{
				bet = new Bet("No Bets","No Bets","",0,"");
				bets.add(bet);
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			bet = new Bet("No Bets","No Bets","",0,"");
			bets.add(bet);
			return bets;
		}
		
		
		return bets;
	}
	
	@ApiMethod(name="getThisWeeksGames")
	public List<Game> getThisWeeksGames ()
	{
		List<Game> games = new ArrayList<Game>();
		Game game = new Game();
						
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
			game = new Game("Error",e.getMessage(),"",0,"");
			games.add(game);
			return games;
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT w.name_long AS theweek, th.team AS hometeam, ta.team AS awayteam, g.home_line AS line FROM bedb1.SysInfo AS s INNER JOIN bedb1.Weeks AS w ON w.id = s.current_week INNER JOIN bedb1.Games AS g ON w.id = g.week_id INNER JOIN bedb1.Teams AS th ON g.home = th.id INNER JOIN bedb1.Teams AS ta ON g.away = ta.id;";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			int gamecount = 0;
			while (rs.next())
			{
				game = new Game(rs.getString("theweek"),rs.getString("hometeam"),rs.getString("awayteam"),rs.getLong("line"),"");
				
				games.add(game);
				
				gamecount++;
			}
			
			if (gamecount == 0)
			{
				game = new Game("No Games","No Games","",0,"");
				games.add(game);
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			game = new Game("SQL Exception:", e.getMessage(),"",0,"");
			games.add(game);
			return games;
		}
		
		
		return games;
	}
	
	//User makes a bet for this week.
	@ApiMethod(name="makebet", scopes = {Constants.EMAIL_SCOPE}, clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID})
	public List<Bet> makebet(@Named("team") String teamin, User guser) throws UnauthorizedException
	{
		List<Bet> bets = new ArrayList<Bet>();
		Bet bet = new Bet();
						
		String strquery=null;
		String strurl=null;
		String betteam = "";
		
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
			bet = new Bet("Error creating database connection.", "", "", 0, "");
			bets.add(bet);
			return bets;
		} 
		
		//First, check to see if the user either has a bet on the game already.  
		//If they already have a bet on the team that they asked to bet on, simply inform them that they already have the bet.
		//If they have an existing bet on the other team, then delete that bet.
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT b.id AS betnum, th.team AS hometeam, ta.team AS awayteam, b.home AS homebetbool FROM bedb1.Bets as b INNER JOIN bedb1.Games as g ON g.id = b.game_id INNER JOIN bedb1.SysInfo as s ON s.current_week = g.week_id INNER JOIN bedb1.Teams AS th ON g.home = th.id INNER JOIN bedb1.Teams AS ta ON g.away = ta.id WHERE b.email = '" + guser.getEmail() + "' AND (th.team = '" + teamin + "' OR ta.team = '" + teamin + "');";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			while (rs.next())
			{
				if (rs.getInt("homebetbool") == 1)
				{
					betteam = rs.getString("hometeam");
				}
				else
				{
					betteam = rs.getString("awayteam");
				}
				
				if (betteam.equals(teamin))
				{
					bet = new Bet(("You already had a bet on the " + teamin + "!"), "", "", 0, "");
					bets.add(bet);
					conn.close();
					return bets;
				}
				else
				{
					int id_to_delete = rs.getInt("betnum");
					strquery = "DELETE FROM bedb1.Bets WHERE id = " + id_to_delete + ";";
					conn.createStatement().executeUpdate(strquery);
				}
				
			}
		} 
		catch (SQLException e) 
		{
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "");
			bets.add(bet);
			return bets;
		}
		
		
		//Next query the users bets for the current week.  If they already have 3 bets then cancel the oldest one to make room for this one.
		conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT b.id AS betnum FROM bedb1.Bets as b INNER JOIN bedb1.Games as g ON g.id = b.game_id INNER JOIN bedb1.SysInfo as s ON s.current_week = g.week_id INNER JOIN bedb1.Teams AS th ON g.home = th.id INNER JOIN bedb1.Teams AS ta ON g.away = ta.id WHERE b.email = '" + guser.getEmail()+ "' ORDER BY b.id;";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			int betcount = 0;
			int firstbet = 0;
			while (rs.next())
			{
				betcount++;
				
				if (betcount == 1)
				{
					firstbet = rs.getInt("betnum");
				}
				
			}
			if (betcount > 2)
			{
				strquery = "DELETE FROM bedb1.Bets WHERE id = " + firstbet + ";";
				conn.createStatement().executeUpdate(strquery);
			}
		} 
		catch (SQLException e) 
		{
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "");
			bets.add(bet);
			return bets;
		}
		
		
		//Finally, if we're still here.  Enter the bet.
		conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "INSERT INTO bedb1.Bets(email, league_season_id, game_id, home) VALUES ('" + guser.getEmail() + "', 1, (SELECT g.id AS game_id FROM bedb1.Games AS g INNER JOIN bedb1.SysInfo AS s ON g.week_id = s.current_week INNER JOIN bedb1.Teams AS th ON th.id = g.home INNER JOIN bedb1.Teams AS ta ON ta.id = g.away WHERE th.team = '" + teamin + "' OR ta.team = '" + teamin + "'), (SELECT IF (th.team = '" + teamin + "', 1, 0) AS home FROM bedb1.Games AS g INNER JOIN bedb1.SysInfo AS s ON g.week_id = s.current_week INNER JOIN bedb1.Teams AS th ON th.id = g.home INNER JOIN bedb1.Teams AS ta ON ta.id = g.away WHERE th.team = '" + teamin + "' OR ta.team = '" + teamin + "'));";
			conn.createStatement().executeUpdate(strquery);
			
			bet = new Bet(("Successfully added bet on " + teamin + "."), strquery, "", 0, "");
			bets.add(bet);
			conn.close();
		} 
		catch (SQLException e) 
		{
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "");
			bets.add(bet);
			return bets;
		}
				
		return bets;
	}
	
	//This Method processes bets for the week.
	//If I can figure out Cron Jobs it will run every hour (checking to see if we're close enough to the deadline), for now, for testing purposes, it runs on button push.
	//It will add the community bets for each player.
	@ApiMethod(name="preGameProcess")
	public League preGameProcess(@Named("team") String teamin)
	{
		League returner = new League(0, "Didn't Process");
						
		//The outer query checks to see if the current week has been preprocessed already (a sysinfo column).
		//WHEN THIS FUNCTIONS AS A CRON JOB: The next inner query, should check if we are close enough to the first game to preprocess.
		//The inner query will populate the house_bet table.
		
		/*
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
			game = new Game("Error",e.getMessage(),"",0,"");
			games.add(game);
			return games;
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "SELECT w.name_long AS theweek, th.team AS hometeam, ta.team AS awayteam, g.home_line AS line FROM bedb1.SysInfo AS s INNER JOIN bedb1.Weeks AS w ON w.id = s.current_week INNER JOIN bedb1.Games AS g ON w.id = g.week_id INNER JOIN bedb1.Teams AS th ON g.home = th.id INNER JOIN bedb1.Teams AS ta ON g.away = ta.id;";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			int gamecount = 0;
			while (rs.next())
			{
				game = new Game(rs.getString("theweek"),rs.getString("hometeam"),rs.getString("awayteam"),rs.getLong("line"),"");
				
				games.add(game);
				
				gamecount++;
			}
			
			if (gamecount == 0)
			{
				game = new Game("No Games","No Games","",0,"");
				games.add(game);
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			game = new Game("SQL Exception:", e.getMessage(),"",0,"");
			games.add(game);
			return games;
		}
		*/
		
		return returner;
	}
	
	
	/*
	//Display a user's bets for this week
	@ApiMethod(name="makebet", scopes = {Constants.EMAIL_SCOPE}, clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID})
	public List<Bet> makebet(User guser) throws UnauthorizedException
	{
				
	}
	*/
	
	/*********************************************************************************************/
	/***** These Are The Old League Admin API Methods That I Used to Test The Initial Setup ******/
	/*********************************************************************************************/
	
	@ApiMethod(name="getNewMethodTest")
	public List<User> getNewMethodTest ()
	{
		List<User> users = new ArrayList<User>();
		User user = new User("", "", "");
						
		String strquery=null;
		String strurl=null;
	
		user = new User("test", "test", "test");
	
		users.add(user);
		return users;
	}
	
	
	
	
	@ApiMethod(name="list")
	public List<League> getLeagues ()
	{
		List<League> leagues = new ArrayList<League>();
		leagues.add(new League(0, "Working5"));
		
		
		
		FootballUser test = new FootballUser("test", "test", "test");
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