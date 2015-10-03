package com.football;

import java.util.ArrayList;
import java.util.List;

//DB Enabling Libraries
import java.io.*;
import java.sql.*;

import com.google.appengine.api.utils.SystemProperty;

public class Record
{
	String winloss;
	String winnings;
	String username;

	public Record()
	{
		//blank
	}

	public Record(String winlossin, String winningsin, String usernamein)
	{
		super();

		this.winloss = winlossin;
		this.winnings = winningsin;
		this.username = usernamein;
	}

	public Record(String email, int league_season_id)
	{
		String strquery=null;
		String strurl=null;

		try
		{

			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production)
			{
			    // Load the class that provides the new "jdbc:google:mysql://" prefix.
			    Class.forName("com.mysql.jdbc.GoogleDriver");
			    strurl = "jdbc:google:mysql://axial-potential-94723?user=root";
			}
			else {
			    // Local MySQL instance to use during development.
			    Class.forName("com.mysql.jdbc.Driver");
			    strurl = "jdbc:mysql://127.0.0.1:3306/bedb?user=root";
			}


		}
		catch (ClassNotFoundException e) {
			return;
		}

		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(strurl);
			ResultSet rs;
			int week_id;

			//First retreive the players first name.
			strquery = "SELECT u.fname AS fname FROM bedb1.Users u WHERE u.email = '" + email + "';";
			rs = conn.createStatement().executeQuery(strquery);
			if (rs.next())
			{
				this.username = rs.getString("fname");
			}
			else
			{
				//Should throw an exception for no user with that name.
				return;
			}

			//This section will retreive the bet value from the database.
			int betsize = 0;
			strquery = "SELECT ls.bet_size AS bet_size FROM bedb1.League_Seasons ls WHERE ls.id = " + league_season_id + ";";
			rs = conn.createStatement().executeQuery(strquery);
			if (rs.next())
			{
				betsize = rs.getInt("bet_size");
			}
			else
			{
				//Should throw an exception for not being able to find the league season.
				return;
			}


			//This whole section will be about figuring out wins and losses and winnings on the players bets.
			int wins = 0;
			int losses = 0;
			int push = 0;
			double winnings = 0.0;
			boolean homewin = false;
			boolean bethome = false;

			strquery = "SELECT b.home AS bet_on_home, g.home_line AS home_line, g.home_result AS home_result FROM bedb1.Bets b INNER JOIN bedb1.Games g ON b.game_id = g.id  WHERE b.email = '" + email +"' AND b.league_season_id = " + league_season_id + " AND g.home_result IS NOT NULL;";
			rs = conn.createStatement().executeQuery(strquery);

			while (rs.next())
			{
				if (rs.getInt("home_line") == rs.getInt("home_result"))
				{
					push++;
				}
				else
				{
					homewin = (rs.getInt("home_line") > rs.getInt("home_result"));
					bethome = (rs.getInt("bet_on_home") == 1);

					if ((bethome && homewin) || (!bethome && !homewin))
					{
						wins++;
						winnings = winnings + betsize;
					}
					else
					{
						losses++;
						winnings = winnings - betsize;
					}
				}
			}


			//This section will add up the value of the house bets
			double size = 0.0;
			strquery = "SELECT hb.other_bettees AS otherbetters, b.home AS bet_on_home, g.home_line AS home_line, g.home_result AS home_result FROM bedb1.House_Bets hb INNER JOIN bedb1.Bets b ON b.id = hb.parent_bet_id INNER JOIN bedb1.Games g ON g.id = b.game_id WHERE hb.email = '" + email + "' AND g.home_result IS NOT NULL AND b.league_season_id = " + league_season_id + ";";
			rs = conn.createStatement().executeQuery(strquery);

			while (rs.next())
			{
				size = ((double)betsize) / ((double)rs.getInt("otherbetters"));
				if (rs.getInt("home_line") != rs.getInt("home_result")) //A push doesn't matter in house bets, so only process if it's not.
				{
					homewin = (rs.getInt("home_line") > rs.getInt("home_result"));
					bethome = (rs.getInt("bet_on_home") == 0); //You have the opposite bet of the person who made the bet.

					if ((bethome && homewin) || (!bethome && !homewin))
					{
						winnings = winnings + size;
					}
					else
					{
						winnings = winnings - size;
					}
				}
			}




			//This section will calculate the string values for the user.
			this.winloss = "" + wins + "-" + losses + "-" + push;
			this.winnings = "$" + (Math.round(winnings*100.0)/100.0);
		}
		catch (SQLException e)
		{
			return;
		}
	}

	public String getWinLoss()
	{
		return this.winloss;
	}

	public String getWinnings()
	{
		return this.winnings;
	}

	public String getUsername()
	{
		return this.username;
	}

}