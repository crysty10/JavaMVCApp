package ro.z2h.service;

import ro.z2h.dao.EmployeeDao;
import ro.z2h.domain.Employee;
import ro.z2h.utils.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dumitru on 12.11.2014.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private final static String username = "ZTH_07";
    private final static String password = "passw0rd";

    @Override
    public List<Employee> findAllEmployees() {

        EmployeeDao employeeDao = new EmployeeDao();
        ArrayList<Employee> arrayList = new ArrayList<Employee>();
        Connection con = DatabaseManager.getConnection(username, password);
        try {

            arrayList = employeeDao.getAllEmployees(con);
            return arrayList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<Employee>();
    }

    @Override
    public Employee findOneEmployee(Long id) {

        EmployeeDao employeeDao = new EmployeeDao();
        Employee employee = new Employee();
        Connection con = DatabaseManager.getConnection(username, password);
        try {

            employee = employeeDao.getEmployeeById(con, id);
            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return new Employee();
    }
}
