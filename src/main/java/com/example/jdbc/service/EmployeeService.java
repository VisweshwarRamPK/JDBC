package com.example.jdbc.service;

import com.example.jdbc.model.Employee;
import com.example.jdbc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;


    private final Map<Long, Employee> employeeCache = new ConcurrentHashMap<>();

    public Employee getEmployeeById(Long id) {

        Employee cachedEmployee = employeeCache.get(id);
        if (cachedEmployee != null) {
            System.out.println("Returning employee from cache: " + id);
            return cachedEmployee;
        }


        System.out.println("Cache miss. Fetching employee from database for ID: " + id);
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee != null) {
            employeeCache.put(id, employee);
        }
        return employee;
    }

    public List<Employee> getAllEmployees() {

        return employeeRepository.getAllEmployees();
    }

    public String createEmployee(Employee employee) {

        employeeCache.clear();
        return employeeRepository.createEmployee(employee);
    }

    public String updateEmployee(Long id, Employee employee) {

        employeeCache.remove(id);
        String result = employeeRepository.updateEmployee(id, employee);


        if (result.equals("Employee updated successfully")) {
            employeeCache.put(id, employee);
        }
        return result;
    }

    public String deleteEmployee(Long id) {

        employeeCache.remove(id);
        return employeeRepository.deleteEmployee(id);
    }
}
