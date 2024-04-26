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
		String sql = "SELECT * From pallets where 1=1";
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
				// Wrap the FileReader in a BufferedReader for
				// efficient reading.
				BufferedReader bufferedReader = new BufferedReader(reader);
				// Establish a connection to the database.
				// Create a statement object to execute SQL
				// commands.
				Statement statement = conn.createStatement();) {

			StringBuilder builder = new StringBuilder();

			String line;
			int lineNumber = 0;
			int count = 0;

			// Read lines from the SQL file until the end of the
			// file is reached.
			while ((line = bufferedReader.readLine()) != null) {
				lineNumber += 1;
				line = line.trim();

				// Skip empty lines and single-line comments.
				if (line.isEmpty() || line.startsWith("--"))
					continue;

				builder.append(line);
				// If the line ends with a semicolon, it
				// indicates the end of an SQL command.
				if (line.endsWith(";"))
					try {
						// Execute the SQL command
						statement.execute(builder.toString());
						// Print a success message along with
						// the first 15 characters of the
						// executed command.
						System.out.println(
								++count
										+ " Command successfully executed : "
										+ builder.substring(
												0,
												Math.min(builder.length(), 25))
										+ "...");
						builder.setLength(0);
					} catch (SQLException e) {
						// If an SQLException occurs during
						// execution, print an error message and
						// stop further execution.
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

		if (!cookieExists(cookie)) {
			return "{\"status\": \"unknown cookie\"}";
		}

		String insertSql = "insert into pallets (dateTime, location, blocked, cookieName) values (now(), ?, ?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(insertSql)) {

			ps.setString(1, "warehouse1");
			ps.setString(2, "no");
			ps.setString(3, cookie);
			int rowsInserted = ps.executeUpdate();

			if (rowsInserted > 0) {
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (generatedKeys.next()) {
					Integer palletId = generatedKeys.getInt(1);
					return "{\"status\": \"ok\", \"id\": " + palletId + "}";
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
			String json = Jsonizer.toJson(rs, "cookies");
			return !json.equals("");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
