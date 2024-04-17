package krusty;

import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se";

	// For use with MySQL or PostgreSQL
	private Connection conn;
	private static final String jdbcUsername = "hbg19";
	private static final String jdbcPassword = "kfy510fe";

	public void connect() {
		// Connect to database here		
		try {
			conn = DriverManager.getConnection(jdbcString + "/" +
					"hbg19", jdbcUsername, jdbcPassword);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
		return "{}";
	}

	public String getRawMaterials(Request req, Response res) {
		return "{}";
	}

	public String getCookies(Request req, Response res) {
		return "{\"cookies\":[]}";
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
