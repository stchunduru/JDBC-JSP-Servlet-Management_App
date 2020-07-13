package usermanagementapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import usermanagementapp.model.User;

// This data access object class provides CRUD database operations for the table users in the database
public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/management_app";
	private String jdbcUsername = "root";
	private String jdbcPassword = "root";

	private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
	private static final String SELECT_USER_BY_ID = "SELECT id, name, email, country FROM users where id = ?;";
	private static final String DELETE_USERS_SQL = "DELETE FROM users where id = ?;";
	private static final String SELECT_ALL_USERS = "SELECT * FROM users;";
	private static final String UPDATE_USERS_SQL = "UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?;";
	
	protected Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}
	
	// create or insert user
	public void insertUser(User user) throws SQLException {
		try (Connection connection = getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(INSERT_USERS_SQL);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getCountry());
			stmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// update user
	public boolean updateUser(User user) throws SQLException {
		boolean updated;
		try (Connection connection = getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(UPDATE_USERS_SQL);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getCountry());
			stmt.setInt(4, user.getId());
			
			updated = stmt.executeUpdate() > 0;
			
		}
		return updated;
	}
	// select user by id
	public User getUser(int id) throws SQLException {
		User user = null;
		try (Connection connection = getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SELECT_USER_BY_ID);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				user = new User(id, name, email, country);
			}
		}
		return user;
		
	}
	
	// select all users
	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
		
		try (Connection connection = getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_USERS);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String country = rs.getString("country");
				users.add(new User(id, name, email, country));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return users;
	}

	// delete user
	public boolean deleteUser(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = getConnection();
				PreparedStatement stmt = connection.prepareStatement(DELETE_USERS_SQL);) {
			stmt.setInt(1, id);
			rowDeleted = stmt.executeUpdate() > 0;
		}
		return rowDeleted;
	}
	
	
	
}
