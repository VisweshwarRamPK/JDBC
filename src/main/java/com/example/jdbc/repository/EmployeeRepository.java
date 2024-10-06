package com.example.jdbc.repository;

import com.example.jdbc.model.Employee;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final String url = "jdbc:postgresql://localhost:5432/Employees";
    private final String user = "postgres";
    private final String password = "2012061";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Employee getEmployeeById(Long id) {
        System.out.println("Fetching employee from the database: " + id);
        String query = "SELECT * FROM employee WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employee> getAllEmployees() {
        String query = "SELECT * FROM employee";
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public String createEmployee(Employee employee) {
        String query = "INSERT INTO employee (name, position, salary) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.executeUpdate();
            return "Employee created successfully";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error creating employee: " + e.getMessage();
        }
    }

    public String updateEmployee(Long id, Employee employee) {
        String query = "UPDATE employee SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setLong(4, id);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0 ? "Employee updated successfully" : "Employee not found";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error updating employee: " + e.getMessage();
        }
    }

    public String deleteEmployee(Long id) {
        String query = "DELETE FROM employee WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0 ? "Employee deleted successfully" : "Employee not found";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error deleting employee: " + e.getMessage();
        }
    }
}
