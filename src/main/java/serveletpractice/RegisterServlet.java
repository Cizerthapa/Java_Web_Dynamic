package serveletpractice;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;

import controller.DatabaseController;
import model.StudentModel;

@WebServlet(asyncSupported = true, urlPatterns = { "/RegisterServlet" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	DatabaseController dbController = new DatabaseController();

	public RegisterServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	private int checkString(String check) {
		if (check.isEmpty()) {
			return -1;
		} else if (symbolValidate(check)) {
			return -1;
		} else {
			return 0;
		}
	}

	private boolean symbolValidate(String n) {
		// Variable that stores the integer value of the character (32 - 47 | 58 - 64 |
		// 91 - 96 | 123 - 126)
		int[][] ranges = { { 33, 47 }, { 58, 64 }, { 91, 96 }, { 123, 126 } };
		// Calculate the total size of the array and Create the array
		int totalSize = 0;
		for (int[] range : ranges) {
			totalSize += range[1] - range[0] + 1;
		}
		int[] intArray = new int[totalSize];

		// Assign value in the array
		int currentIndex = 0;
		for (int[] range : ranges) {
			for (int i = range[0]; i <= range[1]; i++) {
				intArray[currentIndex++] = i;
			}
		}
		// Get the length of the string using the length method
		int length = n.length();

		// loop to iterate over each char in string
		for (int i = 0; i < length; i++) {
			// Use the charAt() method to get the character at the current index
			int currentChar = (int) n.charAt(i);
			for (int j = 0; j < intArray.length; j++) {
				if (currentChar == intArray[j]) {
					System.out.println("Validation error");
					return true;
				}
			}
		}
		return false;
	}

	private int checkUsername(String usrNm) {
		byte usrNmLength = (byte) usrNm.length();
		byte rqrdNum = 6;
		if (usrNmLength >= rqrdNum && checkString(usrNm) == 0) {
			return 0;
		} else {
			return -1;
		}
	}

	private int checkPhone(String number) {
		// Ensure the length of the number is at least 14
		if (number.length() >= 14) {
			// Iterate over each character of the number starting from the second character
			for (int i = 1; i < number.length(); i++) {
				// Check if each character is a digit
				if (!Character.isDigit(number.charAt(i))) {
					return -1; // Return -1 if any character is not a digit
				}
			}
			// Check if the number starts with '+'
			if (number.startsWith("+")) {
				return 0; // Return 0 if the number meets the criteria
			}
		}
		return -1; // Return -1 if the length is less than 14 or doesn't start with '+'
	}

	private int genderCheck(String check) {
		if (check.equalsIgnoreCase("male") || check.equalsIgnoreCase("female")) {
			return 0;
		}
		return -1;
	}
	
	private static boolean isSpecialCharacter(char ch) {
		// Define your set of special characters
		String specialChars = "!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";

		// Check if the character is a special character
		return specialChars.contains(String.valueOf(ch));
	}
	
	private static boolean isValidPassword(String password) {
		// Check if password length is greater than 6 characters
		if (password.length() <= 6) {
			return false;
		}

		// Check if password contains at least one number, one special character, and
		// one capital letter
		boolean hasNumber = false;
		boolean hasSpecialChar = false;
		boolean hasCapitalLetter = false;

		for (char ch : password.toCharArray()) {
			if (Character.isDigit(ch)) {
				hasNumber = true;
			} else if (isSpecialCharacter(ch)) {
				hasSpecialChar = true;
			} else if (Character.isUpperCase(ch)) {
				hasCapitalLetter = true;
			}

			// If all required conditions are met, return true
			if (hasNumber && hasSpecialChar && hasCapitalLetter) {
				return true;
			}
		}

		// If any of the conditions are not met, return false
		return false;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user_name = request.getParameter("username");
		String first_name = request.getParameter("firstName");
		String last_name = request.getParameter("lastName");
		String dobString = request.getParameter("birthday");
		LocalDate dob = LocalDate.parse(dobString);
		String gender = request.getParameter("gender");
		String email = request.getParameter("email");
		String phone_number = request.getParameter("phoneNumber");
		String subject = request.getParameter("subject");
		String password = request.getParameter("password");
		String repassword = request.getParameter("retypePassword");

		System.out.println("Now Checking process begins ");

		if (checkUsername(user_name) == 0 && checkString(first_name) == 0 && checkString(last_name) == 0
				&& checkPhone(phone_number) == 0 && genderCheck(gender) == 0 && isValidPassword(password)) {
			if (dbController.getUserNameFromDb(user_name) == 1 && dbController.getEmailFromDb(email) == 1 && dbController.getPhoneFromDb(phone_number) == 1) {
				response.sendRedirect(request.getContextPath() + "/pagesHtml/register.html");
				System.out.println("Already Enrolled");
			} else {
				System.out.println("Username, First Name, Last Name, gender and password is Validated");

				if (password.equalsIgnoreCase(repassword)) {
					StudentModel studentModel = new StudentModel(user_name, first_name, last_name, dob, gender, email,
							phone_number, subject, password);
					int result = dbController.addStudent(studentModel);
					if (result > 0) {
						response.sendRedirect(request.getContextPath() + "/pagesHtml/login.html");
						System.out.println("Validation Done");
					} else {
						// later
						response.sendRedirect(request.getContextPath() + "/pagesHtml/register.html");
					}
				}
			}
		}
	}
}
