package krusty;

import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.sql.*;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se";

	// For use with MySQL or PostgreSQL
	private Connection conn;
	private static final String jdbcUsername = "hbg10";
	private static final String jdbcPassword = "mtt400wi";

	public void connect() {
		
		// Connect to database here		
		try {
			conn = DriverManager.getConnection(jdbcString + "/" +
					"hbg10", jdbcUsername, jdbcPassword);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
<<<<<<< HEAD
		String Query  = "SELECT name, address From customers";
=======
		String Query  = "SELECT * From customers";
>>>>>>> 52549ea21ac77071d06702033eebe1aa3ffd7a53
		try(PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "customers");
			return json;
		}
			catch (SQLException e) {
				throw new RuntimeException(e);

			}
		
	
	}

	public String getRawMaterials(Request req, Response res) {
		String Query  ="Select name, amount, unit from raw_materials";
		try(PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "raw-materials");
			return json;
		}
			catch (SQLException e) {
				throw new RuntimeException(e);

			}
	}

	public String getCookies(Request req, Response res) {
		String Query  = "SELECT name From cookies";
		try(PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "cookies");
			return json;
		}
			catch (SQLException e) {
				throw new RuntimeException(e);

			}
	}

	public String getRecipes(Request req, Response res) {
		return "{}";
	}

	public String getPallets(Request req, Response res) {
		return "{\"pallets\":[]}";
	}

	public String reset(Request req, Response res) {
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		return "{}";
	}
}
