package krusty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import spark.Request;
import spark.Response;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to
	 * your database!
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
		String Query = "SELECT * From customers";

		try (PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "customers");
			return json;
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}

	}

	public String getRawMaterials(Request req, Response res) {
		String Query = "Select name, amount, unit from raw_materials";
		try (PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "raw-materials");
			return json;
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

	public String getCookies(Request req, Response res) {
		String Query = "SELECT * From cookies";
		try (PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "cookies");
			return json;
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

	public String getRecipes(Request req, Response res) {

		String Query = "SELECT * from recipes";

		try (PreparedStatement ps = conn.prepareStatement(Query)) {
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "recipes");
			return json;
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

	public String getPallets(Request req, Response res) {
		String sql = "SELECT pallet_ID, dateTime as production_date, cookieName as cookie, location, blocked, customer From pallets where 1=1";
		ArrayList<String> values = new ArrayList<String>();
		if (req.queryParams("from") != null) {
			sql += " AND dateTime >= ?";
			values.add(req.queryParams("from"));
		}
		if (req.queryParams("to") != null) {
			sql += " AND dateTime <= ?";
			values.add(req.queryParams("to"));
		}
		if (req.queryParams("blocked") != null) {
			sql += " AND blocked = ?";
			values.add(req.queryParams("blocked"));
		}
		if (req.queryParams("cookie") != null) {
			sql += " AND cookieName = ?";
			values.add(req.queryParams("cookie"));
		}
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (int i = 0; i < values.size(); i++) {
				ps.setString(i + 1, values.get(i));
			}
			ResultSet rs = ps.executeQuery();
			String json = Jsonizer.toJson(rs, "pallets");
			return json;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String reset(Request req, Response res) {
		executeFile("initial-data.sql");
		getCustomers(req, res);
		getCookies(req, res);
		getRawMaterials(req, res);
		getRecipes(req, res);

		return "ok";
	}

	private void executeFile(String path) {
		try (FileReader reader = new FileReader(path, StandardCharsets.UTF_8);
				BufferedReader bufferedReader = new BufferedReader(reader);
				Statement statement = conn.createStatement();) {

			StringBuilder builder = new StringBuilder();

			String line;
			int lineNumber = 0;
			int count = 0;
			while ((line = bufferedReader.readLine()) != null) {
				lineNumber += 1;
				line = line.trim();
				if (line.isEmpty() || line.startsWith("--"))
					continue;
				builder.append(line);
				if (line.endsWith(";"))
					try {
						statement.execute(builder.toString());
						System.out.println(
								++count
										+ " Command successfully executed : "
										+ builder.substring(
												0,
												Math.min(builder.length(), 25))
										+ "...");
						builder.setLength(0);
					} catch (SQLException e) {
						System.err.println(
								"At line " + lineNumber + " : "
										+ e.getMessage() + "\n");
						return;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String createPallet(Request req, Response res) {

		String cookie = req.queryParams("cookie");
		//String blocked = req.queryParams("blocked");
		if (!cookieExists(cookie)) {
			return "{\"status\": \"unknown cookie\"}";
		}

		String insertSql = "insert into pallets (dateTime, location, blocked, cookieName) values (now(), ?, ?, ?)";
		String getRecipes = "select * from recipes where cookieName = ?";
		try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, "warehouse1");
			ps.setString(2, "no");
			ps.setString(3, cookie);
			int rowsInserted = ps.executeUpdate();

			if (rowsInserted > 0) {
				try(ResultSet generatedKeys = ps.getGeneratedKeys()){

				try(PreparedStatement ps1 = conn.prepareStatement(getRecipes)){
					ps1.setString(1, cookie);
					
					ResultSet rs = ps1.executeQuery();
					rs.beforeFirst();
					while(rs.next()){
						System.out.println(rs.getString("ingredient") + " " + rs.getInt("quantityInRecipe"));
						int amount = rs.getInt("quantityInRecipe") * 54;
						String recipeString = rs.getString("ingredient");

						String SQLUpdateRaw = "update raw_materials set amount = amount - ? where name = ?";
						try(PreparedStatement ps2 = conn.prepareStatement(SQLUpdateRaw)){
							ps2.setInt(1,amount);
							ps2.setString(2, recipeString);
							ps2.executeUpdate();
						}catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}
				if (generatedKeys.next()) {
					Integer palletId = generatedKeys.getInt(1);

					return "{\"status\": \"ok\", \"id\": " + palletId + "}";
				}
			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "{\"status\": \"error\"}";
	}

	private boolean cookieExists(String cookie) {
		String Query = "SELECT * From cookies where name = ?";
		try (PreparedStatement ps = conn.prepareStatement(Query)) {
			ps.setString(1, cookie);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
