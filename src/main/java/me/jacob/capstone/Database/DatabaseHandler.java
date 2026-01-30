package me.jacob.capstone.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the connection to the MySQL database for the Majikku Stores application.
 * <p>
 * This class implements the <b>Singleton Design Pattern</b> to ensure that only one
 * instance of the database handler exists throughout the application's lifecycle.
 * It handles the loading of the JDBC driver and establishing the connection.
 * </p>
 *
 * @author Jacob
 * @version 1.0
 */
public class DatabaseHandler {

    /**
     * The single instance of the DatabaseHandler.
     */
    private static DatabaseHandler handler = null;

    /**
     * The active connection to the database.
     */
    private static Connection connection = null;

    /**
     * The URL of the database to connect to.
     */
    private static final String DB_URL = "jdbc:mysql://localhost:3306/MajikkuStores";

    /**
     * The username for the database authentication.
     */
    private static final String USER = "root";

    /**
     * The password for the database authentication.
     */
    private static final String PASS = "admin";

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the connection immediately upon creation.
     */
    private DatabaseHandler() {
        createConnection();
    }

    /**
     * Retrieves the single instance of the DatabaseHandler.
     * <p>
     * If the instance does not exist, it creates one. Otherwise, it returns
     * the existing instance.
     * </p>
     *
     * @return The singleton instance of DatabaseHandler.
     */
    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    /**
     * Establishes the connection to the database using the MySQL JDBC driver.
     * <p>
     * It loads the {@code com.mysql.cj.jdbc.Driver} class and attempts to
     * connect using the credentials defined in the constants.
     * </p>
     */
    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    /**
     * Gets the active database connection.
     *
     * @return The {@link Connection} object for the database.
     */
    public Connection getConnection() {
        return connection;
    }
}