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

	//Returns the basic information necessary to setup the UI.
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
					strquery = "Select preGameProcess From bedb1.SysInfo;";
					ResultSet check_process_rs = conn.createStatement().executeQuery(strquery);
					check_process_rs.next();
					if (check_process_rs.getBoolean("preGameProcess"))
					{
						user = new FootballUser(rs.getString("fname"), rs.getString("handle"), rs.getString("email"), 1);
					}
					else
					{
						user = new FootballUser(rs.getString("fname"), rs.getString("handle"), rs.getString("email"), 0);
					}
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
	
	//Get all of the games for this week.
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
			bet = new Bet("Error creating database connection.", "", "", 0, "","");
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
					bet = new Bet(("You already had a bet on the " + teamin + "!"), "", "", 0, "","");
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
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "","");
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
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "","");
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
			
			bet = new Bet(("Successfully added bet on " + teamin + "."), strquery, "", 0, "","");
			bets.add(bet);
			conn.close();
		} 
		catch (SQLException e) 
		{
			bet = new Bet(("SQL Error:" + e.getMessage()), "", "", 0, "","");
			bets.add(bet);
			return bets;
		}
				
		return bets;
	}
	
	//This Method processes bets for the week.
	//If I can figure out Cron Jobs it will run every hour (checking to see if we're close enough to the deadline), for now, for testing purposes, it runs on button push.
	//It will add the community bets to the pool.
	@ApiMethod(name="preGameProcess")
	public League preGameProcess(@Named("pass") String passin)
	{
		League returner = new League(0, "Didn't Process");
		
		//At a basic level, the first thing I'm going to do is be sure that the password is right.  This is just to prevent someone from improperly making use of the system.
		if (!passin.equals(Constants.SYS_PASS))
		{
			return new League(0, "Incorrect Password!");
		}
		
		
		//The first step is to see if the "preGameProcess" flag has already been set for this week or if it still needs to be done.
		//If it does need to be done, the second query will set it.
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
			return new League (0, "Step 1 failed to create a valid url.");
		}
		
		
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			strquery = "Select preGameProcess From bedb1.SysInfo;";
			ResultSet rs = conn.createStatement().executeQuery(strquery);
			int count = 0;
			while (rs.next())
			{
				count++;
				if (!rs.getBoolean("preGameProcess"))
				{
					strquery = "UPDATE bedb1.SysInfo SET preGameProcess = 1;";
					conn.createStatement().executeUpdate(strquery);
				}
				else
				{
					conn.close();
					return new League (0, "preGameProcess already completed.");
				}
			}
			
			if (count == 0)
			{
				conn.close();
				return new League(0, "That's weird, successful DB login, but couldn't find preGameProcess!");
			}
			
		} 
		catch (SQLException e) 
		{
			return new League (0, ("SQL Exception:" + e.getMessage()));
		}
		
		
		//Next we will create all of the child bets.  Note that if this were a truly multi-tennant solution, it would be necessary to have an outside loop to load the players
		//for each league instead of the system I have currently that just assumes the league id is 1 and loads the users for that league.
		
		try 
		{
			//This first query loads bets_rs with all of the bets for this week from this league.
			strquery = "SELECT g.id as game, b.id as bet, b.email as email, b.home as home FROM bedb1.Bets b INNER JOIN bedb1.Games g ON b.game_id = g.id INNER JOIN bedb1.Weeks w ON g.week_id = w.id INNER JOIN bedb1.SysInfo s ON s.current_week = w.id WHERE b.league_season_id = 1;";
			ResultSet bets_rs = conn.createStatement().executeQuery(strquery);
			
			//This query loads players_rs with all of the players in the league.
			strquery = "SELECT u.email as email FROM bedb1.Users u INNER JOIN bedb1.User_League_Season_Maps ulsm ON u.email = ulsm.email WHERE ulsm.league_season_id = 1;";
			ResultSet players_rs = conn.createStatement().executeQuery(strquery);
			
			int bets=0;
			int minibets=0;
			int players=0;
			String better;
			String bettee;
			
			//Figure out the number of players
			while (players_rs.next())
			{
				players++;
			}
			players_rs.beforeFirst();
			
			
			while (bets_rs.next())
			{
				bets++;
				better = bets_rs.getString("email");
				
				while (players_rs.next())
				{
					minibets++;
					
					bettee = players_rs.getString("email");
					
					if (!better.equalsIgnoreCase(bettee))
					{
						strquery = "INSERT INTO bedb1.House_Bets (other_bettees, email, parent_bet_id) VALUES (" + (players - 1) + ", '" + bettee + "', " + bets_rs.getInt("bet") + ");";
						conn.createStatement().executeUpdate(strquery);
					}
					
				}
				
				players_rs.beforeFirst();
			}
			
			if (bets == 0)
			{
				conn.close();
				return new League(0, "Didn't find any bets.");
			}
			
			if (minibets == 0)
			{
				conn.close();
				return new League(0, "Didn't find any users.");
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			return new League (0, ("SQL Exception:" + e.getMessage()));
		}
		
		returner = new League(1, "Got to the end.");
		return returner;
	}
	
	
	//Display a user's bets.
	//If the "this_week" parameter is set to 1, then the bets will be only for this week.
	//If the "no_house" parameter is set to 1, then only bets that the user has made and not bets that they were forced to make will be returned.
		//The players bets will always come first and then the house bets.
	@ApiMethod(name="getUserBets", scopes = {Constants.EMAIL_SCOPE}, clientIds = {Constants.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID})
	public List<Bet> getUserBets (@Named("this_week") boolean this_week, @Named("no_house") boolean no_house, User guser) throws UnauthorizedException
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
			bet = new Bet("Error",e.getMessage(),"",0,"","");
			bets.add(bet);
			return bets;
		}
			
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(strurl);
			ResultSet rs;
			int betcount = 0;
			int res = 0;
			
			//The first two queries load the user's non-house bets the first one does it for just this week (if that's specified) the second one does it for all bets if that's specified.
			if (this_week == true)
			{
				strquery = "SELECT ls.bet_size AS bet_size, w.name_long AS game_week, b.home AS picked_home, g.home_result AS home_result, th.team AS home_team, ta.team AS away_team, g.home_line AS home_line FROM bedb1.Bets b INNER JOIN bedb1.Games g ON b.game_id = g.id INNER JOIN bedb1.Weeks w ON g.week_id = w.id INNER JOIN bedb1.Teams th ON g.home = th.id INNER JOIN bedb1.Teams ta ON g.away = ta.id INNER JOIN bedb1.League_Seasons ls ON b.league_season_id = ls.id INNER JOIN bedb1.SysInfo si ON si.current_week = w.id WHERE ls.id = 1 AND b.email = '" + guser.getEmail() + "' ORDER BY w.id;";
			}
			else if (this_week == false)
			{
				strquery = "SELECT ls.bet_size AS bet_size, w.name_long AS game_week, b.home AS picked_home, g.home_result AS home_result, th.team AS home_team, ta.team AS away_team, g.home_line AS home_line FROM bedb1.Bets b INNER JOIN bedb1.Games g ON b.game_id = g.id INNER JOIN bedb1.Weeks w ON g.week_id = w.id INNER JOIN bedb1.Teams th ON g.home = th.id INNER JOIN bedb1.Teams ta ON g.away = ta.id INNER JOIN bedb1.League_Seasons ls ON b.league_season_id = ls.id WHERE ls.id = 1 AND b.email = '" + guser.getEmail() + "' ORDER BY w.id;";	
			}
			
			
			rs = conn.createStatement().executeQuery(strquery);
			while (rs.next())
			{
				//Since Java autocorrects a null result (i.e. the game hasn't been played yet) to a 0 it can't be distinguished from a push.  This code sets it to the value that the constructor expects for games that haven't been played (-1132).
				res = rs.getInt("home_result");
				if (rs.wasNull())
				{
					res = -1132;
				}
		
				bet = new Bet(rs.getString("game_week"),res,rs.getInt("picked_home"),rs.getString("home_team"),rs.getString("away_team"),rs.getLong("home_line"), rs.getInt("bet_size"));
				bets.add(bet);
				betcount++;
			}
						
			//The second two queries will load all of the house bets.  Again this will either be for just the week or for the whole season.
			if (no_house == false)
			{
				if (this_week == true)
				{
					//The query if it's just this week.
					strquery = "Select h.other_bettees AS bettees, ls.bet_size AS bet_size, w.name_long AS game_week, b.result AS result, b.home AS picked_home, ht.team AS home_team, at.team AS away_team, g.home_line AS home_line, u.handle AS otherside From bedb1.House_Bets h INNER JOIN bedb1.Bets b ON b.id = h.parent_bet_id INNER JOIN bedb1.Games g ON g.id = b.game_id INNER JOIN bedb1.SysInfo s ON s.current_week = g.week_id INNER JOIN bedb1.Teams ht ON g.home = ht.id INNER JOIN bedb1.Teams at ON g.away = at.id INNER JOIN bedb1.Weeks w ON g.week_id = w.id INNER JOIN bedb1.Users u ON u.email = b.email INNER JOIN bedb1.League_Seasons ls ON ls.id = b.league_season_id WHERE b.league_season_id = 1 AND h.email = '" + guser.getEmail() + "' ORDER BY g.id;";
				}
				else if (this_week == false)
				{
					//The query if it's all weeks.
					strquery = "Select h.other_bettees AS bettees, ls.bet_size AS bet_size, w.name_long AS game_week, b.result AS result, b.home AS picked_home, ht.team AS home_team, at.team AS away_team, g.home_line AS home_line, u.handle AS otherside From bedb1.House_Bets h INNER JOIN bedb1.Bets b ON b.id = h.parent_bet_id INNER JOIN bedb1.Games g ON g.id = b.game_id INNER JOIN bedb1.Teams ht ON g.home = ht.id INNER JOIN bedb1.Teams at ON g.away = at.id INNER JOIN bedb1.Weeks w ON g.week_id = w.id INNER JOIN bedb1.Users u ON u.email = b.email INNER JOIN bedb1.League_Seasons ls ON ls.id = b.league_season_id WHERE b.league_season_id = 1 AND h.email = '" + guser.getEmail() + "' ORDER BY g.id;";
				}
					
				
				rs = conn.createStatement().executeQuery(strquery);
					
				String against;
				String home;
				String with;
				boolean firstwith;
				int picked_home;
				int likebets;
				long net;
				while (rs.next())
				{
					net = 0;
					likebets = 1;
					picked_home = rs.getInt("picked_home");
					home = rs.getString("home_team");
					against = rs.getString("otherside");
					with = "";
					firstwith = true;
						
					
					//The bets returned are expected to be aggregated, so let's see how many other bets the player has on the same game.  If this is the last one, then it must be the only house bet.
					if(!rs.isLast())
					{
						//This loop will run either to the end of the rs or to the first instance where a different game is in play.
						while ((rs.next()) && (home.equals(rs.getString("home_team"))))
						{
							//if the bet is the same as the first one, then add the user to the picked against and increase the nubmer of bets against.
							if (picked_home == rs.getInt("picked_home"))
							{
								likebets++;
								against = against + ", " + rs.getString("otherside");
							}
							//if the bet is on the same game, but on the other side then reduce the number of likebets.
							else
							{
								likebets--;
								if (firstwith)
								{
									with = rs.getString("otherside");
									firstwith = false;
								}
								else
								{
									with = with + ", " + rs.getString("otherside");
								}
							}
						}
						rs.previous();
					}
						
					//Since Java autocorrects a null result (i.e. the game hasn't been played yet) to a 0 it can't be distinguished from a push.  This code sets it to the value that the constructor expects for games that haven't been played (-1132).
					res = rs.getInt("result");
					if (rs.wasNull())
					{
						res = -1132;
					}
												
					if (likebets > 0)
					{
						//The net bet is going to be the cost per bet times the like bets where cost per bet is the bet_size divided by the bettees.
						net = rs.getInt("bet_size");
						net = net / (rs.getInt("bettees"));
						net = net * likebets;
						
						bet = new Bet(rs.getString("game_week"), res, picked_home, home, rs.getString("away_team"), rs.getLong("home_line"), against, net);
						bets.add(bet);
					}
					else if (likebets < 0)
					{
						//The net bet is going to be the cost per bet (as above) times the like bets.
						net = rs.getInt("bet_size");
						net = net / (rs.getInt("bettees"));
						net = net * likebets * -1;
							
						//Since, as it turns out, there were more bets against the initial position than for it, we need to flip the picked_home variable.
						if (picked_home == 0)
						{
							picked_home = 1;
						}
						else
						{
							picked_home = 0;
						}
						
						bet = new Bet(rs.getString("game_week"), res, picked_home, home, rs.getString("away_team"), rs.getLong("home_line"), with, net);
						bets.add(bet);
					}
				}
				
				
			}
			
			conn.close();
		} 
		catch (SQLException e) 
		{
			bet = new Bet("SQL Error", e.getMessage(),"",0,"","");
			bets.add(bet);
			return bets;
		}
			
			
		return bets;
	}
	
	//Admin Screen Method to Receive a Game Result.  Should take the team and the win by.
		
	//Admin Screen Method to Enter a New Game 
		
	//Admin Screen Method to change the current week to the next week (set the preGameProcess flag to zero).
	
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