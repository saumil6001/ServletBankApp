package com.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataScource {

	private static Connection connection = null;
	private final String database_url = "jdbc:mysql://localhost/";
	private final String database_name = "test";
	private final String database_userName = "root";
	private final String databse_passward = "123456789";
	private final String database_driverName = "com.mysql.jdbc.Driver";
	private final String sql = "select AccountNum,UserName,user_id from UserName where UserName=? and AccountNum= ?";

	public DataScource() {
	}

	
	public boolean connect(String userName, String passaword) {

		PreparedStatement statement=null;
		ResultSet rs = null;
		try {
			Class.forName(database_driverName);
			connection = DriverManager.getConnection(database_url + database_name, database_userName,databse_passward);
			statement = connection.prepareStatement(sql);
			statement.setString(1, userName);
			statement.setString(2, passaword);
			rs = statement.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {

			if(statement!=null) {
				
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return false;
	}

	public static Connection getConnection() {
		return connection;
	}

	public boolean closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
