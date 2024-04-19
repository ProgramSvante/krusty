package krusty;

import spark.Request;
import spark.Response;

import java.util.ArrayList;
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
		String Query  = "SELECT * From customers";

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
		String Query  = "SELECT * From cookies";
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

		String Query  = "SELECT * from recipies";

		try(PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "recipies");
			return json;
		}
			catch (SQLException e) {
				throw new RuntimeException(e);

			}
	}

	public String getPallets(Request req, Response res) {
		String sql = "SELECT  From pallets where 1=1"; 
 
		ArrayList<String> values = new ArrayList<String>(); 
		if (req.queryParams("from") != null) {
            sql += " AND production_date >= ?";
            values.add(req.queryParams("from"));
        }
        if (req.queryParams("to") != null) {
            sql += " AND production_date <= ?";
            values.add(req.queryParams("to"));
        }
        if (req.queryParams("cookie") != null) {
            sql += " AND cookie = ?";
            values.add(req.queryParams("cookie"));
        }
        if (req.queryParams("blocked") != null) {
            sql += " AND blocked = ?";
            values.add(req.queryParams("blocked"));
        }
		
		try (PreparedStatement ps = conn.prepareStatement(sql)) { 
			for (int i = 0; i < values.size(); i++) { 
				ps.setString(i+1, values.get(i)); 
			} 
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "pallets");
			return json;
		} catch (SQLException e) { 
			throw new RuntimeException(e);
		} 
	}

	public String reset(Request req, Response res) {
		return "{}";
	}

	public String createPallet(Request req, Response res) {
		String cookie = req.queryParams("cookie");
		//if(DataBase.getCookies().)
		return "{}";
	}
}
