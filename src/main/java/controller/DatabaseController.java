package controller;

import java.sql.Connection;
import java.io.PrintWriter;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.StringUtils;
import model.StudentModel;

public class DatabaseController {
	
	PrintWriter printOut;
	public Connection getConnection() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3310/college_app";
	 return DriverManager.getConnection(url, "root", "");
 }

	public int addStudent (StudentModel studentModel) {
	try (Connection con = getConnection()) {
		PreparedStatement st = con.prepareStatement (StringUtils.INSERT_STUDENT);
		st.setString(1, studentModel.getUsername());
	 	st.setString(2, studentModel.getFirstName());
		st.setString(3, studentModel.getLastName());
		st.setDate(4, Date.valueOf(studentModel.getDob()));
		st.setString(5, studentModel.getGender());
		st.setString (6, studentModel.getEmail());
		st.setString (7, studentModel.getPhoneNumber());
		st.setString (8, studentModel.getSubject());
		st.setString (9, studentModel.getPassword());
	
		int result = st.executeUpdate();
		return result > 0 ? 1 : 0;
	} catch (SQLException | ClassNotFoundException ex) {
		ex.printStackTrace();
		ex.getMessage();// Log the exception for debugging return -1;
		return -1;
		}
	}

	public int getStudentLoginInfo (String username, String password) {
	try (Connection con = getConnection()) {
		PreparedStatement st = con.prepareStatement (StringUtils.GET_LOGIN_STUDENT_INFO);
		st.setString(1, username);
		st.setString (2, password);
		ResultSet rs = st.executeQuery();
	if (rs.next()){
		// User name and password match in the database
		return 1;
	} else {
		// No matching record found
		return 0;
	}
	} catch (SQLException | ClassNotFoundException ex) {
		ex.printStackTrace();
		ex.getMessage();// Log the exception for debugging 
		return -1;
	}
	}
	public String getStudentUserName (String username, String password) {
		try (Connection con = getConnection()) {
			PreparedStatement st = con.prepareStatement (StringUtils.GET_LOGIN_STUDENT_INFO);
			st.setString(1, username);
			st.setString (2, password);
			ResultSet rs = st.executeQuery();
		if (rs.next()){
			// User name and password match in the database
			return username;
		} else {
			// No matching record found
			return "";
		}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			ex.getMessage();// Log the exception for debugging 
			return null;
		}
	}
	public int getUserNameFromDb(String userName){
		try (Connection con = getConnection()) {
			PreparedStatement st = con.prepareStatement (StringUtils.GET_USERNAME);
			st.setString(1, userName);
			ResultSet rs = st.executeQuery();
			if (rs.next()){
				// User name match in the database
				return 1;
			} else {
				// No matching record found
				return 0;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			ex.getMessage();// Log the exception for debugging 
			return -1;
		}
		}
	public int getEmailFromDb(String email){
		try (Connection con = getConnection()) {
			PreparedStatement st = con.prepareStatement (StringUtils.GET_EMAIL);
			st.setString(1, email);
			ResultSet rs = st.executeQuery();
			if (rs.next()){
				// User name match in the database
				return 1;
			} else {
				// No matching record found
				return 0;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			ex.getMessage();// Log the exception for debugging 
			return -1;
		}
	}
	public int getPhoneFromDb(String phone){
		try (Connection con = getConnection()) {
			PreparedStatement st = con.prepareStatement (StringUtils.GET_PHONE);
			st.setString(1, phone);
			ResultSet rs = st.executeQuery();
			if (rs.next()){
				// User name match in the database
				return 1;
			} else {
				// No matching record found
				return 0;
			}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			ex.getMessage();// Log the exception for debugging 
			return -1;
		}
		}
}

