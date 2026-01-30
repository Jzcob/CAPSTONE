package me.jacob.capstone.DAO;

import me.jacob.capstone.Database.DatabaseHandler;
import me.jacob.capstone.Model.Employee;
import me.jacob.capstone.Utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for handling database operations related to Employees.
 * <p>
 * This class encapsulates the SQL queries required to retrieve, validate, and manage
 * employee records, ensuring a clean separation between the database layer and the application logic.
 * </p>
 *
 * @author Jacob
 * @version 1.0
 */
public class EmployeeDAO {

    /**
     * Validates an employee's login credentials.
     * <p>
     * This method follows a secure "Fetch-then-Verify" approach:
     * <ol>
     * <li>Queries the database for the user by {@code employeeID} only.</li>
     * <li>Retrieves the stored password hash and the unique salt.</li>
     * <li>Uses {@link PasswordUtils#verifyPassword} to hash the input password with the stored salt.</li>
     * <li>Compares the calculated hash against the stored hash.</li>
     * </ol>
     * </p>
     *
     * @param employeeID    The unique ID of the employee attempting to log in.
     * @param passwordInput The plain-text password entered by the user.
     * @return An {@link Employee} object if credentials are valid; {@code null} if the user is not found or the password is incorrect.
     */
    public Employee validateLogin(int employeeID, String passwordInput) {
        String sql = "SELECT * FROM EmployeeCredentials ec " +
                "JOIN Employees e ON ec.EmployeeID = e.EmployeeID " +
                "WHERE ec.EmployeeID = ?";

        Connection connection = DatabaseHandler.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeID);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                byte[] storedHash = resultSet.getBytes("PasswordHash");
                byte[] salt = resultSet.getBytes("Salt");

                if (PasswordUtils.verifyPassword(passwordInput, salt, storedHash)) {
                    return new Employee(
                            resultSet.getInt("EmployeeID"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("LastName"),
                            resultSet.getInt("JobID")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found OR password mismatch
    }
}