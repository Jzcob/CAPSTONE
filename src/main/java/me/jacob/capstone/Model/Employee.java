package me.jacob.capstone.Model;

/**
 * Represents an employee within the Majikku Stores system.
 * This model class holds core personal and professional details
 * corresponding to the 'Employees' table in the database.
 *
 * @author Jacob
 * @version 1.0
 */
public class Employee {

    /**
     * The unique identifier for the employee (Primary Key).
     */
    private int employeeID;

    /**
     * The employee's first name.
     */
    private String firstName;

    /**
     * The employee's last name.
     */
    private String lastName;

    /**
     * The identifier for the employee's specific job role (Foreign Key to JobTitles).
     * <p>
     * Common IDs: 1=Manager, 4=Cashier, 6=Stock Associate.
     * </p>
     */
    private int jobID;

    /**
     * Constructs a new Employee object with the specified details.
     *
     * @param employeeID The unique ID of the employee.
     * @param firstName  The first name of the employee.
     * @param lastName   The last name of the employee.
     * @param jobID      The ID corresponding to the employee's job title.
     */
    public Employee(int employeeID, String firstName, String lastName, int jobID) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobID = jobID;
    }

    /**
     * Gets the unique identifier for this employee.
     *
     * @return The employee ID.
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets the unique identifier for this employee.
     *
     * @param employeeID The new employee ID.
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Gets the employee's first name.
     *
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the employee's first name.
     *
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the employee's last name.
     *
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the employee's last name.
     *
     * @param lastName The new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the job title ID associated with this employee.
     *
     * @return The job ID.
     */
    public int getJobID() {
        return jobID;
    }

    /**
     * Sets the job title ID for this employee.
     *
     * @param jobID The new job ID.
     */
    public void setJobID(int jobID) {
        this.jobID = jobID;
    }
}