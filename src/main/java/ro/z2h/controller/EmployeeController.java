package ro.z2h.controller;

import ro.z2h.annotation.MyController;
import ro.z2h.annotation.MyRequestMethod;
import ro.z2h.domain.Employee;
import ro.z2h.service.EmployeeServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru on 11.11.2014.
 */
@MyController(urlPath = "/employees")
public class EmployeeController {

    //TODO
    @MyRequestMethod(urlPath = "/all")
    public List<Employee> getAllEmployees() {
        List<Employee> arrayList = new ArrayList<Employee>();
        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
        arrayList = employeeService.findAllEmployees();

        return arrayList;
    }

    @MyRequestMethod(urlPath = "/one")
    public Employee getOneEmployee(Long id) {
        Employee emp = new Employee();
        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
        emp = employeeService.findOneEmployee(id);

        return emp;
    }
}